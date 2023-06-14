//package gateway.filter;
//import gateway.service.AppUserDetailsService;
//import gateway.util.JwtTokenUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.core.Request;
//import javax.ws.rs.core.Response;
//
//public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config>{
//
//    @Autowired
//    private RouteValidator validator;
//
//    @Autowired
//    private JwtTokenUtil jwtTokenUtil;
//
//
//    @Autowired
//    AppUserDetailsService appUserDetailsService;
//
//    public AuthenticationFilter() {
//        super(Config.class);
//    }
//
//    @Override
//    public GatewayFilter apply(AuthenticationFilter.Config config) {
//        return (exchange, chain) -> {
//
//            if (validator.isSecured.test(exchange.getRequest())) {
//
//                //header contains token or not
//                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
//                    throw new RuntimeException("missing authorization header");
//                }
//                String username = null;
//                String role = null;
//                String jwt = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
//
//                if (jwt != null && jwt.startsWith("Bearer ")) {
//                    jwt = jwt.substring(7);
//                    username = jwtTokenUtil.extractUsername(jwt);
//                }
//
//                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//
//                    UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);
//
//                    if (jwtTokenUtil.validateToken(jwt, userDetails)) {
//                        System.out.println("JWT Token is Valid.");
//                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
//                                userDetails, null, userDetails.getAuthorities());
//                        usernamePasswordAuthenticationToken
//                                .setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) exchange.getRequest()));
//                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
//                    }
//                }
//            }
//            return chain.filter(exchange);
//        };
//
//    }
//
//    public static class Config {
//        // ...
//    }
//}
