package database.domain;

import database.repository.BankAccountRepository;
import database.repository.UserRepository;
import database.service.AuditService;
import database.service.BankAccountsMenu;

import java.util.Scanner;

public class GoMenu {

    private int user_id;
    private static GoMenu instance;

    AuditService auditService = AuditService.getInstance();

    Scanner sc = new Scanner(System.in);
    UserRepository ur = UserRepository.getInstance();
    BankAccountRepository ba = BankAccountRepository.getInstance();
    BankAccountsMenu baMenu =  BankAccountsMenu.getInstance();

    private GoMenu(){};

    public static GoMenu getInstance()
    {
        if (instance == null)
            instance = new GoMenu();
        return instance;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void showMenu() {

        while(true) {

            System.out.println("Alege o optiune:");
            System.out.println("0. Exit");
            System.out.println("1. Schimba parola");
            System.out.println("2. Deschide un nou cont bancar");
            System.out.println("3. Afisare conturi bancare");

            int option = sc.nextInt();

            while (option < 0 || option > 3) {
                System.out.println("Alegere invalida, mai alege o data:");
                option = sc.nextInt();
            }

            switch (option) {
                case 0:
                    exitProgram();
                    auditService.writeToFile("log.csv", "Userul " + user_id + " a inchis programul.");
                    break;
                case 1:
                    changePass();
                    break;
                case 2:
                    newBankAccount();
                    break;
                case 3:
                    showBankAccounts();
                    break;
            }
        }
    }

    private void exitProgram() {
        System.exit(0);
    }

    private void changePass() {
        System.out.println("Introduceti noua parola:");

        if (sc.hasNextLine())
            sc.nextLine();

        String password = sc.nextLine();

        ur.changePassById(this.user_id, password);

    }

    private void newBankAccount()
    {
        ba.newBankAccount(user_id);
    }

    private void showBankAccounts()
    {

        baMenu.showMenu(user_id);
    }

}
