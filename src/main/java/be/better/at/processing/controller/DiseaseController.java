package be.better.at.processing.controller;

import be.better.at.processing.model.api.common.Disease;
import be.better.at.processing.repository.DiseaseRepositoryJPA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@RestController
@RequestMapping("/disease")
public class DiseaseController {

    @Autowired
    private DiseaseRepositoryJPA diseaseRepositoryJPA;

    @GetMapping(path = "list")
    public ResponseEntity<List<Disease>> getDiseaseList() {
        log.info("Got request for disease list.");

        return ResponseEntity.ok(
                StreamSupport.stream(diseaseRepositoryJPA.findAll().spliterator(), false)
                        .map(diseaseJPA -> new Disease(diseaseJPA.getId(), diseaseJPA.getName()))
                        .collect(Collectors.toList())
        );
    }
}
