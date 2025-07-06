package com.monouzbekistanbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "seasons")
public class Season {
    @Id
    @Column(name = "season_id", nullable = false)
    private UUID seasonId;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "created_At", nullable = false)
    private LocalDateTime createdAt;
}
