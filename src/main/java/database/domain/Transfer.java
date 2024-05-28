package database.domain;

import java.sql.Timestamp;

public class Transfer extends Transaction {
    public Transfer(int id, int userId, int accountId, double amount, int currencyId, Timestamp timestamp) {
        super(id, userId, accountId, amount, currencyId, timestamp);
    }

    @Override
    public String getType(){
        return "Transfer";
    }

    @Override
    public String toString(){
        if (amount > 0)
            return "In data de " + timestamp + " a fost primit un transfer in valoare de " + amount + " de " + cr.getCurrencyName(currencyId);
        else
            return "In data de " + timestamp + " a fost trimis un transfer in valoare de " + amount + " de " + cr.getCurrencyName(currencyId);
    }
}
