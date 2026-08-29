package org.ageuxo.kiss.claims.core.db;

import org.ageuxo.kiss.claims.core.config.CoreConfig;
import org.ageuxo.kiss.claims.core.db.mapper.UserMapper;
import org.jdbi.v3.core.HandleCallback;
import org.jdbi.v3.core.HandleConsumer;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.statement.Slf4JSqlLogger;
import org.jdbi.v3.mysql.MysqlPlugin;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.slf4j.event.Level;

public class Database {

    private final Jdbi jdbi;

    public Database(CoreConfig.DatabaseConfig config) {
        this("jdbc:mariadb://" + config.url(), config.username(), config.password());
    }

    public Database(String jdbcUrl, String username, String password) {
        this.jdbi = Jdbi.create(jdbcUrl, username, password)
                .installPlugin(new MysqlPlugin())
                .installPlugin(new SqlObjectPlugin())
                .registerRowMapper(new UserMapper())
                .setSqlLogger(new Slf4JSqlLogger());
    }

    public <T, X extends Exception> T withHandle(HandleCallback<T, X> consumer) throws X {
        return jdbi.withHandle(consumer);
    }

    public <X extends Exception> void useHandle(HandleConsumer<X> consumer) throws X {
        jdbi.useHandle(consumer);
    }

    public Jdbi jdbi() {
        return this.jdbi;
    }

}
