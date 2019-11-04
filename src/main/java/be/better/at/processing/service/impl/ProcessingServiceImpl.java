package be.better.at.processing.service.impl;

import be.better.at.processing.model.api.common.Patient;
import be.better.at.processing.model.api.request.ProcessDoctorRequest;
import be.better.at.processing.model.jpa.*;
import be.better.at.processing.repository.DiseaseRepositoryJPA;
import be.better.at.processing.repository.DoctorRepositoryJPA;
import be.better.at.processing.repository.DocumentReportRepositoryJPA;
import be.better.at.processing.repository.PatientRepositoryJPA;
import be.better.at.processing.service.ProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProcessingServiceImpl implements ProcessingService {

    @Autowired
    private DoctorRepositoryJPA doctorRepositoryJPA;

    @Autowired
    private PatientRepositoryJPA patientRepositoryJPA;

    @Autowired
    private DiseaseRepositoryJPA diseaseRepositoryJPA;

    @Autowired
    private DocumentReportRepositoryJPA documentReportRepositoryJPA;

    @Value("${processing.input}")
    private String inputFolder;

    @Value("${processing.output.success}")
    private String outputFolderSuccess;

    @Value("${processing.output.error}")
    private String outputFolderError;

    @Autowired
    private ObjectMapper jsonMapper;

    private XmlMapper xmlMapper;

    private Validator validator;

    @PostConstruct
    public void init() {
        // create folder structures
        File dir = new File(inputFolder);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        dir = new File(outputFolderSuccess);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        dir = new File(outputFolderError);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // initialize xml deserializer
        xmlMapper = new XmlMapper();

        // initialize java object constraints validator
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    @Transactional
    public void saveDoctorData(ProcessDoctorRequest request) {
        log.info("Processing http request for doctorId={}", request.getId());
        long start = System.currentTimeMillis();
        DocumentReportJPA report = new DocumentReportJPA(documentReportRepositoryJPA.count());
        report.setDocumentSource(DocumentSourceEnum.HTTP_REQUEST);
        report.setDoctorId(request.getId());

        // validate object
        Set<ConstraintViolation<ProcessDoctorRequest>> violations = validator.validate(request);
        if (violations.isEmpty()) {
            // object passed validation, process it
            processData(request);
        } else {
            // there was a validation error
            String error = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
            report.setError(error);
            log.error(error);
        }

        report.setExecutionTime(System.currentTimeMillis()-start);
        documentReportRepositoryJPA.save(report);

        log.info("Done processing http request for doctorId={}", request.getId());
    }

    @Override
    @Scheduled(fixedRate = 90000, initialDelay = 10000)
    @Transactional
    public void processFolder() {
        log.info("Processing input folder");

        File dir = new File(inputFolder);
        if (dir.listFiles().length == 0) {
            log.info("No files to process.");
            return;
        }

        // process file by file
        for (File file : dir.listFiles()) {
            log.info("Processing file={}", file.getName());
            long start = System.currentTimeMillis();

            DocumentReportJPA report = new DocumentReportJPA(documentReportRepositoryJPA.count());
            report.setDocumentSource(DocumentSourceEnum.FOLDER);

            try {
                // based on file extension choose correct deserializer
                ObjectMapper objectMapper = xmlMapper;
                if (file.getName().endsWith("json")) {
                    objectMapper = jsonMapper;
                }

                // deserialize input
                ProcessDoctorRequest request = objectMapper.readValue(file, ProcessDoctorRequest.class);
                report.setDoctorId(request.getId());

                // validate object
                Set<ConstraintViolation<ProcessDoctorRequest>> violations = validator.validate(request);
                if (violations.isEmpty()) {
                    // object passed validation, process it
                    processData(request);
                    moveFile(file, outputFolderSuccess);
                } else {
                    // there was a validation error
                    String error = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(","));
                    report.setError(error);
                    log.error(error);
                    moveFile(file, outputFolderError);
                }
            } catch (IOException e) {
                log.error("Exception reading file={}", file.getName(), e);
                report.setError("Exception reading file=" + file.getName() + ". Exception: " + e.getMessage());
                moveFile(file, outputFolderError);
            } catch (Exception e) {
                log.error("Exception processing file={}", file.getName(), e);
                report.setError("Exception processing file=" + file.getName() + ". Exception: " + e.getMessage());
                moveFile(file, outputFolderError);
            }

            // save report for each file
            report.setExecutionTime(System.currentTimeMillis()-start);
            documentReportRepositoryJPA.save(report);
        }

        log.info("Input folder processed.");
    }

    /**
     * Processes doctor request from web service or folder.
     * If doctor info is updated if exists, inserted if not.
     * Patient and diseases list is updated (not overwritten).
     *
     * @param request validated object of class ProcessDoctorRequest
     *
     * */
    @Transactional
    protected void processData(ProcessDoctorRequest request) {
        final DoctorJPA doctorJPA = doctorRepositoryJPA.findById(request.getId()).orElse(new DoctorJPA(request.getId()));
        doctorJPA.setDepartment(request.getDepartment());

        // create patient list for doctor
        List<PatientJPA> patientJPAList = new ArrayList<>();
        for (Patient patient : request.getPatients()) {
            PatientJPA patientJPA = patientRepositoryJPA.findById(patient.getId()).orElse(new PatientJPA(patient.getId(), doctorJPA));

            // prepare diseases jpa union
            List<DiseaseJPA> diseaseJPAList = patient.getDiseases().stream()
                    .filter(disease -> !patientJPA.getDiseases().stream().anyMatch(diseaseJPA -> diseaseJPA.getName().equals(disease)))
                    .map(disease -> {
                        DiseaseJPA diseaseJPA = diseaseRepositoryJPA.findByName(disease.getDisease()).orElse(null);
                        if (diseaseJPA == null) {
                            diseaseJPA = new DiseaseJPA(diseaseRepositoryJPA.count(), disease.getDisease());
                            diseaseRepositoryJPA.save(diseaseJPA);
                        }

                        return diseaseJPA;
                    })
                    .collect(Collectors.toList());

            patientJPA.updatePatient(patient.getFirstName(), patient.getLastName(), diseaseJPAList);

            patientJPAList.add(patientJPA);
        }

        doctorJPA.updatePatients(patientJPAList);

        doctorRepositoryJPA.save(doctorJPA);

    }

    /**
     * Moves @param file to @param folder.
     *
     * */
    private void moveFile(File file, String folder) {
        // check if file with this name already exists, if it does delete it
        File newFilePath = new File(folder + "/" + file.getName());
        if (newFilePath.exists()) {
            newFilePath.delete();
        }

        // move file from input folder to output folder
        if (!file.renameTo(newFilePath)) {
            log.error("Moving file={} to folder={} failed!", file.getName(), folder);
        }
    }
}
