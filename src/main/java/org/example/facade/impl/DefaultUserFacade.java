package org.example.facade.impl;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.example.dto.TraineeDto;
import org.example.dto.TrainerDto;
import org.example.entity.Trainee;
import org.example.entity.Trainer;
import org.example.entity.User;
import org.example.facade.UserFacade;
import org.example.mapper.Mapper;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class DefaultUserFacade implements UserFacade {

    private UserService userService;
    private TraineeService traineeService;
    private TrainerService trainerService;
    private Mapper<TraineeDto, Trainee> traineeMapper;
    private Mapper<TrainerDto, Trainer> trainerMapper;

    @Override
    public void registerTrainee(TraineeDto traineeDto) {
        Trainee trainee = getTraineeMapper().map(traineeDto);
        processNewUserProfile(trainee);
        getTraineeService().createTrainee(trainee);
    }

    @Override
    public void registerTrainer(TrainerDto trainerDto) {
        Trainer trainer = getTrainerMapper().map(trainerDto);
        processNewUserProfile(trainer);
        getTrainerService().createTrainer(trainer);
    }

    private void processNewUserProfile(User user) {
        user.setActive(Boolean.TRUE);
        user.setUsername(getUserService().calculateUsername(user));
        if (StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(getUserService().generateRandomPassword());
        }
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setTraineeService(TraineeService traineeService) {
        this.traineeService = traineeService;
    }

    @Autowired
    public void setTrainerService(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @Autowired
    public void setTraineeMapper(Mapper<TraineeDto, Trainee> traineeMapper) {
        this.traineeMapper = traineeMapper;
    }

    @Autowired
    public void setTrainerMapper(Mapper<TrainerDto, Trainer> trainerMapper) {
        this.trainerMapper = trainerMapper;
    }
}
