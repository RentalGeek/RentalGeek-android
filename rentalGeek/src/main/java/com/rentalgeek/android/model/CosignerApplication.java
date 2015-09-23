package com.rentalgeek.android.model;

/**
 * Created by rajohns on 9/19/15.
 *
 */
public enum CosignerApplication {

    INSTANCE;

    private String firstName;
    private String lastName;
    private String birthMonth;
    private String birthDay;
    private String birthYear;
    private String SSN;
    private String maritalStatus;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String ownsOrRents;
    private String monthlyPayment;
    private Boolean lostCourtJudgement;
    private Boolean hadLawsuitFiled;
    private Boolean foreclosedUpon;
    private Boolean declaredBankruptcy;
    private Boolean isFelon;
    private String employerName;
    private String employmentPosition;
    private String monthlyIncome;
    private Boolean intendToCoverRent;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public void setBirthMonth(String birthMonth) {
        this.birthMonth = birthMonth;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getSSN() {
        return SSN;
    }

    public void setSSN(String SSN) {
        this.SSN = SSN;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getOwnsOrRents() {
        return ownsOrRents;
    }

    public void setOwnsOrRents(String ownsOrRents) {
        this.ownsOrRents = ownsOrRents;
    }

    public String getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(String monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public Boolean getLostCourtJudgement() {
        return lostCourtJudgement;
    }

    public void setLostCourtJudgement(Boolean lostCourtJudgement) {
        this.lostCourtJudgement = lostCourtJudgement;
    }

    public Boolean getHadLawsuitFiled() {
        return hadLawsuitFiled;
    }

    public void setHadLawsuitFiled(Boolean hadLawsuitFiled) {
        this.hadLawsuitFiled = hadLawsuitFiled;
    }

    public Boolean getForeclosedUpon() {
        return foreclosedUpon;
    }

    public void setForeclosedUpon(Boolean foreclosedUpon) {
        this.foreclosedUpon = foreclosedUpon;
    }

    public Boolean getDeclaredBankruptcy() {
        return declaredBankruptcy;
    }

    public void setDeclaredBankruptcy(Boolean declaredBankruptcy) {
        this.declaredBankruptcy = declaredBankruptcy;
    }

    public Boolean getIsFelon() {
        return isFelon;
    }

    public void setIsFelon(Boolean isFelon) {
        this.isFelon = isFelon;
    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getEmploymentPosition() {
        return employmentPosition;
    }

    public void setEmploymentPosition(String employmentPosition) {
        this.employmentPosition = employmentPosition;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public Boolean getIntendToCoverRent() {
        return intendToCoverRent;
    }

    public void setIntendToCoverRent(Boolean intendToCoverRent) {
        this.intendToCoverRent = intendToCoverRent;
    }

}
