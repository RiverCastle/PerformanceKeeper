package com.example.performancekeeper.api.common.queryCounter;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * N + 1 문제를 탐지하는 목적으로 API 별로 쿼리의 개수를 카운트하는 역할의 클래스입니다.
 */
@Component
@RequestScope
@Getter
public class ApiQueryCounter {
    private int count;
    public void increaseCount() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
