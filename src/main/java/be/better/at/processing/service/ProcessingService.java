package be.better.at.processing.service;

import be.better.at.processing.model.api.request.ProcessDoctorRequest;

public interface ProcessingService {
    void saveDoctorData(ProcessDoctorRequest req);

    void processFolder();
}
