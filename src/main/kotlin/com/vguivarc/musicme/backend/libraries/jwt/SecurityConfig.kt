package com.vguivarc.musicme.backend.libraries.jwt

import com.vguivarc.musicme.backend.data.auth.FacebookAuthenticationProvider
import com.vguivarc.musicme.backend.data.auth.EmailAuthenticationProvider
import com.vguivarc.musicme.backend.data.auth.PasswordAuthenticationProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtProperties: JwtProperties,
    private val emailAuthenticationProvider: EmailAuthenticationProvider,
    private val facebookAuthenticationProvider: FacebookAuthenticationProvider,
    private val passwordAuthenticationProvider: PasswordAuthenticationProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder
) : WebSecurityConfigurerAdapter() {

    @Bean(name = [BeanIds.AUTHENTICATION_MANAGER])
    override fun authenticationManagerBean(): AuthenticationManager {
        authenticationManagerBuilder
            .authenticationProvider(emailAuthenticationProvider)
            .authenticationProvider(facebookAuthenticationProvider)
            .authenticationProvider(passwordAuthenticationProvider)
            .eraseCredentials(false)
        return super.authenticationManagerBean()
    }

    @Bean
    fun implJwtTokenService(): JwtTokenService {
        return JwtTokenService(jwtProperties = jwtProperties)
    }

    override fun configure(http: HttpSecurity?) {
        http ?: return
        http
            .cors()
            .and()
            .antMatcher("/api/**/**")
            .authorizeRequests()
            .antMatchers(
                "/api/**/user/auth/**"
            ).permitAll()
            .antMatchers("/actuator/**").permitAll()
            .antMatchers("/api/**/utils/**").permitAll()
            .antMatchers("/api/**/i18n/**").authenticated()
            .and()
            .addFilterBefore(
                JwtTokenFilter(
                    implJwtTokenService()
                ),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .exceptionHandling()
            .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()
            .csrf()
            .disable()
            .headers()
            .frameOptions()
            .disable()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    }
}
