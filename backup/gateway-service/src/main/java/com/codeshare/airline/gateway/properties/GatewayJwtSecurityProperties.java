package com.codeshare.airline.gateway.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;


@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "gateway.security")
public class GatewayJwtSecurityProperties {

    private Auth auth = new Auth();
    private Api api = new Api();

    @Getter @Setter
    public static class Auth {
        private List<String> publicPaths = List.of();
    }

    @Getter @Setter
    public static class Api {
        private List<String> publicPaths = List.of();
    }

}
