package com.example.performancekeeper.api.common.queryCounter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class LoggingInterceptor implements HandlerInterceptor {
    private static final String queryCountLogFormat = "STATUS CODE = {}, METHOD = {}, URL = {}, QUERY COUNT = {}";
    private static final String queryCountCommentFormat = "Possible N + 1 Problem Executed Queries = {}";
    private static final int queryCountWarningLimit = 7;
    private final ApiQueryCounter apiQueryCounter;

    public LoggingInterceptor(ApiQueryCounter apiQueryCounter) {
        this.apiQueryCounter = apiQueryCounter;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler, final Exception ex) throws Exception {

        final int queryCount = apiQueryCounter.getCount();
        log.info(queryCountLogFormat, response.getStatus(), request.getMethod(), request.getRequestURI(), queryCount);
        if (queryCount >= queryCountWarningLimit) log.warn(queryCountCommentFormat, queryCount);
    }
}
