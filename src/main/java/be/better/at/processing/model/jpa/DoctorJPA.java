package be.better.at.processing.model.jpa;

import be.better.at.processing.model.jpa.base.LongIdAuditJPA;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "doctor")
@Getter
@Setter
@ToString
@Audited
@NoArgsConstructor
public class DoctorJPA extends LongIdAuditJPA {

    @OneToMany(mappedBy = "doctor", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<PatientJPA> patients = new ArrayList<>();

    private String department;

    public DoctorJPA(Long id) {
        this.setId(id);
    }

    public void updatePatients(List<PatientJPA> patients) {
        // set patients only to elements that are not going to be updated
        this.patients = this.patients.stream().filter(patientJPA -> !patients.stream().anyMatch(patient -> patientJPA.getId().equals(patient.getId()))).collect(Collectors.toList());
        // add updated/added patients to list
        this.patients.addAll(patients);
    }
}
