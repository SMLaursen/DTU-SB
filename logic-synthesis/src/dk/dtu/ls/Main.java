package dk.dtu.ls;

import dk.dtu.ls.database.DAO;

public class Main {

    public static void main(String[] args) {
        DAO.setup();
        new Main();
        DAO.close();
    }
    
    private Main() {
        
    }
    
}
