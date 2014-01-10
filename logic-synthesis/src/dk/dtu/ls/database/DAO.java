package dk.dtu.ls.database;

import java.sql.SQLException;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import dk.dtu.ls.database.models.SBGate;
import dk.dtu.sb.Util;

public class DAO {
    
    private final static String DATABASE_URL = "jdbc:sqlite:database/database.db";
    
    public static Dao<SBGate, Integer> SBGate;
    
    public static void setup() {
        ConnectionSource connectionSource = null;
        try {
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
            
            SBGate = DaoManager.createDao(connectionSource, SBGate.class);
            TableUtils.createTableIfNotExists(connectionSource, SBGate.class);
           
        } catch (SQLException e) {
            Util.log.error("Failed to open connection.");
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (SQLException e) {
                    Util.log.error("Failed to close connection.");
                }
            }
        }
    }
}
