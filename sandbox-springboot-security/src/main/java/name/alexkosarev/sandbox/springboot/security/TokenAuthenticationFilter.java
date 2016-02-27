package name.alexkosarev.sandbox.springboot.security;

import com.google.common.base.Strings;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import name.alexkosarev.sandbox.springboot.security.exceptions.TokenAuthenticationHeaderNotFound;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.GenericFilterBean;

public class TokenAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationManager authenticationManager;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final String header;

    private final boolean ignoreFault;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint, String header, boolean ignoreFault) {
        this.authenticationManager = authenticationManager;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.header = header;
        this.ignoreFault = ignoreFault;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        try {
            String headerValue = httpServletRequest.getHeader(header);
            if (Strings.isNullOrEmpty(headerValue)) {
                throw new TokenAuthenticationHeaderNotFound("Header " + header + " is not found.", null);
            }

            Authentication authentication = authenticationManager.authenticate(new TokenAuthentication(headerValue));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (AuthenticationException authenticationException) {
            if (!ignoreFault) {
                authenticationEntryPoint.commence(httpServletRequest, httpServletResponse, authenticationException);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }
        }
    }
}
