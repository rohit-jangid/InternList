package com.rohfl.internslist.model;

public class Internship {
    private String roleName, companyName, stipendProvided, internshipDuration, typeOfJob,
    expireDate, companyLogoUrl = null;
    private boolean  isPartTimeAllowed, isWorkFromHome;

    public Internship(String roleName, String companyName, String stipendProvided, String internshipDuration, String typeOfJob, String expireDate, boolean isPartTimeAllowed, boolean isWorkFromHome) {
        this.roleName = roleName;
        this.companyName = companyName;
        this.stipendProvided = stipendProvided;
        this.internshipDuration = internshipDuration;
        this.typeOfJob = typeOfJob;
        this.expireDate = expireDate;
        this.isPartTimeAllowed = isPartTimeAllowed;
        this.isWorkFromHome = isWorkFromHome;
    }

    public Internship(String roleName, String companyName, String stipendProvided, String internshipDuration, String typeOfJob, String expireDate, boolean isPartTimeAllowed, boolean isWorkFromHome, String companyLogoUrl) {
        this.roleName = roleName;
        this.companyName = companyName;
        this.stipendProvided = stipendProvided;
        this.internshipDuration = internshipDuration;
        this.typeOfJob = typeOfJob;
        this.expireDate = expireDate;
        this.isPartTimeAllowed = isPartTimeAllowed;
        this.isWorkFromHome = isWorkFromHome;
        this.companyLogoUrl = companyLogoUrl;
    }

    public String getCompanyLogoUrl() {
        return companyLogoUrl;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getStipendProvided() {
        return stipendProvided;
    }

    public String getInternshipDuration() {
        return internshipDuration;
    }

    public String getTypeOfJob() {
        return typeOfJob;
    }

    public String getExpireDate() {
        return expireDate;
    }

    public boolean isPartTimeAllowed() {
        return isPartTimeAllowed;
    }

    public boolean isWorkFromHome() {
        return isWorkFromHome;
    }
}
