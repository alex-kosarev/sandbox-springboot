package name.alexkosarev.sandbox.springsecurity.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenAuthenticationHeaderNotFound extends AuthenticationException {

    public TokenAuthenticationHeaderNotFound(String msg, Throwable t) {
        super(msg, t);
    }

}
