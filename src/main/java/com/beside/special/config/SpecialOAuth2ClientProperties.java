package com.beside.special.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Map;

@Slf4j
@Getter
@Setter
@ConstructorBinding
@ConfigurationProperties("special.oauth2.client")
public class SpecialOAuth2ClientProperties {
    private final Map<String, SpecialRegistration> registration;

    public SpecialOAuth2ClientProperties(Map<String, SpecialRegistration> registration) {
        this.registration = registration;
    }

    @Getter
    @Setter
    @ToString
    public static class SpecialRegistration {

        /**
         * Client ID for the registration.
         */
        private final String clientId;

        /**
         * Client secret of the registration.
         */
        private final String clientSecret;

        /**
         * Redirect URI. May be left blank when using a pre-defined provider.
         */
        private final String redirectUri;

        public SpecialRegistration(String clientId, String clientSecret, String redirectUri) {
            this.clientId = clientId;
            this.clientSecret = clientSecret;
            this.redirectUri = redirectUri;
        }

        public String getClientId() {
            return clientId;
        }

        public String getClientSecret() {
            return clientSecret;
        }

        public String getRedirectUri() {
            return redirectUri;
        }
    }
}
