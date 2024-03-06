package com.example.performancekeeper.api.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
/**
 * 강의실 멤버가 자신의 별명을 수정할 때, 새로운 별명을 담는 Dto 객체
 */
@Data
public class NicknameUpdateDto {
    @NotBlank
    private String nickname;
}
