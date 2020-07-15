import java.util.List;

public class Department {
    private String name;
    private String location;
    private String id;
    private String category;
    private String nurse;
    private List<String> codedProducts;

    public Department(){

    }

    public String getNurse() {
        return nurse;
    }

    public void setNurse(String nurse) {
        this.nurse = nurse;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getCodedProducts() {
        return codedProducts;
    }

    public void setCodedProducts(List<String> codedProducts) {
        this.codedProducts = codedProducts;
    }
}
