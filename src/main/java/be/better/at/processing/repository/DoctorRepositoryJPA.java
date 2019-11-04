package be.better.at.processing.repository;

import be.better.at.processing.model.jpa.DoctorJPA;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepositoryJPA extends CrudRepository<DoctorJPA, Long> {
}
