package com.ceyloncab.authproxymgtservice.application.config;

import com.ceyloncab.authproxymgtservice.application.auth.JwtService;
import com.ceyloncab.authproxymgtservice.application.auth.filters.JwtTokenVerifyFilter;
import com.ceyloncab.authproxymgtservice.application.auth.filters.JwtUsernameAndPasswordAuthenticationFilter;
import com.ceyloncab.authproxymgtservice.application.auth.UsernamePasswordService;
import com.ceyloncab.authproxymgtservice.application.exception.RestAccessDeniedHandler;
import com.ceyloncab.authproxymgtservice.application.exception.RestAuthenticationEntryPoint;
import com.ceyloncab.authproxymgtservice.domain.boundary.ApiMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * This is the configuration class for handle access
 *
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${base-url.context}/user/login")
    private String loginBasePath;

    @Value("${non-auth-urls}")
    private String[] nonAuthUrls;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UsernamePasswordService usernamePasswordService;

    private final ApiMappingService apiMappingService;

    @Autowired
    public WebSecurityConfig(PasswordEncoder passwordEncoder, JwtService jwtService,
                             UsernamePasswordService usernamePasswordService, ApiMappingService apiMappingService) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.usernamePasswordService = usernamePasswordService;
        this.apiMappingService = apiMappingService;
    }

    /**
     * Configure all authenticated urls
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        JwtUsernameAndPasswordAuthenticationFilter jwtUsernamePasswordFilter = new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtService);
        jwtUsernamePasswordFilter.setFilterProcessesUrl(loginBasePath);

        http
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtUsernamePasswordFilter)
                .addFilterAfter(new JwtTokenVerifyFilter(jwtService,apiMappingService, nonAuthUrls), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                // allow all who are accessing
                .antMatchers(nonAuthUrls).permitAll()
                // any other request must be authenticated
                .anyRequest().authenticated()
                // Handle exceptions
                .and()
                .exceptionHandling()
                    .accessDeniedHandler(new RestAccessDeniedHandler())
                    .authenticationEntryPoint(new RestAuthenticationEntryPoint(jwtService));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(usernamePasswordService);
        return provider;
    }
}
