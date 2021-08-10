package com.vguivarc.musicme.backend.libraries.jwt

import com.vguivarc.musicme.backend.libraries.jwt.JwtConstants.AUTHENTICATION_PREFIX
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtTokenFilter(private val jwtTokenService: JwtTokenService) : GenericFilterBean() {

    override fun doFilter(request: ServletRequest?, response: ServletResponse?, chain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        val jwt = resolveToken(httpServletRequest)
        if (StringUtils.hasText(jwt)) {
            try {
                val token = jwtTokenService.verifyToken(jwt)
                val authentication = jwtTokenService.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            } catch (invalidTokenException: InvalidTokenException) {
                this.logger.error(invalidTokenException.localizedMessage)
            }
        }
        chain?.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(JwtTokenService.AUTHORIZATION_HEADER)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(AUTHENTICATION_PREFIX)) {
            bearerToken.substring(AUTHENTICATION_PREFIX.length)
        } else {
            null
        }
    }
}
