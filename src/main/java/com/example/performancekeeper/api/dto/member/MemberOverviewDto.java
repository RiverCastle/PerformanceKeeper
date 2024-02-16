package com.example.performancekeeper.api.dto.member;

import com.example.performancekeeper.api.entity.MemberEntity;
import lombok.Data;

@Data
public class MemberOverviewDto {
    private Long id;
    private String nickname;

    public MemberOverviewDto(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public static MemberOverviewDto fromEntity(MemberEntity entity) {
        return new MemberOverviewDto(entity.getId(), entity.getNickname());
    }
}
