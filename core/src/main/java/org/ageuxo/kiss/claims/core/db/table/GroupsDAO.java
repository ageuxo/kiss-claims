package org.ageuxo.kiss.claims.core.db.table;

import org.ageuxo.kiss.claims.core.Result;
import org.ageuxo.kiss.claims.core.db.exception.DuplicateKeyException;
import org.ageuxo.kiss.claims.core.db.exception.ForeignConstraintException;
import org.ageuxo.kiss.claims.core.db.schemas.Group;
import org.ageuxo.kiss.claims.core.db.schemas.User;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface GroupsDAO {

    @SqlQuery("SELECT * FROM `groups` WHERE id = :id")
    Optional<Group> findGroup(@Bind("id") int id);

    @SqlQuery("SELECT * FROM `groups`")
    List<Group> list();

    @SqlUpdate("INSERT INTO `groups` (name, owner, creator) VALUES (:name, :id, :id) ")
    @GetGeneratedKeys("id")
    int createGroup(@Bind("name") String name, @BindMethods User creator) throws SQLException;

    @SqlUpdate("INSERT INTO `groups` (name, owner, creator) VALUES (:name, :id, :id) ")
    @GetGeneratedKeys("id")
    int createGroup(@Bind("name") String name, @Bind("id") int creatorId) throws SQLException;

    @SqlUpdate("UPDATE `groups` SET name = :name WHERE id = :id")
    boolean updateName(@BindMethods Group group, @Bind("name") String name);

    default boolean changeName(int id, String name) {
        return findGroup(id)
                .filter(group -> updateName(group, name))
                .isPresent();
    }

    default Result<Group> makeGroup(String name, int creatorId) {
        try {
            int groupId = createGroup(name, creatorId);
            var group = findGroup(groupId);
            if (group.isPresent()) {
                return Result.success(group.get());
            }
        } catch (DuplicateKeyException e) {
            return Result.informative(e, "Duplicate group data: %s".formatted(e.key()));
        } catch (ForeignConstraintException e) {
            return Result.informative(e, "Invalid foreign value: %s".formatted(e.key()));
        } catch (SQLException e) {
            return Result.error(e);
        }

        throw new Error("An unexpected error occurred while creating group.");
    }

}
