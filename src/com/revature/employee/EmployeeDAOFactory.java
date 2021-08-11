package com.revature.employee;

public class EmployeeDAOFactory {
    private static EmployeeDAO employeeDao;

    private EmployeeDAOFactory() {
    }

    public static EmployeeDAO getEmployeeDao() {
        if (employeeDao == null)
            employeeDao = new EmployeeDAOImpl();
        return employeeDao;
    }
}


