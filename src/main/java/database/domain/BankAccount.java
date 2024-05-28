package database.domain;

import database.repository.BankAccountRepository;

public class BankAccount {
    private int id;
    private int user_id;
    private String iban;
    private Currency currency;
    private double balance;

    private BankAccountRepository ba = BankAccountRepository.getInstance();

    public BankAccount(int id, int user_id, String iban, Currency currency, double balance) {
        this.id = id;
        this.user_id = user_id;
        this.iban = iban;
        this.currency = currency;
        this.balance = balance;
    }



    public String toString()
    {
        return iban + " " + balance + currency.getName();
    }

    public void addToBalance(double deposit){
        this.balance += deposit;
        ba.updateBalance(id, this.balance);

    }

    public int getId(){
        return id;
    }

    public int getUserId(){
        return user_id;
    }

    public int getCurrencyId(){
        return currency.getId();
    }

    public String getCurrencyName() {
        return currency.getName();
    }

    public double getExchangeRate(){
        return currency.getExchangeRate();
    }

    public double getBalance() {
        return balance;
    }

    public String getIban(){
        return iban;
    }

    public double transfer(BankAccount to, double amount) {
        addToBalance(-amount);

        double amount_after_exch = amount * this.getExchangeRate();
        amount_after_exch = amount_after_exch / to.getExchangeRate();

        to.addToBalance(amount_after_exch);

        return amount_after_exch;
    }



}
