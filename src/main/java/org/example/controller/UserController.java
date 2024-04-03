package org.example.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.dto.form.ChangePasswordForm;
import org.example.dto.form.LoginForm;
import org.example.dto.form.RegistrationTraineeForm;
import org.example.dto.form.RegistrationTrainerForm;
import org.example.dto.form.UpdateTraineeForm;
import org.example.dto.form.UpdateTrainerForm;
import org.example.facade.UserFacade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static org.example.constants.GeneralConstants.BAD_REQUEST;
import static org.example.constants.GeneralConstants.OK_REQUEST;

@Getter
@Controller
public class UserController {

    private UserFacade userFacade;
    private ModelMapper modelMapper;

    public String registerTrainee(@Valid RegistrationTraineeForm form) {
        getUserFacade().registerTrainee(mapRegistrationFormToTraineeDto(form));
        return OK_REQUEST;
    }

    public String registerTrainer(@Valid RegistrationTrainerForm form) {
        getUserFacade().registerTrainer(mapRegistrationFormToTrainerDto(form));
        return OK_REQUEST;
    }

    public String loginUser(@Valid LoginForm form) {
        getUserFacade().login(form.getUsername(), form.getPassword());
        return OK_REQUEST;
    }

    public String logoutUser() {
        getUserFacade().logout();
        return OK_REQUEST;
    }

    public String changePassword(@Valid ChangePasswordForm form) {
        getUserFacade().changePassword(form.getCurrentPassword(), form.getNewPassword());
        return OK_REQUEST;
    }

    public String updateTrainee(@Valid UpdateTraineeForm form) {
        getUserFacade().updateTrainee(mapUpdateFormToTraineeDto(form));
        return OK_REQUEST;
    }

    public String updateTrainer(@Valid UpdateTrainerForm form) {
        getUserFacade().updateTrainer(mapUpdateFormToTrainerDto(form));
        return OK_REQUEST;
    }

    public String deactivateUser() {
        getUserFacade().deactivateUser();
        return OK_REQUEST;
    }

    public String activateUser(String username) {
        if (StringUtils.isEmpty(username)) {
            return BAD_REQUEST;
        }
        getUserFacade().activateUser(username);
        return OK_REQUEST;
    }

    public String deleteTrainee(String username) {
        getUserFacade().deleteTrainee(username);
        return OK_REQUEST;
    }

    private TraineeDto mapRegistrationFormToTraineeDto(RegistrationTraineeForm form) {
        return modelMapper.map(form, TraineeDto.class);
    }

    private TrainerDto mapRegistrationFormToTrainerDto(RegistrationTrainerForm form) {
        return modelMapper.map(form, TrainerDto.class);
    }

    private TraineeDto mapUpdateFormToTraineeDto(UpdateTraineeForm form) {
        return modelMapper.map(form, TraineeDto.class);
    }

    private TrainerDto mapUpdateFormToTrainerDto(UpdateTrainerForm form) {
        return modelMapper.map(form, TrainerDto.class);
    }

    @Autowired
    public void setUserFacade(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
