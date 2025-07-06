package com.monouzbekistanbackend.repository;

import com.monouzbekistanbackend.entity.Season;
import com.monouzbekistanbackend.enums.SeasonEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeasonRepository extends JpaRepository<Season, UUID> {
    Optional<Season> findBySeasonId(UUID seasonId);

    boolean existsByName(String name);

    List<Season> findByName(String name);
}
