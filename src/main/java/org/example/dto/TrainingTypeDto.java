package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.TrainingTypeEnum;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingTypeDto {

    private Integer id;
    private TrainingTypeEnum name;
}
