package com.revature.users;

import com.revature.util.ConnectionFactory;

import java.sql.*;
import java.sql.PreparedStatement;


public class UserDAOImpl implements UserDAO{

    Connection conn = null;

    public UserDAOImpl(){
        try {
            this.conn = ConnectionFactory.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
        the user submits an email and password and this method
        access the users table to retrieve the rest of their credentials
     */
    public User login(User user) throws SQLException {
        String sql = "select * from users where username = ? and password = ?";
        PreparedStatement preparedStatement= conn.prepareStatement(sql);
        preparedStatement.setString(1, user.getUsername());
        preparedStatement.setString(2, user.getPassword());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()){
            String username = resultSet.getString(1);
            String userPass = resultSet.getString(2);
            String firstName = resultSet.getString(3);
            String lastName = resultSet.getString(4);
            String userType = resultSet.getString(5);
            user.setUsername(username);
            user.setPassword(userPass);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUserType(userType);
        }
        return user;

    }

//    @Override
//    public boolean userExists(String email) throws SQLException {
//        String sql = " select email from users where exists(select email from users where email = " + email + ")";
//        Statement statement = conn.createStatement();
//        ResultSet resultSet = statement.executeQuery(sql);
//
//        return false;
//    }


}
