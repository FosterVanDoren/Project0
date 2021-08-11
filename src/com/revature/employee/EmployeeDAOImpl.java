package com.revature.employee;


import com.revature.services.Account;
import com.revature.util.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeDAOImpl implements EmployeeDAO{
    Connection conn = null;

    public EmployeeDAOImpl() {
        try {
            this.conn = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void approveAccount(Account account) throws SQLException {
        String sql = "update accounts set active_status = 1 where account_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, account.getAccountNumber());

        int count = preparedStatement.executeUpdate();
        if (count > 0)
            System.out.println("Account status set to approved.");
        else
            System.out.println("Something went wrong, please try again.");

    }

    @Override
    public void rejectAccount(Account account) throws SQLException {
        String sql = "update accounts set active_status = 0 where account_id = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setInt(1, account.getAccountNumber());

        int count = preparedStatement.executeUpdate();
        if (count > 0)
            System.out.println("Account status set to rejected.");
        else
            System.out.println("Something went wrong, please try again.");

    }

    @Override
    public List<Account> viewAccounts(int custID) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "select * from accounts where cust_id = '" + custID +"'";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery(sql);
        while (resultSet.next()) {
            int accountId = resultSet.getInt(1);
            String type = resultSet.getString(2);
            double balance = resultSet.getDouble(3);
            int custId = resultSet.getInt(4);
            Date dateOpened = resultSet.getDate(5);
            Account account = new Account();
            account.setAccountNumber(accountId);
            account.setAccountType(type);
            account.setBalance(balance);
            account.setOwnerID(custId);
            account.setOpeningDate(dateOpened);
            accounts.add(account);
        }
        return accounts;
    }


}
