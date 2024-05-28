package database.domain;

import database.repository.UserRepository;

import java.util.Scanner;

public class Menu {
    private static Menu instance;

    Scanner scanner = new Scanner(System.in);
    UserRepository ur = UserRepository.getInstance();

    int user_id;

    private Menu(){};

    public static Menu getInstance()
    {
        if (instance == null)
            instance = new Menu();
        return instance;
    }

    public final void showSignupLogin()
    {
        System.out.println("Alege o optiune:");
        System.out.println("0. Exit");
        System.out.println("1. Creeaza cont");
        System.out.println("2. Autentificare");

        int option = scanner.nextInt();

        while(option < 0 || option > 2){
            System.out.println("Alegere invalida, mai alege o data:");
            option = scanner.nextInt();
        }

        switch (option) {
            case 0:
                exitProgram();
                break;
            case 1:
                createAccount();
                break;
            case 2:
                authenticate();
                break;
        }

    }

    public final void exitProgram(){
        System.out.println("Zi buna!");
        System.exit(0);
    }

    public final void createAccount(){
        ur.signup();
        System.out.println("Inregistrare reusita.");
        showSignupLogin();
    }

    public final void authenticate(){
        // aici actualizam userid
        this.user_id = ur.login();

        if (this.user_id > 0) {
            prepNextMenu();
        }
    }


    public final void prepNextMenu() {
        System.out.println("Autentificare reusita.\n\n");
        GoMenu nextMenu = GoMenu.getInstance();
        nextMenu.setUser_id(this.user_id);
    }


}
