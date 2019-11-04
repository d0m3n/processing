package be.better.at.processing.repository;

import be.better.at.processing.model.jpa.DiseaseJPA;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiseaseRepositoryJPA extends CrudRepository<DiseaseJPA, Long> {
    Optional<DiseaseJPA> findByName(String name);
}
