package main.filters;


import main.services.JwtService;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
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
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new RuntimeException("Messing authorization header");
                }

                String token = extractJwtToken(exchange);
                jwtService.isTokenValid(token);
            }
            return chain.filter(exchange);
        };
    }

    private String extractJwtToken(ServerWebExchange exchange) {
        final String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        final String authPrefix = "Bearer ";
        if (authHeader == null || !authHeader.startsWith(authPrefix)) {
            return null;
        }
        return authHeader.substring(authPrefix.length());
    }
}
