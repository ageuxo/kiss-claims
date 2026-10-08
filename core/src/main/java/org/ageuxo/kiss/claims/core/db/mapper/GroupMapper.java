package org.ageuxo.kiss.claims.core.db.mapper;

import org.ageuxo.kiss.claims.core.db.schemas.Group;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupMapper implements RowMapper<Group> {
    @Override
    public Group map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Group(rs.getInt("id"), rs.getString("name"), rs.getInt("owner"), rs.getInt("creator"), rs.getTimestamp("created_at"), rs.getTimestamp("modified_at"));
    }
}
