package com.revature.services;

public class AccountDAOFactory {
    private static AccountDAO accountDao;

    private AccountDAOFactory(){}

        public static AccountDAO getAccountDao(){
        if (accountDao == null)
            accountDao = new AccountDAOImpl();
        return accountDao;
        }
}
