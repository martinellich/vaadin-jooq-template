package ch.martinelli.vj.security;

import ch.martinelli.vj.db.tables.records.UserRecord;
import ch.martinelli.vj.domain.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user present with username: " + username));
        return new User(user.getUsername(), user.getHashedPassword(), getAuthorities(user));
    }

    private List<SimpleGrantedAuthority> getAuthorities(UserRecord user) {
        return userRepository.findRolesByUsername(user.getUsername())
                .stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole())).toList();
    }

}
