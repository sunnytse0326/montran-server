package com.montran.server.security.filter;

import com.montran.server.model.MontranJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTAuthFilter extends OncePerRequestFilter {
    
	private final MontranJWT config;
	
	public JWTAuthFilter(MontranJWT config) {
		this.config = config;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		// Get Auth header
		String header = request.getHeader(config.getHeader());
		
		// Check header prefix
		if(header == null || !header.startsWith(config.getPrefix())) {
			chain.doFilter(request, response);
			return;
		}

		String token = header.replace(config.getPrefix(), "");
		try {
			Claims claims = Jwts.parser().setSigningKey(config.getSecret().getBytes()).parseClaimsJws(token).getBody();

			String username = claims.getSubject();

			if(username != null) {
				List<String> authorities = (List<String>) claims.get("authorities");
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
								 username, null, authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
				 SecurityContextHolder.getContext().setAuthentication(auth);
			}
		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}
		chain.doFilter(request, response);
	}

}