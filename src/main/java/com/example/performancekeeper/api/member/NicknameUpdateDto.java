package com.example.performancekeeper.api.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NicknameUpdateDto {
    @NotBlank
    private String nickname;
}
