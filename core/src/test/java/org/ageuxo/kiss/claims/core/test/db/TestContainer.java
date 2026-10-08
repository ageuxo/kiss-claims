package org.ageuxo.kiss.claims.core.test.db;

import org.flywaydb.core.Flyway;
import org.testcontainers.mariadb.MariaDBContainer;

public class TestContainer implements AutoCloseable {

    private final MariaDBContainer db;

    public TestContainer() {
        this.db = new MariaDBContainer("mariadb:11");

        db.start();

        // Scans ./db/migration for migration files by default
        Flyway flyway = Flyway.configure()
                .dataSource(db.getJdbcUrl(), db.getUsername(), db.getPassword())
                .load();

        flyway.migrate();
    }

    public String jdbcUrl() {
        return db.getJdbcUrl();
    }

    public String username() {
        return db.getUsername();
    }

    public String password() {
        return db.getPassword();
    }

    @Override
    public void close() {
        db.close();
    }
}
