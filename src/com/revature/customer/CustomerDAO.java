package com.revature.customer;

import com.revature.services.Account;
import com.revature.users.User;

import java.sql.SQLException;

public interface CustomerDAO {
    void createNewAccount(Account account) throws SQLException;
    Customer getCustomerInfo(User user) throws SQLException;
}
