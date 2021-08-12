package com.revature.services;

import com.revature.customer.Customer;

import java.sql.SQLException;
import java.util.List;


public interface AccountDAO {
   Account getAccountById(int accountNumber, int customerid) throws SQLException;
    Account viewBalance(int accountNumber) throws SQLException;
    void withdraw(int accountId, double amount, double balance) throws SQLException;
    void deposit(int accountId, double amount) throws SQLException;
    Account getInfoForTransfer(Account account) throws SQLException;
    void transfer(Account account, double amount) throws SQLException;
    List<Account> getPendingTransfers(Customer customer) throws SQLException;
    void updateTransfer() throws SQLException;
    void acceptTransfer(Account account) throws SQLException;
}
