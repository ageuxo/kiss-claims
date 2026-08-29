package org.ageuxo.kiss.claims.core.db.table;

import org.ageuxo.kiss.claims.core.db.schemas.User;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindMethods;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersDAO {

    @SqlQuery("SELECT * FROM `users` WHERE id = :id")
    Optional<User> findUser(@Bind("id") int id);

    @SqlQuery("SELECT * FROM `users` WHERE uuid = :uuid")
    Optional<User> findUser(@Bind("uuid") UUID uuid);

    @SqlQuery("SELECT * FROM `users` WHERE name = :name")
    Optional<User> findUser(@Bind("name") String name);

    @SqlQuery("SELECT * FROM `users`")
    List<User> list();

    @SqlUpdate("INSERT INTO `users` (id, uuid, name) VALUES (:id, :uuid, :name)")
    boolean insertUser(@BindMethods User user);

    @SqlUpdate("INSERT INTO `users` (uuid, name) VALUES (:uuid, :name)")
    @GetGeneratedKeys("id")
    int createUser(@Bind("uuid") UUID uuid, @Bind("name") String name);

    @SqlUpdate("UPDATE `users` SET name = :name WHERE id = :id")
    boolean updateName(@BindMethods User user, @Bind("name") String name);

    /**
     * Change name of the user with <code>id</code>
     * @return true if successful
     */
    default boolean changeName(int id, String name) {
        return findUser(id)
                .filter(user -> updateName(user, name))
                .isPresent();

    }

}
