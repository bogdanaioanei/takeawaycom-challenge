package com.takeaway.gamechallenge.config;

import com.opentable.db.postgres.embedded.FlywayPreparer;
import com.opentable.db.postgres.junit.EmbeddedPostgresRules;
import com.opentable.db.postgres.junit.PreparedDbRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class TestDbConfig {

    @Bean(initMethod = "before", destroyMethod = "after")
    public PreparedDbRule dbRule() {
        return EmbeddedPostgresRules.preparedDatabase(
                FlywayPreparer.forClasspathLocation("db/migration", "db/testmigrations"));
    }

    @Bean
    public DataSource dataSource(PreparedDbRule dbRule) {
        return dbRule.getTestDatabase();
    }
}
