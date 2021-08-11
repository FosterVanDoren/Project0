package com.revature.employee;

import com.revature.users.User;

public class Employee extends User {
    protected int empId;
    protected String empUsername;
    protected String password;
    protected String firstName;
    protected String lastName;

    public Employee() {

    }


    public int getId() {
        return empId;
    }


    public void setId(int empId) {
        this.empId = empId;
    }


    public String getEmpUsername() {
        return empUsername;
    }

    public void setEmpUsername(String empUsername) {
        this.empUsername = empUsername;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId=" + empId +
                ", empUsername='" + empUsername + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
