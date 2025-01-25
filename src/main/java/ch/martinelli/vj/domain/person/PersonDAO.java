package ch.martinelli.vj.domain.person;

import ch.martinelli.oss.jooqspring.JooqDAO;
import ch.martinelli.vj.db.tables.Person;
import ch.martinelli.vj.db.tables.records.PersonRecord;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static ch.martinelli.vj.db.tables.Person.PERSON;

@Repository
public class PersonDAO extends JooqDAO<Person, PersonRecord, Long> {

	public PersonDAO(DSLContext dslContext) {
		super(dslContext, PERSON);
	}

}
