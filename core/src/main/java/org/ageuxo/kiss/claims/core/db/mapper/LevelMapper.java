package org.ageuxo.kiss.claims.core.db.mapper;

import org.ageuxo.kiss.claims.core.db.schemas.Level;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LevelMapper implements RowMapper<Level> {

    @Override
    public Level map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Level(rs.getInt("id"), rs.getString("name"));
    }
}
