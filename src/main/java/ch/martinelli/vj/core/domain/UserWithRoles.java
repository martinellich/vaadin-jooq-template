package ch.martinelli.vj.core.domain;

import ch.martinelli.vj.db.tables.records.UserRecord;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class UserWithRoles {

	private final UserRecord user;

	private Set<String> roles;

	public UserWithRoles() {
		this.user = new UserRecord();
		this.roles = new HashSet<>();
	}

	public UserWithRoles(UserRecord user, List<String> roles) {
		this.user = user;
		this.roles = new HashSet<>(roles);
	}

	public UserRecord getUser() {
		return user;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof UserWithRoles other) || user.getUsername() == null) {
			return false;
		}
		return user.getUsername().equals(other.user.getUsername());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(user.getUsername());
	}

}
