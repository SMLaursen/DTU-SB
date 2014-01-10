package dk.dtu.ls.database;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import dk.dtu.ls.database.models.SBGate;
import dk.dtu.sb.Util;

/**
 * 
 */
public class DAO {

    private final static String DATABASE_URL = "jdbc:sqlite:database/database.db";
    private final static String TEST_DATABASE_URL = "jdbc:sqlite::memory:";

    private ConnectionSource connectionSource = null;

    private static DAO singleton;

    public static Dao<SBGate, Integer> SBGate;

    /**
     * 
     */
    public static void setup() {
        singleton = new DAO(DATABASE_URL);
    }

    /**
     * 
     */
    public static void setupTest() {
        singleton = new DAO(TEST_DATABASE_URL);
    }

    /**
     * 
     */
    public static void close() {
        if (singleton.connectionSource != null) {
            try {
                singleton.connectionSource.close();
            } catch (SQLException e) {
                Util.log.error("Failed to close database connection.");
            }
        }
    }

    private DAO(String url) {
        try {
            connectionSource = new JdbcConnectionSource(url);

            SBGate = DaoManager.createDao(connectionSource, SBGate.class);
            TableUtils.createTableIfNotExists(connectionSource, SBGate.class);

        } catch (SQLException e) {
            Util.log.error("Failed to open database connection.");
        }
    }
}
