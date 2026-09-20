package org.ageuxo.kiss.claims.core.db.exception;

import org.jdbi.v3.core.statement.SqlExceptionHandler;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.SQLException;

public class IntegrityExceptionHandler implements SqlExceptionHandler {
    @Override
    public void handle(SQLException ex, StatementContext ctx) {

        switch (ex.getErrorCode()) {
            case 1062 -> throw new DuplicateKeyException(ex);
            case 1451, 1452 -> throw new ForeignConstraintException(ex);
        }
    }
}
