package be.better.at.processing.controller;

import be.better.at.processing.model.api.common.Disease;
import be.better.at.processing.model.api.common.Patient;
import be.better.at.processing.model.jpa.PatientJPA;
import be.better.at.processing.repository.PatientRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientRepositoryJPA patientRepositoryJPA;

    @GetMapping
    public ResponseEntity<Patient> getPatientInfo(@RequestParam Long id) {
        log.info("Got request for patient info. patientId={}", id);

        PatientJPA patientJPA = patientRepositoryJPA.findById(id).orElse(null);
        if (patientJPA == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                new Patient(
                    patientJPA.getId(),
                    patientJPA.getFirstName(),
                    patientJPA.getLastName(),
                    patientJPA.getDiseases().stream().map(diseaseJPA -> new Disease(diseaseJPA.getId(), diseaseJPA.getName())).collect(Collectors.toList())
                )
        );
    }

    @GetMapping(path = "list")
    public ResponseEntity<List<Patient>> getPatientList() {
        log.info("Got request for patient list.");

        return ResponseEntity.ok(
                StreamSupport.stream(patientRepositoryJPA.findAll().spliterator(), false)
                        .map(patientJPA -> new Patient(
                                    patientJPA.getId(),
                                    patientJPA.getFirstName(),
                                    patientJPA.getLastName(),
                                    patientJPA.getDiseases().stream().map(diseaseJPA -> new Disease(diseaseJPA.getId(), null)).collect(Collectors.toList())
                                )
                            ).collect(Collectors.toList())

        );
    }
}
