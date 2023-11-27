package com.example.performancekeeper.api.users;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String encodedPW;

    public UserEntity(String username, String encodedPW) {
        this.username = username;
        this.encodedPW = encodedPW;
    }
}
