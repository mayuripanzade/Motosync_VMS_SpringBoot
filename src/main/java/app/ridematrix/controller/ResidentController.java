package app.ridematrix.controller;

import app.ridematrix.converter.ResidentMapper;
import app.ridematrix.converter.ResidentMapperWithVehicle;
import app.ridematrix.dto.GetResidentDataRequest;
import app.ridematrix.entity.Resident;
import app.ridematrix.service.ResidentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/residents")
@Slf4j
@Tag(name = "Resident Controller", description = "APIs to manage residents and their vehicles")
public class ResidentController
{
    @Autowired
    private ResidentService residentService;

    @PostMapping("/addResident")
    @Operation(summary = "Create a new resident with vehicles")
    public ResponseEntity<String> createResident(@Valid @RequestBody Resident resident){
        log.info("Received request to create resident with firstName: {}, lastName: {}", resident.getFName(), resident.getLName());

        String msg = residentService.saveResident(resident);
        log.info("Resident creation result: {}", msg);
        return new ResponseEntity<>(msg,HttpStatus.CREATED);
    }

    @GetMapping("/getAllResident")
    @Operation(summary = "Get all resident details")

    public ResponseEntity<List<GetResidentDataRequest>> getAllResidents() {
        log.info("Fetching all residents");

        List<Resident> residents = residentService.getAllResidents();
        log.debug("Number of residents fetched: {}", residents.size());

        List<GetResidentDataRequest> dtoList = residents.stream()
                .map(ResidentMapper::toDto)
                .collect(Collectors.toList());
        log.info("Returning resident DTO list with {} entries", dtoList.size());
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/getResidentByName")
    @Operation(summary = "Get Resident details with name")
    public ResponseEntity<?> getResidentByName(
            @Pattern(regexp = "^[A-Za-z]*$", message = "First name should not contain numbers")
            @Parameter(description = "First name of resident (optional)")
            @RequestParam(required = false) String fName,

            @Pattern(regexp = "^[A-Za-z]*$", message = "Last name should not contain numbers")
            @Parameter(description = "Last name of resident (optional)")
            @RequestParam(required = false) String lName) {

        log.info("Searching resident by name - firstName: {}, lastName: {}", fName, lName);

        List<Resident> residentList = residentService.getResidentByName(fName, lName);

        if (residentList.isEmpty()) {
            log.warn("No residents found with firstName: {}, lastName: {}", fName, lName);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Resident not found with given name inputs.");
        }
        log.debug("Found {} resident(s)", residentList.size());

        List<GetResidentDataRequest> dtoList = residentList.stream()
                .map(ResidentMapperWithVehicle::toGetResident)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }
}
