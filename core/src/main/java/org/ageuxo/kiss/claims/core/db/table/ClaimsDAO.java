package org.ageuxo.kiss.claims.core.db.table;

import org.ageuxo.kiss.claims.core.Result;
import org.ageuxo.kiss.claims.core.db.exception.DuplicateKeyException;
import org.ageuxo.kiss.claims.core.db.exception.ForeignConstraintException;
import org.ageuxo.kiss.claims.core.db.schemas.Claim;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ClaimsDAO {

    @SqlQuery("SELECT * FROM `claims` WHERE id = :id")
    Optional<Claim> findClaim(@Bind("id") int id);

    @SqlQuery("SELECT * FROM `claims`")
    List<Claim> list();

    @SqlUpdate("INSERT INTO `claims` (group_id, x, y, z, created_by) VALUES (:group_id, :x, :y, :z, :created_by)")
    int createClaim(@Bind("group_id") int group_id, @Bind("x") int chunkX, @Bind("y") int chunkY, @Bind("z") int chunkZ, @Bind("created_by") int createdById) throws SQLException;

    @SqlUpdate("DELETE FROM `claims` WHERE id = :id")
    boolean deleteClaim(@Bind("id") int id) throws SQLException;

    default Result<Claim> makeClaim(int groupId, int x, int y, int z, int claimedById) {
        try {
            int claimId = createClaim(groupId, x, y, z, claimedById);
            Optional<Claim> claim = findClaim(claimId);
            if (claim.isPresent()) {
                return Result.success(claim.get());
            }
        } catch (DuplicateKeyException e) {
            return Result.informative(e, "Duplicate claim data: %s".formatted(e.key()));
        } catch (ForeignConstraintException e) {
            return Result.informative(e, "Invalid foreign value: %s".formatted(e.key()));
        } catch (SQLException e) {
            return Result.error(e);
        }

        throw new Error("An unexpected error occurred while claiming.");
    }

    default Result<Boolean> removeClaim(int id) {
        try {
            return Result.success(deleteClaim(id));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
