package org.ageuxo.kiss.claims.core.db.schemas;

import java.sql.Timestamp;

public record Claim(int id, Group group, int chunkX, int chunkY, User createdBy, Timestamp createdAt, Timestamp modifiedAt) {

}
