package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import org.example.dto.TrainerDto;
import org.example.dto.form.RegistrationTrainerForm;
import org.example.dto.form.UpdateTrainerForm;
import org.example.dto.form.search.SearchTrainerTrainingsPayload;
import org.example.facade.TrainerFacade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Getter
@RestController
@RequestMapping(value = "/trainers", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trainer", description = """
        Operations for creating, updating, retrieving, and performing other manipulations of information\s
        related to Trainers in the application.
        """)
public class TrainerController {

    private TrainerFacade trainerFacade;
    private ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Register (create) a new Trainer in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registration successful (created a new Trainer profile in the system).",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data (form validation failed, for example some required fields are not present or the data format is incorrect).",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> registerTrainer(@RequestBody @Valid RegistrationTrainerForm form, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(getTrainerFacade().registerTrainer(getModelMapper().map(form, TrainerDto.class)));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Edit (update) a Trainer profile in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edit successful, changes saved (update a Trainer profile in the system).",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data (form validation failed, for example some required fields are not present or the data format is incorrect).",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as a path variable) does not exist, or has an\s
                    inappropriate type (role), such as Trainee.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> updateTrainer(@PathVariable("username") String username,
                                           @RequestBody @Valid UpdateTrainerForm form, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors());
        }
        return ResponseEntity.ok()
                .body(getTrainerFacade().updateTrainer(username, getModelMapper().map(form, TrainerDto.class)));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Retrieve the specific Trainer profile with the supplied username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Trainer profile with the supplied username.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as a path variable) does not exist, or has an\s
                    inappropriate type (role), such as Trainee.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> getTrainerProfile(@PathVariable("username") String username) {
        return ResponseEntity.ok()
                .body(getTrainerFacade().getTrainerProfile(username));
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Retrieve the list of Trainings for specific Trainer with the supplied username and search criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of Trainings specific Trainer and search criteria.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data (form validation failed, for example date format is incorrect).",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as a path variable) does not exist, or has an\s
                    inappropriate type (role), such as Trainee.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> getTrainerTrainings(@PathVariable("username") String username,
                                                 @RequestBody @Valid SearchTrainerTrainingsPayload form, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors());
        }
        return ResponseEntity.ok()
                .body(getTrainerFacade().getTrainerTrainings(username, form));
    }

    @GetMapping("/not-assigned/{traineeUsername}")
    @Operation(summary = "Retrieve the list of Trainers that are not assigned on specific Trainer with the supplied username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of Trainers that are not assigned on specific Trainer.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as a path variable) does not exist, or has an\s
                    inappropriate type (role) - not a Trainee.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> getTrainersThatNotAssignedOnTrainee(@PathVariable("traineeUsername") String username) {
        return ResponseEntity.ok()
                .body(getTrainerFacade().getTrainersThatNotAssignedOnTrainee(username));
    }

    @Autowired
    public void setTrainerFacade(TrainerFacade trainerFacade) {
        this.trainerFacade = trainerFacade;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
