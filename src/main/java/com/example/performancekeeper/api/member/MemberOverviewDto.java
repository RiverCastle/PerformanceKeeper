package com.example.performancekeeper.api.member;

import lombok.Data;

@Data
public class MemberOverviewDto {
    private Long id;
    private String name;

    public MemberOverviewDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MemberOverviewDto fromEntity(MemberEntity entity) {
        return new MemberOverviewDto(entity.getId(), entity.getUser().getUsername());
    }
}
