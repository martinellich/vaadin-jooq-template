package ch.martinelli.vj.security;

import ch.martinelli.vj.db.tables.records.UserRecord;
import ch.martinelli.vj.domain.user.UserDAO;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final AuthenticationContext authenticationContext;
    private final UserDAO userDAO;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserDAO userDAO) {
        this.userDAO = userDAO;
        this.authenticationContext = authenticationContext;
    }

    public Optional<UserRecord> get() {
        return authenticationContext.getAuthenticatedUser(Jwt.class)
                .flatMap(jwt -> userDAO.findById(jwt.getSubject()));
    }

    public void logout() {
        authenticationContext.logout();
    }

}
