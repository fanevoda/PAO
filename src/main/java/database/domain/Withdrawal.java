package database.domain;

import java.sql.Timestamp;

public class Withdrawal extends Transaction {
    public Withdrawal(int id, int userId, int accountId, double amount, int currencyId, Timestamp timestamp) {
        super(id, userId, accountId, amount, currencyId, timestamp);
    }

    @Override
    public String getType(){
        return "Withdrawal";
    }

    @Override
    public String toString(){
        return "In data de " + timestamp + " au fost retrasi " + amount + " de " + cr.getCurrencyName(currencyId);

    }
}
