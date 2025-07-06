package com.monouzbekistanbackend.service;

import com.monouzbekistanbackend.entity.Season;
import com.monouzbekistanbackend.repository.SeasonRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
}
