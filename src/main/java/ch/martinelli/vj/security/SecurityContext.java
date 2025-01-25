package ch.martinelli.vj.security;

import ch.martinelli.vj.db.tables.records.UserRecord;
import ch.martinelli.vj.domain.user.UserDAO;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityContext {

	private final AuthenticationContext authenticationContext;

	private final UserDAO userDAO;

	public SecurityContext(AuthenticationContext authenticationContext, UserDAO userDAO) {
		this.userDAO = userDAO;
		this.authenticationContext = authenticationContext;
	}

	public Optional<UserRecord> getLoggedInUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			if (authentication.getPrincipal() instanceof Jwt jwt) {
				return userDAO.findById(jwt.getSubject());
			}
			else if (authentication.getPrincipal() instanceof User user) {
				return userDAO.findById(user.getUsername());
			}
		}
		return Optional.empty();
	}

	public void logout() {
		authenticationContext.logout();
	}

}
