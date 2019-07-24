package com.example.demo.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.boot.actuate.health.HealthEndpoint
import org.springframework.boot.actuate.info.InfoEndpoint
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.AndRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import javax.servlet.http.HttpServletRequest

@Configuration
@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(web: WebSecurity) {
        web
            .ignoring()
            .requestMatchers(RequestMatcher { request: HttpServletRequest ->
                8080 == request.localPort
            })
    }

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .requestMatchers(AndRequestMatcher(RequestMatcher { request: HttpServletRequest ->
                8081 == request.localPort
            }, AntPathRequestMatcher("/monitor")))
            .hasRole("USER")
            .requestMatchers(EndpointRequest.to(HealthEndpoint::class.java, InfoEndpoint::class.java))
            .hasRole("ADMIN")
            .requestMatchers(EndpointRequest.toAnyEndpoint())
            .hasRole("SUPER_ADMIN")

        super.configure(http)
    }

    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        auth.inMemoryAuthentication()
            .withUser("user")
            .password("{noop}pass")
            // .roles("ADMIN", "USER")
            .roles("USER")
    }
}