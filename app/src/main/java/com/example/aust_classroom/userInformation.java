package com.example.aust_classroom;

public class userInformation {

    private String StudentName;
    private String StudentId;
    private String Password;
    private String Department;
    private String Year;
    private String Semester;
    private String Email;
    private String PhoneNo;
    private String name;
    private String url;

    public userInformation(){

    }
    public userInformation(String StudentName,String StudentId,String Password,String Department,String Year,String Semester,String Email,String PhoneNo,String name, String url)
    {
        this.StudentName = StudentName;
        this.StudentId = StudentId;
        this.Password = Password;
        this.Department = Department;
        this.Year = Year;
        this.Semester = Semester;
        this.Email = Email;
        this.PhoneNo = PhoneNo;
        this.name = name;
        this.url = url;
    }


    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String Department) {
        this.Department= Department;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getPassword() { return Password; }

    public void setPassword(String Password) { this.Password = Password; }


    public String getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(String PhoneNo) {
        this.PhoneNo = PhoneNo;
    }


    public String getSemester() {
        return Semester;
    }

    public void setSemester(String Semester) {
        this.Semester = Semester;
    }


    public String getStudentId() {
        return StudentId;
    }

    public void setStudentId(String StudentId) {
        this.StudentId = StudentId;
    }


    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String StudentName) {
        this.StudentName = StudentName;
    }


    public String getYear() {
        return Year;
    }

    public void setYear(String Year) {
        this.Year = Year;
    }


    public String getname() {
        return name;
    }

    public void setname(String name) {
        this.name = name;
    }

    public String geturl() {
        return url;
    }

    public void seturl(String url) {
        this.url = url;
    }
}
