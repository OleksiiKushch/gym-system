package org.example.controller;

import org.example.dto.TrainingDto;
import org.example.dto.form.CreateTrainingForm;
import org.example.facade.TrainingFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class TrainingControllerUnitTest {

    @InjectMocks
    TrainingController testInstance;

    @Mock
    TrainingFacade trainingFacade;
    @Mock
    ModelMapper modelMapper;

    @Mock
    CreateTrainingForm createTrainingForm;
    @Mock
    BindingResult result;

    @Mock
    TrainingDto trainingDto;
    @Mock
    List<FieldError> errors;

    @Test
    void shouldOrganizeTraining_whenNoErrors() {
        doReturn(false).when(result).hasErrors();
        doReturn(trainingDto).when(modelMapper).map(createTrainingForm, TrainingDto.class);
        doNothing().when(trainingFacade).createTraining(trainingDto);

        var actualResult = testInstance.organizeTraining(createTrainingForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.CREATED, actualResult.getStatusCode());
    }

    @Test
    void shouldDoesntOrganizeTraining_whenSomeErrors() {
        doReturn(true).when(result).hasErrors();
        doReturn(errors).when(result).getFieldErrors();

        var actualResult = testInstance.organizeTraining(createTrainingForm, result);

        assertNotNull(actualResult);
        assertEquals(HttpStatus.BAD_REQUEST, actualResult.getStatusCode());
        assertEquals(errors, actualResult.getBody());
    }
}