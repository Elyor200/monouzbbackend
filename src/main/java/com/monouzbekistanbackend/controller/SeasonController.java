package com.monouzbekistanbackend.controller;

import com.monouzbekistanbackend.dto.SeasonDto;
import com.monouzbekistanbackend.entity.Season;
import com.monouzbekistanbackend.service.SeasonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/seasons")
public class SeasonController {
    private final SeasonService seasonService;

    public SeasonController(SeasonService seasonService) {
        this.seasonService = seasonService;
    }

    @PostMapping("/create-season")
    public ResponseEntity<?> createSeason(@RequestParam String name) {
        try {
            Season season = seasonService.createSeason(name);
            return ResponseEntity.ok(season);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getAllSeason")
    public ResponseEntity<?> getAllSeason() {
        List<SeasonDto> allSeason = seasonService.getAllSeason();
        return ResponseEntity.ok(allSeason);
    }
}
