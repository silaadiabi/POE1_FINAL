package com.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login app = new Login();

        // === Registration ===
        System.out.println("=== Registration ===");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();

        System.out.print("Enter username: ");
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine();

        String regMsg = app.registerUser(firstName, lastName, username, password, phone);
        System.out.println(regMsg);

        if (!regMsg.contains("successfully")) {
            System.out.println("Exiting program due to registration failure.");
            scanner.close();
            return;
        }

        // === Login ===
        System.out.println("\n=== Login ===");
        System.out.print("Enter username: ");
        String loginUser = scanner.nextLine();

        System.out.print("Enter password: ");
        String loginPass = scanner.nextLine();

        String loginMsg = app.returnLoginStatus(loginUser, loginPass);
        System.out.println(loginMsg);

        scanner.close();
    }
}
