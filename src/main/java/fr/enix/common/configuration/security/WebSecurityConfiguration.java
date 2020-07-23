package fr.enix.common.configuration.security;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.UUID;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final AdminServerProperties adminServerProperties;
    private final int tokenValidityInSeconds = 3600;
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {


        httpSecurity.authorizeRequests()
                        .antMatchers(this.adminServerProperties.getContextPath() + "/assets/**").permitAll()
                        .antMatchers(this.adminServerProperties.getContextPath() + "/login").permitAll()
                        .anyRequest()
                        .authenticated()
                    .and()
                    .formLogin()
                        .loginPage      (this.adminServerProperties.getContextPath() + "/login")
                        .successHandler (getSavedRequestAwareAuthenticationSuccessHandler())
                    .and()
                    .logout()
                        .logoutUrl(this.adminServerProperties.getContextPath() + "/logout")
                    .and()
                    .httpBasic()
                    .and()
                    .csrf()
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .ignoringRequestMatchers(
                                new AntPathRequestMatcher(
                                    this.adminServerProperties.getContextPath() + "/instances", HttpMethod.POST.toString()
                                ),
                                new AntPathRequestMatcher(
                                    this.adminServerProperties.getContextPath() + "/v1/**", HttpMethod.POST.toString()
                                ),
                                new AntPathRequestMatcher(
                                    this.adminServerProperties.getContextPath() + "/instances/*", HttpMethod.DELETE.toString()
                                ),
                                new AntPathRequestMatcher(
                                    this.adminServerProperties.getContextPath() + "/actuator/**"
                                )
                        )
                    .and()
                    .rememberMe()
                        .key                    (UUID.randomUUID().toString())
                        .tokenValiditySeconds   (tokenValidityInSeconds);
    }

    private SavedRequestAwareAuthenticationSuccessHandler getSavedRequestAwareAuthenticationSuccessHandler() {
        SavedRequestAwareAuthenticationSuccessHandler savedRequestAwareAuthenticationSuccessHandler =
            new SavedRequestAwareAuthenticationSuccessHandler();
        savedRequestAwareAuthenticationSuccessHandler.setTargetUrlParameter("redirectTo");
        savedRequestAwareAuthenticationSuccessHandler.setDefaultTargetUrl  (this.adminServerProperties.getContextPath() + "/" );

        return  savedRequestAwareAuthenticationSuccessHandler;
    }
}
