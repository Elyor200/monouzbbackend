package com.monouzbekistanbackend.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
public class SeasonDto {
    private UUID seasonId;
    private String seasonName;
    private LocalDate createdAt;
}
