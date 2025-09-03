package ch.martinelli.vj.core.ui;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

class LoginViewTest extends KaribuTest {

	@BeforeEach
	void setUp() {
		logout(); // Ensure we start logged out
        UI.getCurrent().navigate(LoginView.class);
	}

	@Test
	void login_view_has_username_and_password_fields() {
		var usernameField = _get(TextField.class, spec -> spec.withLabel("Username"));
		var passwordField = _get(PasswordField.class, spec -> spec.withLabel("Password"));
		
		assertThat(usernameField).isNotNull();
		assertThat(passwordField).isNotNull();
	}

}