package org.example.config.security.bruteforceprevent;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;

@Getter
@RequiredArgsConstructor
@Component
public class AuthenticationFailureListener implements
        ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final HttpServletRequest request;

    private final LoginAttemptService loginAttemptService;

    @Override
    public void onApplicationEvent(@NonNull AuthenticationFailureBadCredentialsEvent event) {
        String xfHeader = getLoginAttemptService().retrieveXForwardedForPropertyFromHeader(getRequest());
        String remoteAddress = getRequest().getRemoteAddr();
        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(remoteAddress)) {
            getLoginAttemptService().loginFailed(remoteAddress);
        } else {
            getLoginAttemptService().loginFailed(getLoginAttemptService().retrieveClientIdFromXForwardedProperty(xfHeader));
        }
    }
}
