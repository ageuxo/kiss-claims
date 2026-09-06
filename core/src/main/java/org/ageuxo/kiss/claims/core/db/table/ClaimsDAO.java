package org.ageuxo.kiss.claims.core.db.table;

import org.ageuxo.kiss.claims.core.db.schemas.Claim;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

public interface ClaimsDAO {

    @SqlQuery("SELECT * FROM `claims` WHERE id = :id")
    Optional<Claim> findClaim(@Bind("id") int id);

    @SqlQuery("SELECT * FROM `claims`")
    List<Claim> list();

    @SqlUpdate("INSERT INTO `claims` (group_id, x, y, created_by) VALUES (:group_id, :x, :y, :created_by)")
    int createClaim(@Bind("group_id") int group_id, @Bind("x") int chunkX, @Bind("y") int chunkY, @Bind("created_by") int createdById);

}
