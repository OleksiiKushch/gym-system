package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.facade.TrainingTypeFacade;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Getter
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/training-types", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Training Type", description = """
        Some operations related to retrieving Training Types.
        """)
public class TrainingTypeController {

    private final TrainingTypeFacade trainingTypeFacade;

    @GetMapping
    @Operation(summary = "Retrieve all existing Training Types in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all existing Training Types in the system.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> getTrainingTypes(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok()
                .body(getTrainingTypeFacade().getTrainingTypes());
    }
}
