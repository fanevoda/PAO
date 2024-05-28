package org.example;


import database.config.SetupDataUsingStatement;
import database.domain.GoMenu;
import database.domain.Menu;
import database.repository.UserRepository;
import database.service.DatabaseManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        DatabaseManager databaseManager = DatabaseManager.getInstance();

        Scanner sc = new Scanner(System.in);

        Menu loginMeniu = Menu.getInstance();

        loginMeniu.showSignupLogin();

        GoMenu goMenu = GoMenu.getInstance();
        goMenu.showMenu();


    }
}