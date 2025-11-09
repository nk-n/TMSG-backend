package ku.cs.tmsg.service;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class LineAuthenticationToken extends AbstractAuthenticationToken {
    private final String idToken;

    public LineAuthenticationToken(String idToken) {
        super(null);
        this.idToken = idToken;
        setAuthenticated(false);
    }

    @Override
    public Object getCredentials() { return null; }

    @Override
    public Object getPrincipal() { return idToken; }
}
