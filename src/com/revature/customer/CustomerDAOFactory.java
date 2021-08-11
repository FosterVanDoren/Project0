package com.revature.customer;

public class CustomerDAOFactory {
    private static CustomerDAO customerDao;

    private CustomerDAOFactory(){}

    public static CustomerDAO getCustomerDao() {
    if (customerDao == null)
        customerDao = new CustomerDAOImpl();
    return customerDao;
    }
}
