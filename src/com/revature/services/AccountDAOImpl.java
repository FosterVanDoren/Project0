package com.revature.services;


import com.revature.util.ConnectionFactory;

import java.sql.*;


public class AccountDAOImpl implements AccountDAO {
    Connection conn = null;

    public AccountDAOImpl() {
        try {
            this.conn = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //this method gets a customers account based on their id and the accounts id
    @Override
    public Account getAccountById(int accountNumber, int customerId) throws SQLException {
        Account account = new Account();
        String sql = "select * from accounts where cust_id = " + customerId + " and account_id = " + accountNumber;
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            int accountId = resultSet.getInt(1);
            String accountType = resultSet.getString(2);
            double balance = resultSet.getDouble(3);
            int custId = resultSet.getInt(4);
            Date date = resultSet.getDate(5);
            int active = resultSet.getInt(6);
            account.setAccountNumber(accountId);
            account.setAccountType(accountType);
            account.setBalance(balance);
            account.setOwnerID(custId);
            account.setOpeningDate(date);
            account.setActive(active);
        }
        return account;

    }

    //displays an accounts information based on it's id
    @Override
    public Account viewBalance(int accountNumber) throws SQLException {
        Account account = new Account();
        String sql = "call sp_view_balance(" + accountNumber + ")";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();

        if (resultSet != null) {
            int id = resultSet.getInt(1);
            String accountType = resultSet.getString(2);
            double balance = resultSet.getDouble(3);
            int custID = resultSet.getInt(4);
            Date dateOpened = resultSet.getDate(5);
            int active = resultSet.getInt(6);
            account.setAccountNumber(id);
            account.setAccountType(accountType);
            account.setBalance(balance);
            account.setOwnerID(custID);
            account.setOpeningDate(dateOpened);
            account.setActive(active);
        } else {
            System.out.println("There is no account with that ID");
        }
        return account;
    }

    //
    @Override
    public void withdraw(int accountId, double amount, double balance) throws SQLException {
        String sqlUpdate = "update accounts  set balance = balance - " + amount + " where account_id = " + accountId;
        Statement updateStatement = conn.createStatement();
        if (amount > balance) {
            System.out.println("Insufficient funds to make this withdrawal");
        } else if (amount < 0) {
            System.out.println("Something went wrong, please try again.");
        } else {
            int count = updateStatement.executeUpdate(sqlUpdate);
            if (count > 0) {
                System.out.println("Successfully withdrew $" + amount);
            }

        }

    }


    @Override
    public void deposit(int accountId, double amount) throws SQLException {
        String sqlUpdate = "update accounts  set balance = balance + " + amount + " where account_id = " + accountId;
        Statement updateStatement = conn.createStatement();

        if (amount < 0) {
            System.out.println("Something went wrong, please enter a valid amount,");
        } else {
            int count = updateStatement.executeUpdate(sqlUpdate);
            if (count > 0) {
                System.out.println("Successfully deposited $" + amount);
            } else {
                System.out.println("Something went wrong, please try again.");
            }

        }
    }

    @Override
    public Account getInfoForTransfer(Account account) throws SQLException {
        String sql = "select * from accounts where account_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, account.getAccountNumber());
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            int accountId = resultSet.getInt(1);
            String accountType = resultSet.getString(2);
            double balance = resultSet.getDouble(3);
            int custId = resultSet.getInt(4);
            Date date = resultSet.getDate(5);
            int active = resultSet.getInt(6);
            account.setAccountNumber(accountId);
            account.setAccountType(accountType);
            account.setBalance(balance);
            account.setOwnerID(custId);
            account.setOpeningDate(date);
            account.setActive(active);
        }
        return account;
    }

    @Override
    public void transfer(Account firstAccount, Account secondAccount) {

    }

    @Override
    public void acceptTransfer(Account account) {

    }
}
