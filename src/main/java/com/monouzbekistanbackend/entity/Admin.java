package com.monouzbekistanbackend.entity;

import com.monouzbekistanbackend.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "admin")
public class Admin {
    @Id
    @Column(name = "admin_id", nullable = false)
    private String adminId;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "created_At", nullable = false)
    private LocalDateTime createdAt;
}
