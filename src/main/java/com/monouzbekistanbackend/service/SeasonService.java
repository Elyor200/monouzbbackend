package com.monouzbekistanbackend.service;

import com.monouzbekistanbackend.dto.SeasonDto;
import com.monouzbekistanbackend.entity.Season;
import com.monouzbekistanbackend.repository.SeasonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SeasonService {
    private final SeasonRepository seasonRepository;

    public SeasonService(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    public Season createSeason(String name) {
        if (seasonRepository.existsByName(name)) {
            throw new RuntimeException("Season already exists");
        }
        Season season = new Season();
        season.setSeasonId(UUID.randomUUID());
        season.setName(name);
        season.setCreatedAt(LocalDateTime.now());
        return seasonRepository.save(season);
    }

    public List<SeasonDto> getAllSeason() {
        List<Season> seasons = seasonRepository.findAll();
        List<SeasonDto> seasonDtoList = new ArrayList<>();
        for (Season season : seasons) {
            SeasonDto seasonDto = new SeasonDto();
            seasonDto.setSeasonId(season.getSeasonId());
            seasonDto.setSeasonName(season.getName());
            seasonDtoList.add(seasonDto);
        }
        return seasonDtoList;
    }
}
