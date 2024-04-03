package org.example.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import org.example.dto.TrainingDto;
import org.example.dto.form.CreateTrainingForm;
import org.example.facade.TrainingFacade;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import static org.example.constants.GeneralConstants.OK_REQUEST;

@Getter
@Controller
public class TrainingController {

    private TrainingFacade trainingFacade;
    private ModelMapper modelMapper;

    public String organizeTraining(@Valid CreateTrainingForm form) {
        getTrainingFacade().createTraining(mapCreationFormToTrainingDto(form));
        return OK_REQUEST;
    }

    private TrainingDto mapCreationFormToTrainingDto(CreateTrainingForm form) {
        return modelMapper.map(form, TrainingDto.class);
    }

    @Autowired
    public void setTrainingFacade(TrainingFacade trainingFacade) {
        this.trainingFacade = trainingFacade;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
