package database.repository;

import database.config.DatabaseConfiguration;
import database.domain.Currency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyRepository {
    private static CurrencyRepository instance;
    private List<Currency> currencies = new ArrayList<Currency>();

    private CurrencyRepository() {};
    public static CurrencyRepository getInstance() {
        if (instance == null) {
            instance = new CurrencyRepository();
            instance.getAllCurrencies();
        }
        return instance;
    }

    private void getAllCurrencies() {

        String sql = "select * from currency";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                Currency new_curr = new Currency(rs.getInt("id"), rs.getString("cod"), rs.getString("symbol"), rs.getDouble("exchange_rate"));
                currencies.add(new_curr);
            }

        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public String getCurrencyName(int id){
        return currencies.get(id - 1).getName();
    }

}
