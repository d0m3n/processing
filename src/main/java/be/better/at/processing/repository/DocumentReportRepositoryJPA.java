package be.better.at.processing.repository;

import be.better.at.processing.model.jpa.DocumentReportJPA;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentReportRepositoryJPA extends CrudRepository<DocumentReportJPA, Long> {
}
