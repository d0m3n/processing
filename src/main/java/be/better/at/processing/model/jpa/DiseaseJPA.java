package be.better.at.processing.model.jpa;

import be.better.at.processing.model.jpa.base.LongIdAuditJPA;
import lombok.*;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "disease")
@Getter
@Setter
@ToString(exclude = "patients")
@Audited
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseJPA extends LongIdAuditJPA {

    @ManyToMany(mappedBy = "diseases")
    private List<PatientJPA> patients;

    @Column(unique = true)
    private String name;

    public DiseaseJPA(Long id, String name) {
        this.setId(id);
        this.name = name;
    }
}
