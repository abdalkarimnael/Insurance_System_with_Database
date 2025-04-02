public class Insurance {
    private String insurance_num;
    private String startDate;
    private String availableDate;
    private String expireDate;
    private String departmentNumber;
    public Insurance() {
        super();
    }

    public String getInsurance_num() {
        return insurance_num;
    }

    public void setInsurance_num(String insurance_num) {
        this.insurance_num = insurance_num;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getAvailableDate() {
        return availableDate;
    }

    public void setAvailableDate(String availableDate) {
        this.availableDate = availableDate;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    public String getDepartmentNumber() {
        return departmentNumber;
    }

    public void setDepartmentNumber(String departmentNumber) {
        this.departmentNumber = departmentNumber;
    }
}
