package com.codeshare.airline.platform.web.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.codeshare.airline.platform.core.response.CSMServiceResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class CSMResponseHandler implements ResponseBodyAdvice<Object> {

    private static final String APPLICATION_PACKAGE = "com.codeshare.airline.";
    private static final String PLATFORM_PACKAGE = "com.codeshare.airline.platform.";

    private final ObjectMapper objectMapper;

    public CSMResponseHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean supports(
            MethodParameter returnType,
            Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return returnType.hasMethodAnnotation(CSMResponse.class)
                || returnType.getContainingClass().isAnnotationPresent(CSMResponse.class)
                || isApplicationRestController(returnType);
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        if (shouldSkip(request)) {
            return body;
        }

        if (body instanceof CSMServiceResponse) {
            return body;
        }

        CSMServiceResponse<Object> wrappedBody = CSMServiceResponse.success(body);

        if (StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            try {
                return objectMapper.writeValueAsString(wrappedBody);
            } catch (JsonProcessingException ex) {
                throw new IllegalStateException("Failed to serialize CSM response", ex);
            }
        }

        return wrappedBody;
    }

    private boolean isApplicationRestController(MethodParameter returnType) {
        Class<?> controllerType = returnType.getContainingClass();
        Package controllerPackage = controllerType.getPackage();
        String packageName = controllerPackage == null ? "" : controllerPackage.getName();

        return controllerType.isAnnotationPresent(RestController.class)
                && packageName.startsWith(APPLICATION_PACKAGE)
                && !packageName.startsWith(PLATFORM_PACKAGE);
    }

    private boolean shouldSkip(ServerHttpRequest request) {
        String path = request.getURI().getPath();

        return path.startsWith("/actuator")
                || path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/.well-known")
                || path.startsWith("/internal/")
                || path.contains("/internal/")
                || path.equals("/auth/oidc/authorize")
                || path.equals("/auth/oidc/callback");
    }
}
