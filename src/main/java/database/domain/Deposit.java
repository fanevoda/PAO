package database.domain;

import java.sql.Timestamp;
import java.text.CharacterIterator;

public class Deposit extends Transaction {
    public Deposit(int id, int userId, int accountId, double amount, int currencyId, Timestamp timestamp) {
        super(id, userId, accountId, amount, currencyId, timestamp);
    }

    @Override
    public String getType(){
        return "Deposit";
    }
    @Override
    public String toString(){
        return "In data de " + timestamp + " au fost depozitati " + amount + " de " + cr.getCurrencyName(currencyId);
    }
}
