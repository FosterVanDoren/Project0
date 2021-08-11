package com.revature.employee;

import com.revature.customer.Customer;
import com.revature.services.Account;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDAO {
    void approveAccount(Account account) throws SQLException;
    void rejectAccount(Account account) throws SQLException;
    List<Account> viewAccounts(int customerID) throws SQLException;
}
