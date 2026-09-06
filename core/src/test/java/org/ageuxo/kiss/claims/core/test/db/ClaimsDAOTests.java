package org.ageuxo.kiss.claims.core.test.db;

import org.ageuxo.kiss.claims.core.db.Database;
import org.ageuxo.kiss.claims.core.db.schemas.Claim;
import org.ageuxo.kiss.claims.core.db.table.ClaimsDAO;
import org.ageuxo.kiss.claims.core.db.table.GroupsDAO;
import org.ageuxo.kiss.claims.core.db.table.UsersDAO;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("ClaimsDao")
public class ClaimsDAOTests {

    private final TestContainer testContainer = new TestContainer();
    private final Database db = new Database(testContainer.jdbcUrl(), testContainer.username(), testContainer.password());
    private final Jdbi jdbi = db.jdbi();

    @Test
    void addClaim() {
        ClaimsDAO dao = jdbi.onDemand(ClaimsDAO.class);
        UsersDAO usersDAO = jdbi.onDemand(UsersDAO.class);
        GroupsDAO groupsDao = jdbi.onDemand(GroupsDAO.class);

        int userId = usersDAO.createUser(UUID.randomUUID(), "ClaimUser");
        int groupId = groupsDao.createGroup("ClaimGroup", userId);

        int returnedId = dao.createClaim(groupId, 0, 0, userId);
        assertTrue(
                returnedId >= 0,
                "Returned ID not greater than zero."
        );

        Optional<Claim> claimOptional = dao.findClaim(returnedId);
        assertTrue(
                claimOptional.isPresent(),
                "Could not find claim by ID"
        );

        claimMatches(claimOptional.get(), returnedId, userId, groupId);
    }

    private static void claimMatches(Claim claim, int expectedId, int expectedUser, int expectedGroup) {
        assertEquals(expectedId, claim.id());
        assertEquals(expectedGroup, claim.groupId());
        assertEquals(expectedUser, claim.createdById());
    }

}
