package be.better.at.processing.model.api.request;

import be.better.at.processing.model.api.common.Patient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcessDoctorRequest {
    @NotNull(message = "Doctor id must not be null")
    private Long id;

    @NotNull(message = "Department must not be null")
    private String department;

    private List<Patient> patients;
}
