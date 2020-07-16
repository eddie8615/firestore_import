public class Product {
    private String deptName;
    private String lastVisited;
    private boolean isSampleProvided;
    private String interest;
    private String name;

    public Product(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(String lastVisited) {
        this.lastVisited = lastVisited;
    }

    public boolean isSampleProvided() {
        return isSampleProvided;
    }

    public void setSampleProvided(boolean sampleProvided) {
        isSampleProvided = sampleProvided;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public boolean isAllDataSet(){
        if(deptName != null && lastVisited != null && interest != null && name != null)
            return true;
        return false;
    }

    public void setDefault() {
        deptName =null;
        interest = null;
        isSampleProvided = false;
        lastVisited = null;
    }
}
