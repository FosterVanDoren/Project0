package com.revature.main;

import com.revature.customer.Customer;
import com.revature.customer.CustomerDAO;
import com.revature.customer.CustomerDAOFactory;
import com.revature.employee.EmployeeDAO;
import com.revature.employee.EmployeeDAOFactory;
import com.revature.services.Account;
import com.revature.services.AccountDAO;
import com.revature.services.AccountDAOFactory;
import com.revature.users.User;
import com.revature.users.UserDAO;
import com.revature.users.UserDAOFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ZeppBank {
    public static void main(String[] args) throws SQLException {
        CustomerDAO customerDAO = CustomerDAOFactory.getCustomerDao();
        AccountDAO accountDAO = AccountDAOFactory.getAccountDao();
        UserDAO userDAO = UserDAOFactory.getUserDao();
        EmployeeDAO employeeDAO = EmployeeDAOFactory.getEmployeeDao();
        boolean running = true;
        while (running) {
            /*
                Asks the user to login with their username and password then checks the database
                 to see if these credentials exist or they can type quit to terminate the program
             */
            System.out.println("Welcome to Zepp First National Bank. Please login below");
            System.out.println("Please enter your username or type quit to terminate the program.");
            Scanner scan = new Scanner(System.in);
            String username = scan.next();
            if (username.equalsIgnoreCase("quit")) {
                System.out.println("Program terminating.");
                running = false;
            } else {
                System.out.println("Please enter your password.");
                String password = scan.next();
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                /*
                    this will search the database for the credentials and then retrieve the rest of the information
                    that is tied to that specific user from the users table
                 */
              userDAO.login(user);

                System.out.println("Welcome " + user.getFirstName() + " " + user.getLastName());

                boolean loggedIn = true;
                /*
                    if the user type assigned to the user that is currently logged is "customer"
                    the menu will display a specific set of options to them. If it is "employee" then
                    the user will be shown a separate list of options to choose from that only employees
                    have access to.
                 */
                if (user.getUserType().equalsIgnoreCase("customer")) {
                    Customer customer = customerDAO.getCustomerInfo(user);
                    int customerId = customer.getCustID();
                    while (loggedIn) {
                        System.out.println("Please select an option from below");
                        System.out.println("1. View balance");
                        System.out.println("2. Create an account");
                        System.out.println("3. Withdrawal");
                        System.out.println("4. Deposit");
                        System.out.println("5. Post a transfer");
                        System.out.println("6. Accept transfer");
                        System.out.println("7. Logout");
                        int choice = scan.nextInt();
                        /*
                            the choice entered by the user compared in a switch statement
                            and different methods are executed based on the selection
                         */
                        switch (choice) {
                            case 1: {
                                //this displays an account's balance based on its id
                                System.out.println("Please enter the account id");
                                int id = scan.nextInt();
                                Account account = new Account();

                                account = accountDAO.viewBalance(id);

                                System.out.println("Account ID: " + account.getAccountNumber() + ", Account Type: " + account.getAccountType() + ", Balance : $" + account.getBalance()
                                        + ", Date Opened: " + account.getOpeningDate() + " Approved status: " + account.getActive());

                                if(account.getActive() == 0) {
                                    System.out.println("This account is approved to make transactions. Please have an employee approve this account before you " +
                                            "attempt to make any transactions with it.");
                                }
                                break;
                            }
                            case 2: {
                                //creates a new account for the customer
                                System.out.println("Please enter the type of account you would like to create: Checking or Savings,");
                                String accountType = scan.next();
                                if (accountType.equalsIgnoreCase("checking"))
                                    System.out.println("You have chosen a checking account");
                                else if (accountType.equalsIgnoreCase("savings"))
                                    System.out.println("You have chosen a savings account");
                                else {
                                    System.out.println("That is not a valid option. Please select one of the valid account types");
                                    accountType = scan.next();
                                }
                                System.out.println("Please enter the staring balance for the account");
                                double startingAmount = scan.nextDouble();
                                if (startingAmount < 0) {
                                    System.out.println("That is not a valid starting amount. Please enter a valid amount");
                                    startingAmount = scan.nextDouble();
                                }
                                System.out.println("Please enter the customer id to be associated with this account");
                                int custId = scan.nextInt();
                                Account createdAccount = new Account();
                                createdAccount.setAccountType(accountType);
                                createdAccount.setBalance(startingAmount);
                                createdAccount.setOwnerID(custId);
                                customerDAO.createNewAccount(createdAccount);
                                break;
                            }
                            case 3: {
                                //withdraws money from an account
                                System.out.println("Please enter the id of the account you would like to withdraw from");
                                int accountNumber = scan.nextInt();
                                //gets the account information based on its id and the customers id
                                Account account = accountDAO.getAccountById(accountNumber, customerId);
                                double balance = account.getBalance();
                                int active = account.getActive();
                                //checks to see if the account is active and can make transactions
                                if (active == 0) {
                                    System.out.println("This account is not approved to make transactions," +
                                            "please select an approved account");
                                } else {
                                    System.out.println("Please enter the amount of money you would like to withdraw.");
                                    double amount = scan.nextDouble();

                                    accountDAO.withdraw(accountNumber, amount, balance);
                                    System.out.println("Would you like to make another withdrawal?");
                                    String confirm = scan.next();
                                    /*
                                        if the user inputs yes, then they are given the opportunity to make another
                                        withdrawal, otherwise it will return them to the previous menu
                                     */
                                    if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                                        System.out.println("Please enter the id of the account you would like to withdraw from");
                                        accountNumber = scan.nextInt();
                                        System.out.println("Please enter the amount of money you would like to withdraw.");
                                        amount = scan.nextDouble();
                                        balance = account.getBalance();
                                        accountDAO.withdraw(accountNumber, amount, balance);

                                    } else if (confirm.equalsIgnoreCase("no") || confirm.equalsIgnoreCase("n"))
                                        System.out.println("Returning to menu...");
                                    else {
                                        System.out.println("I don't understand that, please try again.");
                                        confirm = scan.next();
                                    }
                                    break;
                                }
                            }
                            case 4: {
                                //same as the withdrawal case, except for deposits
                                System.out.println("Please enter the id of the account you would like to deposit to");
                                int accountNumber = scan.nextInt();
                                Account account = accountDAO.getAccountById(accountNumber, customerId);
                                int active = account.getActive();
                                if (active == 0) {
                                    System.out.println("This account is not approved to make transactions," +
                                            "please select an approved account");
                                } else {
                                    System.out.println("Please enter the amount of money you would like to deposit.");
                                    double amount = scan.nextDouble();

                                    accountDAO.deposit(accountNumber, amount);
                                    System.out.println("Would you like to make another deposit?");
                                    String confirm = scan.next();
                                    if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                                        System.out.println("Please enter the id of the account you would like to deposit to");
                                        accountNumber = scan.nextInt();
                                        System.out.println("Please enter the amount of money you would like to deposit.");
                                        amount = scan.nextDouble();

                                        accountDAO.deposit(accountNumber, amount);

                                    } else if (confirm.equalsIgnoreCase("no") || confirm.equalsIgnoreCase("n"))
                                        System.out.println("Returning to menu...");
                                    else {
                                        System.out.println("I don't understand that, please try again.");
                                        confirm = scan.next();
                                    }
                                }

                                break;
                            }
                            case 5: {
                                System.out.println("Please enter the account id you wish to transfer from");
                                int firstId = scan.nextInt();
                                Account firstAccount = new Account();
                                firstAccount.setAccountNumber(firstId);
                                accountDAO.getInfoForTransfer(firstAccount);
                                if (firstAccount.getActive() == 0) {
                                    System.out.println("This account is not approved to make transactions," +
                                            "please select an approved account");
                                } else {
                                    System.out.println("Please enter the account id you wish to transfer to");
                                    int secondId = scan.nextInt();
                                    Account secondAccount = new Account();
                                    secondAccount.setAccountNumber(secondId);
                                    accountDAO.getInfoForTransfer(secondAccount);
                                    if (secondAccount.getActive() == 0) {
                                        System.out.println("This account is not approved to make transactions," +
                                                "please select an approved account");
                                    }else{
                                        accountDAO.transfer(firstAccount,secondAccount);
                                    }
                                    break;
                                }
                            }
                            case 6: {
                                break;
                            }
                            //logs the user out and returns to the main menu
                            case 7: {
                                System.out.println("Goodbye.");
                                loggedIn = false;
                            }
                            default: {
                                System.out.println("That is not a valid option. Please enter a valid selection.");
                            }
                        }

                    }
                } else if (user.getUserType().equalsIgnoreCase("employee")) {
                    while (loggedIn) {
                        System.out.println("Please select an option from below");
                        System.out.println("1. View a customer's bank accounts");
                        System.out.println("2. Approve an account");
                        System.out.println("3. Reject an account");
                        System.out.println("4. View all transactions");
                        System.out.println("5. Logout");
                        int choice = scan.nextInt();

                        switch (choice) {
                            case 1:
                                System.out.println("Please enter the the id of the customer");
                                int custId = scan.nextInt();
                                List<Account> accounts = employeeDAO.viewAccounts(custId);
                                for (Account account : accounts) {
                                    System.out.println(account);
                                }
                                break;
                            case 2: {
                                Account account = new Account();
                                System.out.println("Please enter the id of the account you wish to approve");
                                int accountId = scan.nextInt();
                                account.setAccountNumber(accountId);
                                employeeDAO.approveAccount(account);
                                break;
                            }
                            case 3: {
                                Account account = new Account();
                                System.out.println("Please enter the id of the account you wish to reject");
                                int accountId = scan.nextInt();
                                account.setAccountNumber(accountId);
                                employeeDAO.rejectAccount(account);
                                break;
                            }
                            case 4:
                                break;
                            case 5: {
                                System.out.println("Goodbye.");
                                loggedIn = false;
                                break;
                            }
                            default: {
                                System.out.println("That is not one of the listed options. Please enter a valid option");
                                choice = scan.nextInt();
                            }
                        }
                    }
                }
            }
        }
    }
}


