package com.example.performancekeeper.api.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NicknameUpdateDto {
    @NotBlank
    private String nickname;
}
