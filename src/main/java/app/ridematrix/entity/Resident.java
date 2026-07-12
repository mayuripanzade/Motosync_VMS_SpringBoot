package app.ridematrix.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

//Resident entity
@Entity
@Setter
@Getter
@ToString
public class Resident {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @NotBlank(message = "First name is Required")
    @JsonProperty("fName")
    private String fName;

    @NotBlank(message = "Last name is Required")
    @JsonProperty("lName")
    private String lName;

    @Pattern(regexp = "^[A-Za-z0-9-]+$", message = "Flat number must be alphanumeric with optional hyphens")
    private String flatNo;

    @NotNull(message = "Mobile no is Required")
    private long mobNo;

    //Email annotation for valid email
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Resident Type is Required")
    @Enumerated(EnumType.STRING)
    private ResidentType residentType;

    // Enum definition
    public enum ResidentType {
        OWNER,
        TENANT
    }
    
    //one to many mapping
    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private List<Vehicle> vehicleList = new ArrayList<>();

    @OneToMany(mappedBy = "resident")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    //not passing visitors in the resident info.
    private List<Visitors> visitorsList = new ArrayList<>();
}
