package org.ageuxo.kiss.claims.core.test.db;

import org.ageuxo.kiss.claims.core.db.Database;
import org.ageuxo.kiss.claims.core.db.schemas.User;
import org.ageuxo.kiss.claims.core.db.table.UsersDAO;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("UserDao")
public class UsersDAOTests {

    private final TestContainer testContainer = new TestContainer();
    private final Database db = new Database(testContainer.jdbcUrl(), testContainer.username(), testContainer.password());
    private final Jdbi jdbi = db.jdbi();

    @Test
    void addUser() {
        UsersDAO dao = jdbi.onDemand(UsersDAO.class);

        UUID testUuid = UUID.randomUUID();
        String testName = "TestName";
        int returnedId = dao.createUser(testUuid, testName);
        LoggerFactory.getLogger(this.getClass()).debug("Returned id: {}", returnedId);
        assertTrue(
                returnedId >= 0,
                "Returned ID not greater than zero."
        );

        // Get user from table by id
        Optional<User> userOptional = dao.findUser(returnedId);
        assertTrue(
                userOptional.isPresent(),
                "Could not find user by ID."
        );
        userMatches(userOptional.get(), returnedId, testName, testUuid);
    }

    @Test
    void nameChange() {
        UsersDAO dao = jdbi.onDemand(UsersDAO.class);

        UUID uuid = UUID.randomUUID();
        String nameA = "NameA";
        String nameB = "NameB";

        // Create user with name "NameA"
        int returnedId = dao.createUser(uuid, nameA);

        // Rename user to "NameB"
        assertTrue(
                dao.changeName(returnedId, nameB),
                "Name change failed"
        );

        // Get user from table by id
        Optional<User> userOptional = dao.findUser(returnedId);
        assertTrue(
                userOptional.isPresent(),
                "Could not find user by ID."
        );

        userMatches(userOptional.get(), returnedId, nameB, uuid);
    }

    /// Assert user fields match expected values.
    private static void userMatches(User user, int expectedId, String expectedName, UUID expectedUUID) {
        assertEquals(expectedId, user.id(), "Ids are not equal!");
        assertEquals(expectedName, user.name(), "Names are not equal!");
        assertEquals(expectedUUID, user.uuid(), "UUIDs are not equal!");
    }

    @AfterAll
    void cleanUp() {
        testContainer.close();
    }
}
