package be.better.at.processing.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * {@link AuditorAware} implementation that takes the user from Spring security
 * user details. Will default it to SYSTEM, if no user details found in context.
 *
 */
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    public static final String SYSTEM_USER = "SYSTEM";

    public Optional<String> getCurrentAuditor() {
        return Optional.of(SYSTEM_USER);
    }

}

