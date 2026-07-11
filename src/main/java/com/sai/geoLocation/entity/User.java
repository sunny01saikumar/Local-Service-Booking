package com.sai.geoLocation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String phone;
    @Column(name = "password_hash")
    private String passwordHash;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "email_verified", nullable = false)
    private boolean emailVerified;
    @Column(name = "phone_verified", nullable = false)
    private boolean phoneVerified;
    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;
}
