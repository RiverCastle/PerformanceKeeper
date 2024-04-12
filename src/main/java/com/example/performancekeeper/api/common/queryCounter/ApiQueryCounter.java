package com.example.performancekeeper.api.common.queryCounter;

import lombok.Getter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

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
