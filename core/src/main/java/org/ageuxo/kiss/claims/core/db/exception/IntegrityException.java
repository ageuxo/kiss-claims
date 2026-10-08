package org.ageuxo.kiss.claims.core.db.exception;

import java.sql.SQLException;

public class IntegrityException extends RuntimeException {
    private final SQLException sqlException;

    public IntegrityException(SQLException cause) {
        super(cause.getMessage(), cause);
        this.sqlException = cause;
    }

    public SQLException sqlException() {
        return sqlException;
    }

    public int errorCode() {
        return sqlException.getErrorCode();
    }

    public String sqlState() {
        return sqlException.getSQLState();
    }
}
