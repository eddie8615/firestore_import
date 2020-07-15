public class Product {
    private String deptName;
    private String lastVisited;
    private boolean isSampleProvided;
    private String interest;

    public Product(){

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
}
