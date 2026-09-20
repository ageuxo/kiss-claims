package org.ageuxo.kiss.claims.core.test.db;

import org.ageuxo.kiss.claims.core.Result;
import org.ageuxo.kiss.claims.core.db.Database;
import org.ageuxo.kiss.claims.core.db.schemas.Group;
import org.ageuxo.kiss.claims.core.db.schemas.User;
import org.ageuxo.kiss.claims.core.db.table.GroupsDAO;
import org.ageuxo.kiss.claims.core.db.table.UsersDAO;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GroupDao")
public class GroupsDAOTests {

    private final TestContainer testContainer = new TestContainer();
    private final Database db = new Database(testContainer.jdbcUrl(), testContainer.username(), testContainer.password());
    private final Jdbi jdbi = db.jdbi();

    @Test
    void addGroup() {
        GroupsDAO dao = jdbi.onDemand(GroupsDAO.class);
        UsersDAO usersDAO = jdbi.onDemand(UsersDAO.class);

        User user = UsersDAOTests.makeTestUser(usersDAO, UUID.randomUUID(), "TestUser");

        makeTestGroup(
                dao,
                "TestGroup",
                user.id()
        );
    }

    @Test
    void nameChange() {
        GroupsDAO dao = jdbi.onDemand(GroupsDAO.class);
        UsersDAO usersDAO = jdbi.onDemand(UsersDAO.class);

        String nameA = "NameA";
        String nameB = "NameB";

        var user = UsersDAOTests.makeTestUser(usersDAO, UUID.randomUUID(), "GroupUser");

        var group = makeTestGroup(dao, nameA, user.id());

        // Rename group to "NameB"
        assertTrue(
                dao.changeName(group.id(), nameB)
        );

        // Get group from table by id
        Optional<Group> groupOptional = dao.findGroup(group.id());
        assertTrue(
                groupOptional.isPresent(),
                "Could not find group by ID."
        );

        assertGroupMatches(groupOptional.get(), group.id(), nameB);
    }

    public static Group makeTestGroup(GroupsDAO dao, String name, int ownerId) {
        Result<Group> groupResult = dao.makeGroup(name, ownerId);

        if (groupResult instanceof Result.Success<Group>(Group result)) {
            assertGroupNameMatches(result, name);
            return result;
        }

        Optional<Throwable> cause = groupResult.getCause();
        throw new AssertionError("Failed making new group.", cause.orElse(null));
    }

    private static void assertGroupNameMatches(Group group, String expectedName) {
        assertEquals(expectedName, group.name(), "Names are not equal!");

    }

    /// Assert group fields match expected values.
    private static void assertGroupMatches(Group group, int expectedId, String expectedName) {
        assertEquals(expectedId, group.id(), "Ids are not equal!");
        assertEquals(expectedName, group.name(), "Names are not equal!");
    }
}
