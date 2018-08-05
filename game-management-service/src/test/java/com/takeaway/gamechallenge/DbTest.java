package com.takeaway.gamechallenge;

import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import org.jooq.DSLContext;
import org.jooq.impl.DefaultDSLContext;
import org.junit.Before;
import org.junit.Rule;

import static org.jooq.SQLDialect.POSTGRES;

public abstract class DbTest {

    @Rule
    public PreparedDbRule db = EmbeddedPostgresRules.preparedDatabase(
            FlywayPreparer.forClasspathLocation("db/migration", "db/testmigrations"));

    protected DSLContext dsl;

    @Before
    public void initJooqDsl() {
        dsl = new DefaultDSLContext(db.getTestDatabase(), POSTGRES);
    }
}
