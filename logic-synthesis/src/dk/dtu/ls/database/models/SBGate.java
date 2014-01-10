package dk.dtu.ls.database.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "sb_gates")
public class SBGate {

    @DatabaseField(generatedId = true)
    private int id;
    
    // for QueryBuilder to be able to find the fields
    public static final String NAME_FIELD_NAME = "name";
    public static final String MODEL_SBML_FILE_FIELD_NAME = "model_sbml_file";
    public static final String MODEL_SBML_CONTENT_FIELD_NAME = "model_sbml_content";

    @DatabaseField(columnName = NAME_FIELD_NAME, canBeNull = false)
    private String name;

    @DatabaseField(columnName = MODEL_SBML_FILE_FIELD_NAME)
    private String modelSBMLFile;
    
    @DatabaseField(columnName = MODEL_SBML_CONTENT_FIELD_NAME)
    private String modelSBMLContents;

    SBGate() {
        // all persisted classes must define a no-arg constructor with at least
        // package visibility
    }

    public SBGate(String name) {
        this.name = name;
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
    
    public String getModelSBMLFile() {
        return modelSBMLFile;
    }

    public void setModelSBMLFile(String modelSBMLFile) {
        this.modelSBMLFile = modelSBMLFile;
    }
    
    public String getModelSBMLContents() {
        return modelSBMLContents;
    }

    public void setModelSBMLContents(String modelSBMLContents) {
        this.modelSBMLContents = modelSBMLContents;
    }

    @Override
    public int hashCode() {
        return id + name.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        return id == ((SBGate) other).id;
    }
}
