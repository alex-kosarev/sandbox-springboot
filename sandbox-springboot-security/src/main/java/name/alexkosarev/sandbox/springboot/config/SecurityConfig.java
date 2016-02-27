package name.alexkosarev.sandbox.springboot.config;

import name.alexkosarev.sandbox.springboot.security.TokenAuthenticationEntryPoint;
import name.alexkosarev.sandbox.springboot.security.TokenAuthenticationFilter;
import name.alexkosarev.sandbox.springboot.security.TokenAuthenticationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${application.tokenAuthentication.header}")
    private String header;

    @Value("${application.tokenAuthentication.ignoreFault}")
    private boolean ignoreFault;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER").and()
                .withUser("admin").password("adminPassword").roles("USER", "ADMIN").and()
                .and()
                .authenticationProvider(tokenAuthenticationProvider(auth.getDefaultUserDetailsService()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new TokenAuthenticationFilter(authenticationManager(), tokenAuthenticationEntryPoint(), header, ignoreFault), BasicAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable();
    }

    @Bean
    public AuthenticationEntryPoint tokenAuthenticationEntryPoint() {
        return new TokenAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationProvider tokenAuthenticationProvider(UserDetailsService userDetailsService) {
        return new TokenAuthenticationProvider(userDetailsService);
    }
}
