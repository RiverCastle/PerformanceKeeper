package com.example.performancekeeper.api.users;

import com.example.performancekeeper.api.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class UserEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String username;
    @NotBlank
    private String encodedPW;
    @NotBlank
    private String role;

    public UserEntity(String username, String encodedPW, String role) {
        this.username = username;
        this.encodedPW = encodedPW;
        this.role = role;
    }
}
