public class BeneficiaryFamilyMembers extends Person{
    private String relationship;
    private String employeeId;

    public BeneficiaryFamilyMembers() {
        super();
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
}
