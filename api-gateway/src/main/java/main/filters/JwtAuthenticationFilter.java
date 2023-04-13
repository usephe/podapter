package main.filters;


import io.jsonwebtoken.JwtException;
import main.services.JwtService;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import java.util.logging.Logger;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    private final static Logger logger =
            Logger.getLogger(JwtAuthenticationFilter.class.getName());
    public static class Config {
    }

    private final JwtService jwtService;
    private final RouteValidator routeValidator;
	public JwtAuthenticationFilter(RouteValidator routeValidator, JwtService jwtService) {
		super(Config.class);
        this.routeValidator = routeValidator;
        this.jwtService = jwtService;
	}

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (routeValidator.isSecured.test(exchange.getRequest())) {
                String token = extractValidJwtToken(exchange);
                exchange.getRequest().mutate().header("userId", jwtService.extractUsername(token));
            }
            return chain.filter(exchange);
        };
    }

    private String extractValidJwtToken(ServerWebExchange exchange) {
        if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            logger.info("Messing authorization header");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Messing authorization header");
        }

        final String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        final String authPrefix = "Bearer ";
        logger.info("authHeader: " + authHeader);
        if (authHeader == null || !authHeader.startsWith(authPrefix)) {
            logger.info("Invalid token in the header: " + authHeader);
            throw new JwtException("Invalid token");
        }
        var token = authHeader.substring(authPrefix.length());
        if (!jwtService.isTokenValid(token)) {
            logger.info("Invalid token: " + token);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden access");
        }
        return token;
    }
}
