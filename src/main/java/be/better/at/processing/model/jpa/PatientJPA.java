package be.better.at.processing.model.jpa;

import be.better.at.processing.model.api.common.Disease;
import be.better.at.processing.model.jpa.base.LongIdAuditJPA;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patient")
@Getter
@Setter
@ToString(exclude = "doctor")
@Audited
@NoArgsConstructor
public class PatientJPA extends LongIdAuditJPA {

    @ManyToOne
    private DoctorJPA doctor;

    private String firstName;

    private String lastName;

    @ManyToMany
    @JoinTable(name = "disease_patient",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "disease_id")
    )
    private List<DiseaseJPA> diseases = new ArrayList<>();

    public PatientJPA(Long id, DoctorJPA doctor) {
        this.setId(id);
        this.doctor = doctor;
    }

    @Transactional
    public void updatePatient(String firstName, String lastName, List<DiseaseJPA> diseases) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.diseases.addAll(diseases);
    }
}
