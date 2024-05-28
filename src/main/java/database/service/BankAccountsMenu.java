package database.service;

import database.domain.*;
import database.repository.BankAccountRepository;
import database.repository.TransactionRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Scanner;

public class BankAccountsMenu {
    static BankAccountsMenu instance;
    private BankAccountsMenu() {};

    private TransactionRepository tr = TransactionRepository.getInstance();

    private int user_id;

    public static BankAccountsMenu getInstance() {
        if (instance == null) {
            instance = new BankAccountsMenu();
        }
        return instance;
    }

    BankAccountRepository ba = BankAccountRepository.getInstance();

    public void showMenu(int user_id)
    {
        this.user_id = user_id;

        List<BankAccount> bankAccounts = ba.getBankAccountsById(user_id);

        System.out.println("Alegeti un cont bancar: ");

        for (int i = 1; i <= bankAccounts.size(); i++){
            System.out.println(i + ". " + bankAccounts.get(i - 1).toString());
        }

        int option = -1;

        Scanner sc = new Scanner(System.in);

        option = sc.nextInt();

        while(option <= 0 || option > bankAccounts.size()){
            System.out.println("Indice invalid, mai incearca o data:");
            option = sc.nextInt();
        }

        showMenu(user_id, bankAccounts.get(option - 1));
    }

    private void showMenu(int user_id, BankAccount bankAcc) {
        System.out.println(bankAcc.toString());
        System.out.println("Alege o optiune:");
        System.out.println("0. Inapoi");
        System.out.println("1. Depune bani");
        System.out.println("2. Retrage bani");
        System.out.println("3. Transfer");
        System.out.println("4. Istoric tranzactii.");

        Scanner sc = new Scanner(System.in);

        int option = sc.nextInt();

        while (option < 0 || option > 4) {
            System.out.println("Alegere invalida, mai alege o data:");
            option = sc.nextInt();
        }

        switch (option) {
            case 0:
                break;
            case 1:
                deposit(bankAcc);
                break;
            case 2:
                withdraw(bankAcc);
                break;
            case 3:
                transfer(bankAcc);
                break;
            case 4:
                tr.transactionHistory(bankAcc);
                break;
        }

    }

    private void deposit(BankAccount bankAcc) {
        System.out.println("Ce suma doriti sa depozitati?");

        Scanner sc = new Scanner(System.in);

        double deposit = sc.nextDouble();

        while(deposit <= 0)
        {
            System.out.println("Suma depozitata trebuie sa fie mai mare ca 0, mai introduceti o data.");
            deposit = sc.nextDouble();
        }

        if (deposit >= 0)
            bankAcc.addToBalance(deposit);

        System.out.println("Ati depozitat cu succes suma de " + deposit + bankAcc.getCurrencyName() +", o zi buna in continuare." );

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Transaction transaction = new Deposit(tr.nextTransactionId(), bankAcc.getUserId(), bankAcc.getId(), deposit, bankAcc.getCurrencyId(), timestamp);
        tr.insertTransaction(transaction);

    }

    private void withdraw(BankAccount bankAcc){

        if (bankAcc.getBalance() <= 0){
            System.out.println("Nu aveti suficiente fonduri pentru a retrage.");
            return;
        }

        System.out.println("Sold disponibil: " + bankAcc.getBalance() + bankAcc.getCurrencyName());
        System.out.println("Ce suma doriti sa retrageti?");

        Scanner sc = new Scanner(System.in);

        double withdraw = sc.nextDouble();

        while(withdraw <= 0)
        {
            System.out.println("Suma retrasa trebuie sa fie mai mare ca 0, mai introduceti o data.");
            withdraw = sc.nextDouble();
        }

        if (withdraw < bankAcc.getBalance()) {
            bankAcc.addToBalance(-withdraw);
            System.out.println("Ati retras cu succes suma de " + withdraw + bankAcc.getCurrencyName() + "!");

            // cream o tranzactie noua

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

            Transaction transaction = new Withdrawal(tr.nextTransactionId(), bankAcc.getUserId(), bankAcc.getId(), withdraw, bankAcc.getCurrencyId(),timestamp);
            tr.insertTransaction(transaction);
        }
        else
            System.out.println("Fonduri insuficiente.");
    }

    private void transfer(BankAccount bankAcc) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Introduceti ibanul conutului caruia doriti sa efectuati transferul.");

        String ibanTransfer = sc.nextLine();

        BankAccount destination = ba.getBankAccountByIban(ibanTransfer);

        if (destination == null)
        {
            System.out.println("Iban incorect.");
            return;
        }

        System.out.println("Introduceti suma pe care doriti sa o transferati.");

        int amount = sc.nextInt();

        while(amount < 0)
        {
            if (amount < 0)
                System.out.println("Suma trebuie sa fie mai mare ca 0, mai introduceti o data.");
            amount = sc.nextInt();
        }

        if (amount > bankAcc.getBalance()) {
            System.out.println("Fonduri insuficiente.");
            return;
        }

        double amount_after = bankAcc.transfer(destination, amount);
        System.out.println("Transfer efectuat cu succes.");


        // cream 2 transferuri noi

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Transaction transaction = new Transfer(tr.nextTransactionId(), bankAcc.getUserId(), bankAcc.getId(), -amount, bankAcc.getCurrencyId(),timestamp);
        tr.insertTransaction(transaction);

        transaction = new Transfer(tr.nextTransactionId(), destination.getUserId(), destination.getId(), amount_after, destination.getCurrencyId(),timestamp);
        tr.insertTransaction(transaction);

        // audit si aici
    }

}
