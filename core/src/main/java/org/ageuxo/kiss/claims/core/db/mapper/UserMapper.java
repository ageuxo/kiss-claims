package org.ageuxo.kiss.claims.core.db.mapper;

import org.ageuxo.kiss.claims.core.db.schemas.User;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UserMapper implements RowMapper<User> {
    @Override
    public User map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new User(rs.getInt("id"), rs.getObject("uuid", UUID.class), rs.getString("name"));
    }
}
