package naeilmolae.global.config.log;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;

@Component
public class RequestResponseLoggingFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        ResponseWrapper responseWrapper = new ResponseWrapper((HttpServletResponse) response);
        String requestURI = httpRequest.getRequestURI();

        try {
            // Swagger 및 API Docs 요청 제외
            if (requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/v3/api-docs")) {
                chain.doFilter(request, response);
                return;
            }

            // 요청 로깅
            logger.info("Incoming Request: Method={}, URI={}, Payload={}",
                    httpRequest.getMethod(), httpRequest.getRequestURI(), getRequestPayload(httpRequest));

            chain.doFilter(request, responseWrapper);

            // 응답 로깅
            logger.info("Outgoing Response: Status={}, Payload={}",
                    responseWrapper.getStatus(), responseWrapper.getData());
        } catch (Exception ex) {
            // 예외 발생 시 로깅
            logger.error("Error occurred while processing the request. URI={}, Method={}", requestURI, httpRequest.getMethod(), ex);
            throw ex; // 예외를 다시 던져야 이후 필터나 컨트롤러로 전달됨
        }
    }

    private String getRequestPayload(HttpServletRequest request) {
        StringBuilder payload = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                payload.append(line);
            }
        } catch (IOException e) {
            logger.error("Error reading request payload", e);
        }
        return payload.toString();
    }
}