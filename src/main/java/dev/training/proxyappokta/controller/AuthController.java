package dev.training.proxyappokta.controller;

import dev.training.proxyappokta.model.AuthResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient auth2AuthorizedClient,
            @AuthenticationPrincipal OidcUser user,
            Model model
    ){
        log.info("user email id : {}", user.getEmail());

        AuthResponse authResponse = new AuthResponse();
        authResponse.setUserId(user.getEmail());
        authResponse.setAccessToken(auth2AuthorizedClient.getAccessToken().getTokenValue());
        authResponse.setRefreshToken(auth2AuthorizedClient.getRefreshToken().getTokenValue());
        authResponse.setExpiredAt(auth2AuthorizedClient.getAccessToken().getExpiresAt().getEpochSecond());

        List<String> authorities = user.getAuthorities().stream().map(grantedAuthority -> {
            return grantedAuthority.getAuthority();
            }).collect(Collectors.toList());

        authResponse.setAuthoritise(authorities);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }
}
