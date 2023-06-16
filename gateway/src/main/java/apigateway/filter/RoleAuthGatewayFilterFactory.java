package apigateway.filter;

import apigateway.util.JwtTokenUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RoleAuthGatewayFilterFactory extends AbstractGatewayFilterFactory<RoleAuthGatewayFilterFactory.Config> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleAuthGatewayFilterFactory.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public RoleAuthGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            var request = exchange.getRequest();
            LOGGER.info("Inside Gateway Filter");
            try {
                if (!jwtTokenUtil.hasRole(request, config.getRole())) {
                    // If the role is not available in the token, return an unauthorized response
                    var response = exchange.getResponse();
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
            } catch (Exception e) {
                LOGGER.info("Authorization failed");
                var response = exchange.getResponse();
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }

            // If the role is available, continue to the next filter in the chain
            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config {
        private String role;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        // This method defines the order of fields in the application.yml shortcut configuration
        return Arrays.asList("role");
    }
}
