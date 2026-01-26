package com.codeshare.airline.security.client.feign;

import com.codeshare.airline.core.exceptions.CSMBusinessException;
import com.codeshare.airline.core.exceptions.CSMErrorCodes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class CSMOAuth2FeignErrorDecoder implements ErrorDecoder {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        String body = extractBody(response);
        JsonNode root = parseJson(body);

        // 1️⃣ Try platform-standard error format
        if (root != null && root.has("error")) {
            JsonNode errorNode = root.get("error");

            String code = getText(errorNode, "code");
            String message = getText(errorNode, "message");
            String details = getText(errorNode, "details");

            CSMErrorCodes resolved =
                    CSMErrorCodes.fromCode(code)
                            .orElse(mapStatusToErrorCode(response.status()));

            return new CSMBusinessException(
                    resolved,
                    message != null ? message : resolved.getDefaultMessage(),
                    details
            );
        }

        // 2️⃣ Fallback to HTTP status mapping
        CSMErrorCodes fallback = mapStatusToErrorCode(response.status());
        return new CSMBusinessException(
                fallback,
                fallback.getDefaultMessage(),
                body
        );
    }

    private String extractBody(Response response) {
        try {
            return response.body() != null
                    ? Util.toString(response.body().asReader())
                    : null;
        } catch (Exception e) {
            return null;
        }
    }

    private JsonNode parseJson(String body) {
        try {
            return body != null ? mapper.readTree(body) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private String getText(JsonNode node, String field) {
        return node.has(field) ? node.get(field).asText() : null;
    }

    private CSMErrorCodes mapStatusToErrorCode(int status) {
        return switch (status) {

            case 400 -> CSMErrorCodes.BAD_REQUEST;
            case 401 -> CSMErrorCodes.AUTH_INVALID_CREDENTIALS;
            case 403 -> CSMErrorCodes.AUTH_USER_LOCKED;
            case 404 -> CSMErrorCodes.DATA_NOT_FOUND;
            case 405 -> CSMErrorCodes.METHOD_NOT_ALLOWED;
            case 415 -> CSMErrorCodes.UNSUPPORTED_MEDIA_TYPE;
            case 429 -> CSMErrorCodes.API_RATE_LIMIT_EXCEEDED;

            case 500 -> CSMErrorCodes.INTERNAL_SERVER_ERROR;
            case 502 -> CSMErrorCodes.BAD_GATEWAY;
            case 503 -> CSMErrorCodes.SERVICE_UNAVAILABLE;
            case 504 -> CSMErrorCodes.BAD_GATEWAY;

            default -> CSMErrorCodes.API_SERVICE_ERROR;
        };
    }
}

