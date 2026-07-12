package app.ridematrix.dto;

import app.ridematrix.entity.Resident;
import app.ridematrix.entity.Vehicle;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

//DTO FOr resident data
@Setter
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetResidentDataRequest
{
    private int id;
    @NotBlank(message = "First name is Required")
    private String fName;
    @NotBlank(message = "Last name is Required")
    private String lName;
    private String flatNo;
    @NotNull(message = "Mobile no is Required")
    private long mobNo;
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Resident Type is Required")
    private Resident.ResidentType residentType;

    public enum ResidentType {
        OWNER,
        TENANT
    }
    private List<Vehicle> vehicleList;
}
