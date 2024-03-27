package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.Getter;
import org.example.config.security.bruteforceprevent.LoginAttemptService;
import org.example.dto.form.ChangePasswordForm;
import org.example.dto.form.LoginForm;
import org.example.facade.UserFacade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.example.constants.GeneralConstants.TOO_MANY_REQUESTS_EXCEPTION_MSG;

@Getter
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User", description = """
        Basic operations related to users such as login, logout, changing passwords, and toggling user activations\s
        (activating/deactivating profiles).
        """)
public class UserController {

    private UserFacade userFacade;
    private ModelMapper modelMapper;
    private LoginAttemptService loginAttemptService;

    @PostMapping("/login")
    @Operation(summary = "Log in the user (validate user credentials (authenticate), and if everything is okay, save the user in a temporary simple session).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully logged in (user saved in temporary simple session).",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data (form validation failed, for example some required fields are not present or the data format is incorrect).",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginForm form, BindingResult result) {
        if (getLoginAttemptService().isBlocked()) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(TOO_MANY_REQUESTS_EXCEPTION_MSG);
        }
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors());
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(getUserFacade().login(form.getUsername(), form.getPassword()));
    }

    @PutMapping("/users/{username}")
    @Operation(summary = "Change the user password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password changed successfully (user's current password updated to the new password).",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid input data (form validation failed, for example some required fields are not present or the data format is incorrect).",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as path variable) does not exist.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> changePassword(@PathVariable("username") String username,
                                            @RequestBody @Valid ChangePasswordForm form, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors());
        }
        getUserFacade().changePassword(username, form.getCurrentPassword(), form.getNewPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PatchMapping("/users/{username}")
    @Operation(summary = "Toggle user activation (activate/deactivate user profile).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activation successfully toggled (user's current status changed to the opposite).",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "You are not authenticated to view the resource.",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = """
                    The resource you were trying to reach is not found. This occurs when a user is authenticated, but\s
                    the target user with the username (which is set as path variable) does not exist.
                    """,
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Application failed to process the request.",
                    content = @Content),
    })
    public ResponseEntity<?> toggleUserActivation(@PathVariable("username") String username) {
        getUserFacade().toggleUserActivation(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @Autowired
    public void setUserFacade(@Qualifier("defaultUserFacade") UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setLoginAttemptService(LoginAttemptService loginAttemptService) {
        this.loginAttemptService = loginAttemptService;
    }
}
