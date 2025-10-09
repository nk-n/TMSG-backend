package ku.cs.tmsg.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class LineAuthenticationProvider implements AuthenticationProvider {

    private final LineUserDetailsService lineUserDetailsService;

    public LineAuthenticationProvider(LineUserDetailsService service) {
        this.lineUserDetailsService = service;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String lineId = authentication.getName(); // LINE user ID
        UserDetails userDetails = lineUserDetailsService.loadUserByUsername(lineId);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return LineAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
