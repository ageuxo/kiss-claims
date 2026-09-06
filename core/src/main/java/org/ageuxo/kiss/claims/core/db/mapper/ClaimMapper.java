package org.ageuxo.kiss.claims.core.db.mapper;

import org.ageuxo.kiss.claims.core.db.schemas.Claim;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ClaimMapper implements RowMapper<Claim> {

    @Override
    public Claim map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Claim(rs.getInt("id"), rs.getInt("group_id"), rs.getInt("x"), rs.getInt("y"),rs.getInt("created_by"), rs.getTimestamp("created_at"), rs.getTimestamp("modified_at"));
    }
}
