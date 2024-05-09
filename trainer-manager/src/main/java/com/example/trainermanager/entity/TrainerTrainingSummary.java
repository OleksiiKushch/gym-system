package com.example.trainermanager.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Month;
import java.util.Map;

@Getter
@Setter
@ToString
@Document(collection = "trainer_training_summary")
@CompoundIndexes({
        @CompoundIndex(name = "username_firstname_idx", def = "{'trainerUsername': 1, 'trainerFirstName': 1}"),
        @CompoundIndex(name = "username_lastname_idx", def = "{'trainerUsername': 1, 'trainerLastName': 1}"),
        @CompoundIndex(name = "username_firstname_lastname_idx", def = "{'trainerUsername': 1, 'trainerFirstName': 1, 'trainerLastName': 1}")
})
public class TrainerTrainingSummary {

    @Id
    private String id;

    @Indexed(unique = true)
    private String trainerUsername;

    private String trainerFirstName;

    private String trainerLastName;

    private Boolean trainerStatus;

    private Map<Integer, Map<Month, Integer>> report;
}
