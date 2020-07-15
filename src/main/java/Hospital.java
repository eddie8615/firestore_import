import java.util.ArrayList;
import java.util.List;

public class Hospital {
    private String name;
    private List<Department> departments;
    private String id;

    public Hospital(){

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

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public void addDepartment(Department department){
        if(departments == null)
            departments = new ArrayList<>();

        departments.add(department);
    }
}
