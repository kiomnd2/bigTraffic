package kr.kiomn2.bigtraffic.infrastructure.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        try {
            // 요청 로깅
            logRequest(wrappedRequest);

            // 다음 필터 체인 실행
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // 응답 로깅
            logResponse(wrappedRequest, wrappedResponse, duration);

            // 응답 본문을 실제 응답으로 복사
            wrappedResponse.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("======================== HTTP Request ========================\n");
        sb.append(String.format("[%s] %s\n", request.getMethod(), request.getRequestURI()));

        String queryString = request.getQueryString();
        if (queryString != null) {
            sb.append(String.format("Query String: %s\n", queryString));
        }

        sb.append("Headers:\n");
        Map<String, String> headers = getHeaders(request);
        headers.forEach((key, value) -> {
            // 민감한 정보는 마스킹
            if (key.equalsIgnoreCase("Authorization") && value != null) {
                sb.append(String.format("  %s: %s\n", key, maskToken(value)));
            } else {
                sb.append(String.format("  %s: %s\n", key, value));
            }
        });

        sb.append(String.format("Remote Address: %s\n", request.getRemoteAddr()));
        sb.append("==============================================================");

        log.info(sb.toString());
    }

    private void logResponse(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, long duration) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("======================== HTTP Response =======================\n");
        sb.append(String.format("[%s] %s\n", request.getMethod(), request.getRequestURI()));
        sb.append(String.format("Status: %d\n", response.getStatus()));
        sb.append(String.format("Duration: %d ms\n", duration));

        // 응답 헤더
        sb.append("Headers:\n");
        response.getHeaderNames().forEach(headerName -> {
            sb.append(String.format("  %s: %s\n", headerName, response.getHeader(headerName)));
        });

        // 응답 본문 (크기 제한)
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            String responseBody = new String(content, StandardCharsets.UTF_8);
            if (responseBody.length() > 1000) {
                responseBody = responseBody.substring(0, 1000) + "... (truncated)";
            }
            sb.append(String.format("Response Body: %s\n", responseBody));
        }

        sb.append("==============================================================");

        // 에러 상태 코드는 WARN 레벨로
        if (response.getStatus() >= 400) {
            log.warn(sb.toString());
        } else {
            log.info(sb.toString());
        }
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 20) {
            return "***";
        }
        // "Bearer " 제거 후 마스킹
        if (token.startsWith("Bearer ")) {
            String actualToken = token.substring(7);
            if (actualToken.length() > 10) {
                return "Bearer " + actualToken.substring(0, 10) + "..." + actualToken.substring(actualToken.length() - 10);
            }
        }
        return token.substring(0, 10) + "***";
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 정적 리소스는 로깅하지 않음
        return path.startsWith("/css/") ||
               path.startsWith("/js/") ||
               path.startsWith("/images/") ||
               path.startsWith("/favicon.ico");
    }
}
