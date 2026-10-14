package org.ageuxo.kiss.claims.core.db.schemas;

import java.sql.Timestamp;

public record Claim(int id, int groupId, int chunkX, int chunkY, int chunkZ, int createdById, Timestamp createdAt, Timestamp modifiedAt,
                    int level) {
    public Claim(int id, Group group, int chunkX, int chunkY, int chunkZ, User createdBy, Timestamp createdAt, Timestamp modifiedAt, int level) {
        this(id, group.id(), chunkX, chunkY, chunkZ, createdBy.id(), createdAt, modifiedAt, level);
    }

}
