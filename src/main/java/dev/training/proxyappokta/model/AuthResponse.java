package dev.training.proxyappokta.model;

import lombok.Data;

import java.util.Collection;

@Data
public class AuthResponse {
    private String userId;
    private String accessToken;
    private String refreshToken;
    private long expiredAt;
    private Collection<String> authoritise;
}
