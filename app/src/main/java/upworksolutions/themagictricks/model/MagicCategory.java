package upworksolutions.themagictricks.model;

import java.util.List;

public class MagicCategory {
    private int id;
    private String name;
    private String description;
    private List<String> examples;

    // Default constructor for Gson
    public MagicCategory() {
    }

    public MagicCategory(int id, String name, String description, List<String> examples) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.examples = examples;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getExamples() {
        return examples;
    }

    public void setExamples(List<String> examples) {
        this.examples = examples;
    }
} 