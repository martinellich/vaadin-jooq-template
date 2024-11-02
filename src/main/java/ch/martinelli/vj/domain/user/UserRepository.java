package ch.martinelli.vj.domain.user;

import ch.martinelli.oss.jooqspring.JooqRepository;
import ch.martinelli.vj.db.tables.User;
import ch.martinelli.vj.db.tables.records.UserRecord;
import ch.martinelli.vj.db.tables.records.UserRoleRecord;
import org.jooq.DSLContext;
import org.jooq.OrderField;
import org.jooq.Record1;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static ch.martinelli.vj.db.tables.User.USER;
import static ch.martinelli.vj.db.tables.UserRole.USER_ROLE;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.select;


@Service
public class UserRepository extends JooqRepository<User, UserRecord, String> {

    public UserRepository(DSLContext dslContext) {
        super(dslContext, USER);
    }

    public Optional<UserWithRoles> findUserWithRolesByUsername(String username) {
        return dslContext.select(USER,
                        multiset(select(USER_ROLE.ROLE)
                                .from(USER_ROLE)
                                .where(USER_ROLE.USERNAME.eq(USER.USERNAME))
                        ).convertFrom(r -> r.map(Record1::value1)))
                .from(USER)
                .where(USER.USERNAME.eq(username))
                .fetchOptional(mapping(UserWithRoles::new));
    }

    public List<UserRoleRecord> findRolesByUsername(String username) {
        return dslContext.selectFrom(USER_ROLE)
                .where(USER_ROLE.USERNAME.eq(username))
                .fetch();
    }

    public List<UserWithRoles> findAllUserWithRoles(int offset, int limit, List<OrderField<?>> orderFields) {
        return dslContext.select(USER,
                        multiset(select(USER_ROLE.ROLE)
                                .from(USER_ROLE)
                                .where(USER_ROLE.USERNAME.eq(USER.USERNAME))
                        ).convertFrom(r -> r.map(Record1::value1)))
                .from(USER)
                .orderBy(orderFields)
                .offset(offset)
                .limit(limit)
                .fetch(mapping(UserWithRoles::new));
    }

    @Transactional
    public void save(UserWithRoles userWithRoles) {
        var user = userWithRoles.getUser();
        dslContext.attach(user);
        user.store();

        // First delete all assigned roles
        dslContext.deleteFrom(USER_ROLE)
                .where(USER_ROLE.USERNAME.eq(user.getUsername()))
                .execute();

        // Then add all roles
        for (var role : userWithRoles.getRoles()) {
            var userRole = new UserRoleRecord(user.getUsername(), role);
            dslContext.attach(userRole);
            userRole.store();
        }
    }

    @Transactional
    public void deleteUserAndRolesByUsername(String username) {
        dslContext.deleteFrom(USER_ROLE)
                .where(USER_ROLE.USERNAME.eq(username))
                .execute();
        dslContext.deleteFrom(USER)
                .where(USER.USERNAME.eq(username))
                .execute();
    }
}
