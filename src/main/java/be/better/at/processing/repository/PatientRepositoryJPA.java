package be.better.at.processing.repository;

import be.better.at.processing.model.jpa.PatientJPA;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepositoryJPA extends CrudRepository<PatientJPA, Long> {
}
