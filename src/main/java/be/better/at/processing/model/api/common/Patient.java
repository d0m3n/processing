package be.better.at.processing.model.api.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Patient {

    @NotNull(message = "Patient id must not be null")
    private Long id;

    @NotNull(message = "First name must not be null")
    @JsonProperty("first_name")
    @JacksonXmlProperty(localName = "first_name")
    private String firstName;

    @NotNull(message = "Last name must not be null")
    @JsonProperty("last_name")
    @JacksonXmlProperty(localName = "last_name")
    private String lastName;

    private List<Disease> diseases;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }
}
