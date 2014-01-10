package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import org.junit.Test;

import dk.dtu.ls.database.DAO;
import dk.dtu.ls.database.models.SBGate;

public class TestDatabase {

    @Test
    public void testConnection() throws SQLException {
        DAO.setupTest();
        assertEquals(0, DAO.SBGate.countOf());
    }
    
    @Test(expected = SQLException.class)    
    public void testConnectionClose() throws SQLException {
        DAO.setupTest();
        DAO.close();
        DAO.SBGate.countOf();
    }
    
    @Test
    public void testInsert() throws SQLException {
        DAO.setupTest();
        DAO.SBGate.create(new SBGate("test"));
        assertEquals(1, DAO.SBGate.countOf());
    }

}
