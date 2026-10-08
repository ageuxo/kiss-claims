package org.ageuxo.kiss.claims.core.test.db;

import org.ageuxo.kiss.claims.core.Result;
import org.ageuxo.kiss.claims.core.db.Database;
import org.ageuxo.kiss.claims.core.db.schemas.Claim;
import org.ageuxo.kiss.claims.core.db.schemas.Group;
import org.ageuxo.kiss.claims.core.db.schemas.User;
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

        User user = UsersDAOTests.makeTestUser(usersDAO, UUID.randomUUID(), "ClaimUser");
        Group group = GroupsDAOTests.makeTestGroup(groupsDao, "ClaimGroup", user.id());

        makeTestClaim(dao, group.id(), 0, 0, 0, user.id());
    }

    public static Claim makeTestClaim(ClaimsDAO dao, int groupId, int x, int y, int z, int claimedById) throws AssertionError {
        Result<Claim> claimResult = dao.makeClaim(groupId, x, y, z, claimedById);

        if (claimResult instanceof Result.Success<Claim>(Claim result)) {
            assertClaimMatches(result, groupId, x, y, z, claimedById);
            return result;
        }

        Optional<Throwable> cause = claimResult.getCause();
        throw new AssertionError("Failed making new claim.", cause.orElse(null));
    }

    private static void assertClaimMatches(Claim claim, int expectedGroup, int expectedX, int expectedY, int expectedZ, int expectedCreator) {
        assertEquals(expectedGroup, claim.groupId());
        assertEquals(expectedX, claim.chunkX());
        assertEquals(expectedY, claim.chunkY());
        assertEquals(expectedZ, claim.chunkZ());
        assertEquals(expectedCreator, claim.createdById());
    }

}
