import java.util.ArrayList;
import java.util.List;

public class Department {
    private String name;
    private String location;
    private String id;
    private String category;
    private String nurse;
    private String contact;
    private List<Product> codedProducts;

    public Department(){

    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public List<Product> getCodedProducts() {
        return codedProducts;
    }

    public void setCodedProducts(List<Product> codedProducts) {
        this.codedProducts = codedProducts;
    }

    public void addAllProducts(List<Product> products){
        if(codedProducts == null)
            codedProducts = new ArrayList<>();
        codedProducts.addAll(products);
    }

    public void addProduct(Product product){
        if(codedProducts == null)
            codedProducts = new ArrayList<>();
        codedProducts.add(product);
    }

    public boolean isAllDataSet(){
        if (name != null && location != null && id != null && category != null && nurse != null && codedProducts != null)
            return true;
        return false;
    }

    public String toString(){
        return "Name: " + name + "Location: " + location + "category: " + category + "Nurse: " + nurse + "Product num: " + String.valueOf(codedProducts.size());
    }
}
