package com.revature.users;

import java.sql.SQLException;

public interface UserDAO {
    User login(User user) throws SQLException;
//    boolean userExists(String email) throws SQLException;

}
