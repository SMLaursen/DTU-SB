package dk.dtu.ls;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTable;
import com.j256.ormlite.table.TableUtils;

public class Main {
    
    private final static String DATABASE_URL = "jdbc:sqlite:test.db";
    
    private Dao<Account, Integer> accountDao;

    public static void main(String[] args) throws Exception {

        new Main().doMain(args);
    }

    private void doMain(String[] args) throws Exception {
        ConnectionSource connectionSource = null;
        try {
            // create our data-source for the database
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
            // setup our database and DAOs
            setupDatabase(connectionSource);
            
            accountDao.create(new Account("test"));
           
            System.out.println("\n\nIt seems to have worked\n\n");
        } finally {
            // destroy the data source which should close underlying connections
            if (connectionSource != null) {
                connectionSource.close();
            }
        }
    }

    /**
     * Setup our database and DAOs
     */
    private void setupDatabase(ConnectionSource connectionSource)
            throws Exception {
        
        accountDao = DaoManager.createDao(connectionSource, Account.class);

        TableUtils.createTableIfNotExists(connectionSource, Account.class);
    }
}

@DatabaseTable(tableName = "accounts")
class Account {

        // for QueryBuilder to be able to find the fields
        public static final String NAME_FIELD_NAME = "name";
        public static final String PASSWORD_FIELD_NAME = "passwd";

        @DatabaseField(generatedId = true)
        private int id;

        @DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
        private String name;

        @DatabaseField(columnName = PASSWORD_FIELD_NAME)
        private String password;

        Account() {
                // all persisted classes must define a no-arg constructor with at least package visibility
        }

        public Account(String name) {
                this.name = name;
        }

        public Account(String name, String password) {
                this.name = name;
                this.password = password;
        }

        public int getId() {
                return id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        @Override
        public int hashCode() {
                return name.hashCode();
        }

        @Override
        public boolean equals(Object other) {
                if (other == null || other.getClass() != getClass()) {
                        return false;
                }
                return name.equals(((Account) other).name);
        }
}
