package com.codeshare.airline.common.services.config.feign;

import com.codeshare.airline.common.services.exceptions.BusinessException;
import com.codeshare.airline.common.services.exceptions.ErrorCodes;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {

        String body = extractBody(response);
        JsonNode json = parseJson(body);

        ErrorCodes errorCode = mapStatusToErrorCode(response.status());
        String message = extractMessage(json, errorCode, body);

        return new BusinessException(errorCode, message);
    }

    private String extractBody(Response response) {
        try {
            if (response.body() != null) {
                return Util.toString(response.body().asReader());
            }
        } catch (Exception ignored) {}
        return "NO_BODY";
    }

    private JsonNode parseJson(String body) {
        try {
            return mapper.readTree(body);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String extractMessage(JsonNode json, ErrorCodes defaultCode, String rawBody) {
        if (json != null) {
            if (json.has("message")) return json.get("message").asText();
            if (json.has("error")) return json.get("error").asText();
        }
        return defaultCode.getDefaultMessage() + " | " + rawBody;
    }

    private ErrorCodes mapStatusToErrorCode(int status) {
        switch (status) {

            case 400: return ErrorCodes.BAD_REQUEST;
            case 401: return ErrorCodes.UNAUTHORIZED;
            case 403: return ErrorCodes.ACCESS_DENIED;
            case 404: return ErrorCodes.DATA_NOT_FOUND;
            case 405: return ErrorCodes.METHOD_NOT_ALLOWED;
            case 415: return ErrorCodes.UNSUPPORTED_MEDIA_TYPE;
            case 429: return ErrorCodes.API_RATE_LIMIT_EXCEEDED;

            case 500: return ErrorCodes.INTERNAL_SERVER_ERROR;
            case 502: return ErrorCodes.BAD_GATEWAY;
            case 503: return ErrorCodes.SERVICE_UNAVAILABLE;
            case 504: return ErrorCodes.API_GATEWAY_TIMEOUT;

            default: return ErrorCodes.API_SERVICE_ERROR;
        }
    }
}
