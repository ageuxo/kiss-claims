package org.ageuxo.kiss.claims.core.test.db;

import org.ageuxo.kiss.claims.core.Result;
import org.ageuxo.kiss.claims.core.db.Database;
import org.ageuxo.kiss.claims.core.db.schemas.User;
import org.ageuxo.kiss.claims.core.db.table.UsersDAO;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

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
        makeTestUser(
                jdbi.onDemand(UsersDAO.class),
                UUID.randomUUID(),
                "TestName"
        );
    }

    @Test
    void addDuplicateUsername() {
        UsersDAO dao = jdbi.onDemand(UsersDAO.class);

        String name = "Duplicate";
        // Make first user
        makeTestUser(dao, UUID.randomUUID(), name);

        // Make second user
        assertThrows(AssertionError.class,  () -> makeTestUser(dao, UUID.randomUUID(), name));

    }

    @Test
    void nameChange() {
        UsersDAO dao = jdbi.onDemand(UsersDAO.class);

        UUID uuid = UUID.randomUUID();
        String nameA = "NameA";
        String nameB = "NameB";

        // Create user with name "NameA"
        var user = makeTestUser(dao, uuid, nameA);

        // Rename user to "NameB"
        assertTrue(
                dao.changeName(user.id(), nameB),
                "Name change failed"
        );

        // Get user from table by id
        Optional<User> userOptional = dao.findUser(user.id());
        assertTrue(
                userOptional.isPresent(),
                "Could not find user by ID."
        );

        assertUserMatches(userOptional.get(), nameB, uuid);
    }

    public static User makeTestUser(UsersDAO dao, UUID uuid, String name) throws AssertionError {
        Result<User> userResult = dao.makeUser(uuid, name);

        if (userResult instanceof Result.Success<User>(User result)) {
            assertUserMatches(result, name, uuid);
            return result;
        }

        Optional<Throwable> cause = userResult.getCause();
        throw new AssertionError("Failed making new user.", cause.orElse(null));
    }

    /// Assert user fields match expected values.
    private static void assertUserMatches(User user, String expectedName, UUID expectedUUID) {
        assertEquals(expectedName, user.name(), "Names are not equal!");
        assertEquals(expectedUUID, user.uuid(), "UUIDs are not equal!");
    }

    @AfterAll
    void cleanUp() {
        testContainer.close();
    }
}
