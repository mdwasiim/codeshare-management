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
        JsonNode json = parseJson(body);

        CSMErrorCodes errorCode = mapStatusToErrorCode(response.status());
        String message = extractMessage(json, errorCode, body);

        return new CSMBusinessException(errorCode, message);
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

    private String extractMessage(JsonNode json, CSMErrorCodes defaultCode, String rawBody) {
        if (json != null) {
            if (json.has("message")) return json.get("message").asText();
            if (json.has("error")) return json.get("error").asText();
        }
        return defaultCode.getDefaultMessage() + " | " + rawBody;
    }

    private CSMErrorCodes mapStatusToErrorCode(int status) {
        switch (status) {

            case 400: return CSMErrorCodes.BAD_REQUEST;
            case 401: return CSMErrorCodes.UNAUTHORIZED;
            case 403: return CSMErrorCodes.ACCESS_DENIED;
            case 404: return CSMErrorCodes.DATA_NOT_FOUND;
            case 405: return CSMErrorCodes.METHOD_NOT_ALLOWED;
            case 415: return CSMErrorCodes.UNSUPPORTED_MEDIA_TYPE;
            case 429: return CSMErrorCodes.API_RATE_LIMIT_EXCEEDED;

            case 500: return CSMErrorCodes.INTERNAL_SERVER_ERROR;
            case 502: return CSMErrorCodes.BAD_GATEWAY;
            case 503: return CSMErrorCodes.SERVICE_UNAVAILABLE;
            case 504: return CSMErrorCodes.API_GATEWAY_TIMEOUT;

            default: return CSMErrorCodes.API_SERVICE_ERROR;
        }
    }
}
