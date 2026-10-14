package org.ageuxo.kiss.claims.core.db.table;

import org.ageuxo.kiss.claims.core.Result;
import org.ageuxo.kiss.claims.core.db.exception.DuplicateKeyException;
import org.ageuxo.kiss.claims.core.db.schemas.Level;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface LevelsDAO {

    @SqlQuery("SELECT * FROM `levels` WHERE id = :id")
    Optional<Level> findLevel(@Bind("id") int id);

    @SqlQuery("SELECT * FROM `levels` WHERE name = :name")
    Optional<Level> findLevel(@Bind("name") String name);

    @SqlQuery("SELECT * FROM `levels`")
    List<Level> list();

    @SqlUpdate("INSERT INTO `levels` (name) VALUES (:name)")
    @GetGeneratedKeys("id")
    int createLevel(@Bind("name") String name) throws SQLException;

    @SqlUpdate("DELETE FROM `level` WHERE id = :id")
    boolean deleteLevel(@Bind("id") int id) throws SQLException;

    default Result<Level> makeLevel(String name) {
        try {
            int levelId = createLevel(name);
            Optional<Level> level = findLevel(levelId);
            if (level.isPresent()) {
                return Result.success(level.get());
            }
        } catch (DuplicateKeyException e) {
            return Result.informative(e, "Duplicate level name.");
        } catch (SQLException e) {
            return Result.error(e);
        }

        throw new Error("An unexpected error occurred while creating level.");
    }

}
