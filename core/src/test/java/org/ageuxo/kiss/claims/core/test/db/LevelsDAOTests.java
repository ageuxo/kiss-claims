package org.ageuxo.kiss.claims.core.test.db;

import org.ageuxo.kiss.claims.core.Core;
import org.ageuxo.kiss.claims.core.Result;
import org.ageuxo.kiss.claims.core.db.Database;
import org.ageuxo.kiss.claims.core.db.schemas.Level;
import org.ageuxo.kiss.claims.core.db.table.LevelsDAO;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LevelsDAOTests {

    private final TestContainer testContainer = new TestContainer();
    private final Database db = new Database(testContainer.jdbcUrl(), testContainer.username(), testContainer.password());
    private final Jdbi jdbi = db.jdbi();

    @Test
    void addLevel() {
        LevelsDAO levelsDAO = jdbi.onDemand(LevelsDAO.class);

        makeTestLevel(levelsDAO, "TestAddLevel");
    }

    @Test
    void addLevels() throws SQLException {
        LevelsDAO levelsDAO = jdbi.onDemand(LevelsDAO.class);

        Core.LOGGER.error(levelsDAO.list().toString());

        int[] levels = new int[4];
        for (int i = 0; i < levels.length; i++) {
            levels[i] = levelsDAO.createLevel("level" + i);
        }

        Core.LOGGER.error(Arrays.toString(levels));
        Core.LOGGER.error(levelsDAO.list().toString());
    }

    public static Level makeTestLevel(LevelsDAO dao, String name) {
        Result<Level> levelResult = dao.makeLevel(name);

        if (levelResult instanceof Result.Success<Level>(Level result)) {
            assertEquals(name, result.name());
            return result;
        }

        Optional<Throwable> cause = levelResult.getCause();
        throw new AssertionError("Failed making new level.", cause.orElse(null));
    }

}
