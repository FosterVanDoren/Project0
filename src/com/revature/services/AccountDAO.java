package com.revature.services;



import java.sql.SQLException;


public interface AccountDAO {
   Account getAccountById(int accountNumber, int customerid) throws SQLException;
    Account viewBalance(int accountNumber) throws SQLException;
    void withdraw(int accountId, double amount, double balance) throws SQLException;
    void deposit(int accountId, double amount) throws SQLException;
    Account getInfoForTransfer(Account account) throws SQLException;
    void transfer(Account firstAccount, Account secondAccount) throws SQLException;
    void acceptTransfer(Account account);
}
