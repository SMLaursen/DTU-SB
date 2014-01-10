package dk.dtu.ls.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sb_gates")
public class SBGate {

    @DatabaseField(generatedId = true)
    private int id;
    
    // for QueryBuilder to be able to find the fields
    public static final String NAME_FIELD_NAME = "name";
    public static final String PASSWORD_FIELD_NAME = "passwd";

    @DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
    private String name;

    @DatabaseField(columnName = PASSWORD_FIELD_NAME)
    private String password;

    SBGate() {
        // all persisted classes must define a no-arg constructor with at least
        // package visibility
    }

    public SBGate(String name) {
        this.name = name;
    }

    public SBGate(String name, String password) {
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
        return name.equals(((SBGate) other).name);
    }
}
