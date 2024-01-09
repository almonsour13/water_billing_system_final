package controller.accountLoggedSetter;

import java.io.*;
import java.util.Scanner;

public class LoggedAccountSetter {
    private static String path = "C:/Users/Merry Ann/Documents/NetBeansProjects/water billing sytem/src/controller/accountLoggedSetter/accounts.txt";
   
    public void setAccount(int accountID) {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
            writer.println(accountID);
            System.out.println("Account ID set successfully");
        } catch (IOException e) {
            System.err.println("Error setting account ID: " + e.getMessage());
        }
    }

    public void removeAccount() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(path)))) {
            writer.print("");
            System.out.println("Account ID removed successfully");
        } catch (IOException e) {
            System.err.println("Error removing account ID: " + e.getMessage());
        }
    }

    public int getAccount() {
        int accountID = 0;
        try (Scanner myReader = new Scanner(new File(path))) {
            if (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                accountID = Integer.parseInt(data);
            }
        } catch (FileNotFoundException | NumberFormatException e) {
            System.err.println("Error reading account ID: " + e.getMessage());
        }
        return accountID;
    }
    public boolean checkAccount() {
        try (Scanner myReader = new Scanner(new File(path))) {
            return myReader.hasNextLine();
        } catch (FileNotFoundException e) {
            System.err.println("Error checking account: " + e.getMessage());
        }
        return false;
    }
}