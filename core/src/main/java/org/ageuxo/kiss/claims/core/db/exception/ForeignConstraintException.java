package org.ageuxo.kiss.claims.core.db.exception;

import java.sql.SQLException;

public class ForeignConstraintException extends IntegrityException {

    public ForeignConstraintException(SQLException cause) {
        super(cause);
    }

    public String key() {
        String message = sqlException().getMessage();

        if (message == null) {
            return null;
        }

        String marker = "for key '";
        int start = message.indexOf(marker);

        if (start < 0) {
            return null;
        }

        start += marker.length();

        int end = message.indexOf('\'', start);

        if (end < 0) {
            return null;
        }

        return message.substring(start, end);

    }
}
