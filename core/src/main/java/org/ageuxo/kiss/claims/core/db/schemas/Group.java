package org.ageuxo.kiss.claims.core.db.schemas;

import java.sql.Timestamp;

public record Group(int id, String name, int ownerId, int creatorId, Timestamp createdAt, Timestamp modifiedAt) {
    public Group(int id, String name, User owner, User creator, Timestamp createdAt, Timestamp modifiedAt) {
        this(id, name, owner.id(), creator.id(), createdAt, modifiedAt);
    }

}
