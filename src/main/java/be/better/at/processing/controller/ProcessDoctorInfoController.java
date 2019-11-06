package be.better.at.processing.controller;

import be.better.at.processing.model.api.request.ProcessDoctorRequest;
import be.better.at.processing.service.ProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/process")
public class ProcessDoctorInfoController {

    @Autowired
    private ProcessingService processingService;

    @PostMapping(path = "/doctor", consumes = { "application/json", "application/xml" })
    public ResponseEntity<Void> processJson(@RequestBody ProcessDoctorRequest request) {
        log.info("Got HTTP request for processing.");
        processingService.saveDoctorData(request);
        return ResponseEntity.noContent().build();
    }
}
