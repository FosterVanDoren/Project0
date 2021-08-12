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
import org.apache.log4j.FileAppender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ZeppBank {

//    private static final Logger logger = LogManager.getLogger(ZeppBank.class);


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
//                logger.info("User " + user.getFirstName() + user.getLastName() + " logged in");
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
                        System.out.println("-----------------------------------");
                        System.out.println("Please select an option from below");
                        System.out.println("1. View balance");
                        System.out.println("2. Create an account");
                        System.out.println("3. Withdrawal");
                        System.out.println("4. Deposit");
                        System.out.println("5. Post a transfer");
                        System.out.println("6. Accept transfer");
                        System.out.println("7. Logout");
                        System.out.println("-----------------------------------");
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
                                        + ", Date Opened: " + account.getOpeningDate() + ", Approval status: " + account.getActive() + ", Pending Transactions: " + account.getPendingTransaction());

                                if (account.getActive().equals("Rejected")) {
                                    System.out.println("This account is not approved to make transactions. Please have an employee approve this account before you " +
                                            "attempt to make any transactions with it.");
                                }
//                                logger.info("Account " + account.getAccountNumber() + " balance viewed.");
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
//                                logger.info("New " + createdAccount.getAccountType() +" account created.");
                                break;
                            }
                            case 3: {
                                //withdraws money from an account
                                System.out.println("Please enter the id of the account you would like to withdraw from");
                                int accountNumber = scan.nextInt();
                                //gets the account information based on its id and the customers id
                                Account account = accountDAO.getAccountById(accountNumber, customerId);
                                double balance = account.getBalance();
                                String active = account.getActive();
                                //checks to see if the account is active and can make transactions
                                if (active.equals("Rejected")) {
                                    System.out.println("This account is not approved to make transactions," +
                                            " please select an approved account");
//                                    logger.info("Withdrawal on account " + accountNumber + " failed");
                                } else if (active.equals("Pending")) {
                                    System.out.println("Account is currently waiting on a pending transfer. Please wait for the transfer to complete.");
//                                    logger.info("Withdrawal on account " + accountNumber + " failed due to pending transfer");
                                } else {
                                    System.out.println("Please enter the amount of money you would like to withdraw.");
                                    double amount = scan.nextDouble();

                                    accountDAO.withdraw(accountNumber, amount, balance);
//                                    logger.info("Withdrew " + amount + " from account " + accountNumber);
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
//                                        logger.info("Withdrew " + amount + " from account " + accountNumber);

                                    } else if (confirm.equalsIgnoreCase("no") || confirm.equalsIgnoreCase("n"))
                                        System.out.println("Returning to menu...");
                                    else {
                                        System.out.println("I don't understand that, please try again.");
                                        confirm = scan.next();
                                    }
                                }
                                break;
                            }
                            case 4: {
                                //same as the withdrawal case, except for deposits
                                System.out.println("Please enter the id of the account you would like to deposit to");
                                int accountNumber = scan.nextInt();
                                Account account = accountDAO.getAccountById(accountNumber, customerId);
                                String active = account.getActive();
                                if (active.equals("Rejected")) {
                                    System.out.println("This account is not approved to make transactions," +
                                            "please select an approved account");
//                                    logger.info("Deposit on account " + accountNumber + " failed");
                                } else if (active.equals("Pending")) {
                                    System.out.println("Account is currently waiting on a pending transfer. Please wait for the transfer to complete.");
//                                    logger.info("Deposit on account " + accountNumber + " failed due to pending transfer");
                                } else {
                                    System.out.println("Please enter the amount of money you would like to deposit.");
                                    double amount = scan.nextDouble();

                                    accountDAO.deposit(accountNumber, amount);
//                                    logger.info("Deposited + " + amount + " from account " + accountNumber);
                                    System.out.println("Would you like to make another deposit?");
                                    String confirm = scan.next();
                                    if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                                        System.out.println("Please enter the id of the account you would like to deposit to");
                                        accountNumber = scan.nextInt();
                                        System.out.println("Please enter the amount of money you would like to deposit.");
                                        amount = scan.nextDouble();

                                        accountDAO.deposit(accountNumber, amount);
//                                        logger.info("Deposited + " + amount + " from account " + accountNumber);

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
                                //asks the user for an account id in order to obtain all the information about that account from the database
                                System.out.println("Please enter the account id you wish to transfer from");
                                int firstId = scan.nextInt();
                                Account firstAccount = new Account();
                                firstAccount.setAccountNumber(firstId);
                                accountDAO.getInfoForTransfer(firstAccount);
                                /*
                                    if the accounts active status is either rejected or pending, then the system will not
                                    allow the user to perform any transfer with this account.
                                 */

                                if (firstAccount.getActive().equals("Rejected")) {
                                    System.out.println("This account is not approved to make transactions," +
                                            "please select an approved account");
//                                    logger.info("Transfer request failed to post.");
                                } else if (firstAccount.getActive().equals("Pending")) {
                                    System.out.println("Account is currently waiting on a pending transfer. Please wait for the transfer to complete.");
//                                    logger.info("Transfer request failed to post due to another transfer being present.");
                                } else {
                                    System.out.println("Please enter the amount of money you would like to transfer");
                                    /*
                                        if the amount the user would like to transfer is either
                                        greater than the current balance of the account or negative,
                                        then the system will not allow the user to complete the transaction
                                     */
                                    double amount = scan.nextDouble();
                                    if (amount > firstAccount.getBalance()) {
                                        System.out.println("The account you have selected to transfer from does not have the funds to" +
                                                "complete this transfer. Please deposit more money first.");
//                                        logger.info("Transfer request failed to post due to lack of funds.");
                                    } else if (amount < 0) {
                                        System.out.println("Invalid amount. please enter a valid amount");
//                                        logger.info("Transfer request failed to post due to invalid amount.");
                                    } else {
                                        //updates the status of the account to pending to prepare it for a transfer
                                        accountDAO.transfer(firstAccount, amount);
//                                        logger.info("Transfer of $" + amount + " successfully posted for account " + firstAccount.getAccountNumber());
                                        System.out.println("Please enter the account id you wish to transfer to");
                                        //same as above, but for the second account
                                        int secondId = scan.nextInt();
                                        Account secondAccount = new Account();
                                        secondAccount.setAccountNumber(secondId);
                                        accountDAO.getInfoForTransfer(secondAccount);
                                        if (secondAccount.getActive().equals("Rejected")) {
                                            System.out.println("This account is not approved to make transactions," +
                                                    "please select an approved account");
//                                            logger.info("Transfer request failed to post.");
                                        } else if (secondAccount.getActive().equals("Pending")) {
                                            System.out.println("Account is currently waiting on a pending transfer. Please wait for the transfer to complete.");
//                                            logger.info("Transfer request failed to post due to another transfer being present.");
                                        } else {
                                            accountDAO.transfer(secondAccount, amount);
//                                            logger.info("Transfer of $" + amount + " successfully posted for account " + secondAccount.getAccountNumber());
                                        }
                                    }
                                }
                                break;
                            }
                            case 6: {
                                /*
                                    displays a list of accounts owned by the user that
                                    currently have a pending transaction
                                 */
                                System.out.println("These are the accounts you own that currently have a pending transfer");
                                List<Account> pendingTransfers = accountDAO.getPendingTransfers();
                                for (Account pending : pendingTransfers) {
                                    System.out.println(pending);
                                }
                                System.out.println("Would you like to accept this transfer?");
                                /*
                                    if the user does not wish to accept the transfer
                                    the pending accounts are restored to their previous state
                                 */
                                String confirm = scan.next();
                                if (confirm.equalsIgnoreCase("no") || confirm.equalsIgnoreCase("n")) {
                                    accountDAO.updateTransfer();
                                    System.out.println("Transfer cancelled.");
//                                    logger.info("Transfer declined.");
                                } else if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                                    /*
                                        in order to obtain the account information to be used in the transfer,
                                        the user is asked to confirm the transfer by inputting each accounts id when prompted
                                     */
                                    System.out.println("To confirm, please enter the id of the account to send the transfer");
                                    int firstId = scan.nextInt();
                                    Account firstAccount = new Account();
                                    firstAccount.setAccountNumber(firstId);
                                    accountDAO.getInfoForTransfer(firstAccount);
                                    double amount = firstAccount.getPendingTransaction();
                                    double balance = firstAccount.getBalance();
                                    if (amount > balance) {
                                        System.out.println("This account does not have enough money to support this transfer. Please deposit more before attempting again");
                                        accountDAO.updateTransfer();
                                    } else {
                                        accountDAO.withdraw(firstId, amount, balance);
                                        System.out.println("To confirm, please enter the id of the account receiving the transfer");
                                        int secondId = scan.nextInt();
                                        Account secondAccount = new Account();
                                        secondAccount.setAccountNumber(secondId);
                                        accountDAO.getInfoForTransfer(secondAccount);
                                        accountDAO.deposit(secondId, amount);
                                        accountDAO.updateTransfer();
//                                        logger.info("Transfer complete");
                                    }
                                } else {
                                    System.out.println("I don't understand that, please try again");
                                }
                                break;
                            }
                            //logs the user out and returns to the main menu
                            case 7: {
                                System.out.println("Goodbye.");
                                loggedIn = false;
                                break;
                            }
                            default: {
                                System.out.println("That is not a valid option. Please enter a valid selection.");
                            }
                        }

                    }
                } else if (user.getUserType().equalsIgnoreCase("employee")) {
                    while (loggedIn) {
                        System.out.println("----------------------------------");
                        System.out.println("Please select an option from below");
                        System.out.println("1. View a customer's bank accounts");
                        System.out.println("2. Approve an account");
                        System.out.println("3. Reject an account");
                        System.out.println("4. View all transactions");
                        System.out.println("5. Logout");
                        System.out.println("----------------------------------");
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
                                employeeDAO.viewTransactions();
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