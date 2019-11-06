package be.better.at.processing.model.api.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Disease {

    private Long id;

    @NotNull(message = "Disease name must not be null")
    private String disease;

    public Disease(@NotNull(message = "Disease name must not be null") String disease) {
        this.disease = disease;
    }
}
