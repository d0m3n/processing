package be.better.at.processing.controller;

import be.better.at.processing.model.api.common.Doctor;
import be.better.at.processing.model.jpa.DoctorJPA;
import be.better.at.processing.repository.DoctorRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    @Autowired
    private DoctorRepositoryJPA doctorRepositoryJPA;

    @GetMapping
    public ResponseEntity<Doctor> getDoctorInfo(@RequestParam(name = "id") Long id) {
        log.info("Got request for doctor info. doctorId={}", id);

        DoctorJPA doctorJPA = doctorRepositoryJPA.findById(id).orElse(null);
        if (doctorJPA == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                new Doctor(
                        doctorJPA.getId(),
                        doctorJPA.getDepartment(),
                        doctorJPA.getPatients().stream().map(patientJPA -> patientJPA.getId()).collect(Collectors.toList())
                )
        );
    }

    @GetMapping(path = "list")
    public ResponseEntity<List<Doctor>> getDoctorList() {
        log.info("Got request for doctor list.");

        return ResponseEntity.ok(
                StreamSupport.stream(doctorRepositoryJPA.findAll().spliterator(), false)
                        .map(doctorJPA -> new Doctor(
                                doctorJPA.getId(),
                                doctorJPA.getDepartment(),
                                doctorJPA.getPatients().stream().map(patientJPA -> patientJPA.getId()).collect(Collectors.toList())
                            )
                        ).collect(Collectors.toList())
        );
    }

}
