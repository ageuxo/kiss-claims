package org.ageuxo.kiss.claims.core.db.schemas;

import java.sql.Timestamp;

public record Group(int id, String name, User owner, User creator, Timestamp createdAt, Timestamp modifiedAt) {

}
