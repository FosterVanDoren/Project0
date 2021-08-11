package com.revature.customer;

import com.revature.users.User;

public class Customer extends User {
    protected int custID;
    protected String firstName;
    protected String lastName;
    protected String custUsername;
    protected String custPassword;

    public Customer(){

    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
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

    public String getCustusername() {
        return custUsername;
    }

    public void setCustUsername(String custUsername) {
        this.custUsername = custUsername;
    }

    public String getCustPassword() {
        return custPassword;
    }

    public void setCustPassword(String custPassword) {
        this.custPassword = custPassword;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custID=" + custID +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", custUsername='" + custUsername + '\'' +
                ", custPassword='" + custPassword + '\'' +
                '}';
    }
}
