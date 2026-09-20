package org.ageuxo.kiss.claims.core.test.db;

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

        int returnedId = dao.createGroup("TestGroup", user.id());
        assertTrue(
                returnedId >= 0,
                "Returned ID not greater than zero."
        );

        // Get user from table by id
        Optional<Group> groupOptional = dao.findGroup(returnedId);
        assertTrue(
                groupOptional.isPresent(),
                "Could not find group by ID."
        );

    }

    @Test
    void nameChange() {
        GroupsDAO dao = jdbi.onDemand(GroupsDAO.class);
        UsersDAO usersDAO = jdbi.onDemand(UsersDAO.class);

        String nameA = "NameA";
        String nameB = "NameB";

        var user = UsersDAOTests.makeTestUser(usersDAO, UUID.randomUUID(), "GroupUser");

        int returnedId = dao.createGroup(nameA, user.id());
        assertTrue(
                returnedId >= 0,
                "Returned ID not greater than zero."
        );

        // Rename group to "NameB"
        assertTrue(
                dao.changeName(returnedId, nameB)
        );

        // Get group from table by id
        Optional<Group> groupOptional = dao.findGroup(returnedId);
        assertTrue(
                groupOptional.isPresent(),
                "Could not find group by ID."
        );

        groupMatches(groupOptional.get(), returnedId, nameB);
    }

    /// Assert group fields match expected values.
    private static void groupMatches(Group group, int expectedId, String expectedName) {
        assertEquals(expectedId, group.id(), "Ids are not equal!");
        assertEquals(expectedName, group.name(), "Names are not equal!");
    }
}
