package com.revature.users;

public class UserDAOFactory {
    private static UserDAO userDao;

    private UserDAOFactory() {}

    public static UserDAO getUserDao(){
        if(userDao==null)
            userDao = new UserDAOImpl();
        return userDao;
    }
}
