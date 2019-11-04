package be.better.at.processing.model.jpa;

import be.better.at.processing.model.jpa.base.LongIdAuditJPA;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "document_report")
@Getter
@Setter
@ToString
@Audited
public class DocumentReportJPA extends LongIdAuditJPA {

    private long executionTime;

    private long doctorId;

    private String error;

    @Enumerated(EnumType.STRING)
    private DocumentSourceEnum documentSource;

    public DocumentReportJPA(long id) {
        this.setId(id);
    }
}
