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

    private TrainingDto mapCreationFormToTrainingDto(CreateTrainingForm form) {
        return modelMapper.map(form, TrainingDto.class);
    }
}
