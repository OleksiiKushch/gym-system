package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.dto.TrainingDto;
import org.example.dto.form.CreateTrainingForm;
import org.example.facade.TrainingFacade;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Getter
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/trainings", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Training", description = """
        Some operations related to Training, in particular, the creation of Training entry.
        """)
public class TrainingController {

    private final TrainingFacade trainingFacade;
    private final ModelMapper modelMapper;

    @PostMapping
    @Operation(summary = "Organize (create) a new Training entry in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully organized (created a new training entry in the system).",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data (form validation failed, for example some required fields are not present or the data format is incorrect).",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> organizeTraining(@RequestBody @Valid CreateTrainingForm form, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors());
        }
        getTrainingFacade().createTraining(mapCreationFormToTrainingDto(form));
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{trainingId}")
    @Operation(summary = "Delete the specific Training with the supplied id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the specific Training with the supplied id.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target training with the id (which is set as a path variable) does not exist.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> deleteTrainee(@PathVariable("trainingId") Integer trainingId) {
        getTrainingFacade().deleteTraining(trainingId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    private TrainingDto mapCreationFormToTrainingDto(CreateTrainingForm form) {
        return modelMapper.map(form, TrainingDto.class);
    }
}
