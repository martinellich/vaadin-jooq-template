package ch.martinelli.vj.user.ui;

import ch.martinelli.vj.core.domain.Role;
import ch.martinelli.vj.core.domain.UserWithRoles;
import ch.martinelli.vj.core.ui.KaribuTest;
import ch.martinelli.vj.core.ui.UserView;
import com.github.mvysny.kaributesting.v10.GridKt;
import com.github.mvysny.kaributesting.v10.pro.ConfirmDialogKt;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.textfield.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static com.github.mvysny.kaributesting.v10.LocatorJ._click;
import static com.github.mvysny.kaributesting.v10.LocatorJ._get;
import static org.assertj.core.api.Assertions.assertThat;

class UserViewTest extends KaribuTest {

	@BeforeEach
	void navigate() {
		login("admin", List.of(Role.ADMIN));
		UI.getCurrent().getPage().reload();
		UI.getCurrent().navigate(UserView.class);
	}

	@Test
	void check_grid_size() {
		var grid = _get(Grid.class);
		assertThat(GridKt._size(grid)).isEqualTo(2);
	}

	@Test
	void navigate_to_user() {
		UI.getCurrent().navigate(UserView.class, "user");

		var grid = _get(Grid.class);
		assertThat(GridKt._size(grid)).isEqualTo(2);

		@SuppressWarnings("unchecked")
		Set<UserWithRoles> selectedItems = grid.getSelectedItems();
		assertThat(selectedItems).hasSize(1)
			.first()
			.extracting(userWithRoles -> userWithRoles.getUser().getFirstName())
			.isEqualTo("John");

		var firstNameTextField = _get(TextField.class, s -> s.withLabel("First Name"));
		assertThat(firstNameTextField.getValue()).isEqualTo("John");
	}

	@Test
	void delete_person() {
		var grid = _get(Grid.class);
		assertThat(GridKt._size(grid)).isEqualTo(2);

		@SuppressWarnings("unchecked")
		Component component = GridKt._getCellComponent(grid, 0, "actions");
		assertThat(component).isInstanceOf(SvgIcon.class);
		_click((SvgIcon) component);

		ConfirmDialog confirmDialog = _get(ConfirmDialog.class);
		ConfirmDialogKt._fireConfirm(confirmDialog);

		assertThat(GridKt._size(grid)).isEqualTo(1);
	}

}