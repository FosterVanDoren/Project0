package com.revature.customer;

import com.revature.services.Account;
import com.revature.users.User;
import com.revature.util.ConnectionFactory;

import java.sql.*;

public class CustomerDAOImpl implements CustomerDAO {

    Connection conn = null;

    public CustomerDAOImpl(){
        try {
            this.conn = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void createNewAccount(Account account) throws SQLException {
        String sql = "insert into accounts (account_type, balance, cust_id) values (?, ?, ?)";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, account.getAccountType());
        preparedStatement.setDouble(2, account.getBalance());
        preparedStatement.setInt(3, account.getOwnerID());
        int count = preparedStatement.executeUpdate();
        if(count > 0)
            System.out.println("Account created successfully.");
        else
            System.out.println("Something went wrong, please try again.");
    }

    @Override
    public Customer getCustomerInfo(User user) throws SQLException {
        String email = user.getUsername();
        Customer customer = new Customer();
        String sql = "select * from customers where cust_username = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, user.getUsername());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            int id = resultSet.getInt(1);
            String firstName = resultSet.getString(2);
            String lastName = resultSet.getString(3);
            String custUsername = resultSet.getString(4);
            String password = resultSet.getString(5);
            customer.setCustID(id);
            customer.setFirstName(firstName);
            customer.setLastName(lastName);
            customer.setCustUsername(custUsername);
            customer.setCustPassword(password);
        }
        return customer;
    }


}
