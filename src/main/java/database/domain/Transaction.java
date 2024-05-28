package database.domain;

import database.repository.CurrencyRepository;
import database.repository.UserRepository;

import java.sql.Timestamp;

public abstract class Transaction implements Comparable<Transaction>{
    protected int id;
    protected int userId;
    protected int accountId;
    protected double amount;
    protected int currencyId;
    protected Timestamp timestamp;

    protected UserRepository ur = UserRepository.getInstance();
    protected CurrencyRepository cr = CurrencyRepository.getInstance();

    public Transaction(int id, int userId, int accountId, double amount, int currencyId, Timestamp timestamp) {
        this.id = id;
        this.userId = userId;
        this.accountId = accountId;
        this.amount = amount;
        this.currencyId = currencyId;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }
    public int getUserId() {
        return userId;
    }
    public int getAccountId() {
        return accountId;
    }
    public double getAmount() {
        return amount;
    }
    public int getCurrencyId() {
        return currencyId;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }

    abstract public String getType();

    @Override
    public int compareTo(Transaction other) {
        return this.timestamp.compareTo(other.timestamp);
    }
}
