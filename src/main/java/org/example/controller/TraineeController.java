package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import org.example.dto.TraineeDto;
import org.example.dto.form.RegistrationTraineeForm;
import org.example.dto.form.UpdateTraineeForm;
import org.example.dto.form.search.SearchTraineeTrainingsPayload;
import org.example.facade.TraineeFacade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Getter
@RestController
@RequestMapping(value = "/trainees", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Trainee", description = """
        Operations for creating, updating, retrieving, deleting, and performing other manipulations of information\s
        related to Trainees in the application.
        """)
public class TraineeController {

    private TraineeFacade traineeFacade;
    private ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Register (create) a new Trainee in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Registration successful (created a new Trainee profile in the system).",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data (form validation failed, for example some required fields are not present or the data format is incorrect).",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> registerTrainee(@RequestBody @Valid RegistrationTraineeForm form, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(getTraineeFacade().registerTrainee(mapRegistrationFormToTraineeDto(form)));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Edit (update) a Trainee profile in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edit successful, changes saved (update a Trainee profile in the system).",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data (form validation failed, for example some required fields are not present or the data format is incorrect).",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as a path variable) does not exist, or has an\s
                    inappropriate type (role), such as Trainer.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> updateTrainee(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                           @RequestBody @Valid UpdateTraineeForm form, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors());
        }
        return ResponseEntity.ok()
                .body(getTraineeFacade().updateTrainee(username, mapUpdateFormToTraineeDto(form)));
    }

    @PutMapping("/{username}/trainers")
    @Operation(summary = "Edit (update) a Trainee's Trainer list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edit successful a Trainee's Trainer list, changes saved (update a Trainee's Trainer list).",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data (some Trainers in the request parameter list either do not exist or are not Trainers).",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as a path variable) does not exist, or has an\s
                    inappropriate type (role), such as Trainer.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> updateTraineeTrainers(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                                   @RequestParam("usernamesOfTrainers") List<String> usernamesOfTrainers) {
        return ResponseEntity.ok()
                .body(getTraineeFacade().updateTraineeTrainers(username, usernamesOfTrainers));
    }

    @GetMapping("/{username}")
    @Operation(summary = "Retrieve the specific Trainee profile with the supplied username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the Trainee profile with the supplied username.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as a path variable) does not exist, or has an\s
                    inappropriate type (role), such as Trainer.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> getTraineeProfile(@RequestHeader("Authorization") String token, @PathVariable("username") String username) {
        return ResponseEntity.ok()
                .body(getTraineeFacade().getTraineeProfile(username));
    }

    @GetMapping("/{username}/trainings")
    @Operation(summary = "Retrieve the list of Trainings for specific Trainee with the supplied username and search criteria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of Trainings specific Trainee and search criteria.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data (form validation failed, for example date format is incorrect).",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as a path variable) does not exist, or has an\s
                    inappropriate type (role), such as Trainer.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> getTraineeTrainings(@RequestHeader("Authorization") String token, @PathVariable("username") String username,
                                                 @RequestBody @Valid SearchTraineeTrainingsPayload form, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors());
        }
        return ResponseEntity.ok()
                .body(getTraineeFacade().getTraineeTrainings(username, form));
    }

    @DeleteMapping("/{username}")
    @Operation(summary = "Delete the specific Trainee profile with the supplied username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the specific Trainee profile with the supplied username.",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad request. This occurs when the trainee who is being removed has an active status of `true`.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as a path variable) does not exist, or has an\s
                    inappropriate type (role), such as Trainer.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> deleteTrainee(@RequestHeader("Authorization") String token, @PathVariable("username") String username) {
        getTraineeFacade().deleteTrainee(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    private TraineeDto mapRegistrationFormToTraineeDto(RegistrationTraineeForm form) {
        return getModelMapper().map(form, TraineeDto.class);
    }

    private TraineeDto mapUpdateFormToTraineeDto(UpdateTraineeForm form) {
        return getModelMapper().map(form, TraineeDto.class);
    }

    @Autowired
    public void setTraineeFacade(TraineeFacade traineeFacade) {
        this.traineeFacade = traineeFacade;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
