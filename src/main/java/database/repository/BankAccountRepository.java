package database.repository;

import database.config.DatabaseConfiguration;
import database.domain.BankAccount;
import database.domain.Currency;
import database.service.AuditService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class BankAccountRepository {

    List<BankAccount> bankAccounts = new ArrayList<BankAccount>();
    Map<Integer, BankAccount> bankAccountMap = new HashMap<Integer, BankAccount>();

    CurrencyRepository cr = CurrencyRepository.getInstance();
    AuditService auditService = AuditService.getInstance();

    private static BankAccountRepository instance;

    private BankAccountRepository(){};

    public static BankAccountRepository getInstance(){
        if(instance == null){
            instance = new BankAccountRepository();
        }
        return instance;
    }

    public void newBankAccount(int user_id) {

        // sa aleaga in ce valuta vrea contul

        Scanner sc = new Scanner(System.in);

        List<Currency> currencies = cr.getCurrencies();

        System.out.println("In ce valuta doriti sa deschideti contul?");

        for (int i = 1; i <= currencies.size(); i++) {
            Currency currency = currencies.get(i - 1);
            System.out.println(i + ". " + currency.getName() + ' ' + currency.getSymbol());
        }

        int currency_id = 0;

        while(currency_id < 1 || currency_id > currencies.size()) {
            currency_id = sc.nextInt();
            if(currency_id < 1 || currency_id > currencies.size()) {
                System.out.println("Eroare: id invalid, incercati din nou.");
            }
        }



        // generz iban (imi dau seama ca nu-l fac unic dar sansele sa fie aceleasi o sa fie mic sper)

        String iban = generateIban();


        String sql = "insert into bankAccount (user_id, iban, currency_id, balance) values (?, ?, ?, ?)";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user_id);
            ps.setString(2, iban);
            ps.setInt(3, currency_id);
            ps.setInt(4, 0);
            ps.execute();

            // nu uita audit

            System.out.println("Am adaugat un cont bancar cu ibanul " + iban + " avand ca valuta " + currency_id);

            auditService.writeToFile("log.csv", "Userul " + user_id + "a creat un nou cont bancar cu ibanul " + iban + " avand ca valuta " + currency_id);
        }catch(SQLException e)
        {
            e.printStackTrace();
        }


    }

    private String generateIban()
    {
        String iban = "";

        Random random = new Random();

        StringBuilder ibanBuilder = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            ibanBuilder.append(random.nextInt(10));
        }
        iban = "RO" + ibanBuilder.toString();

        return iban;
    }

    private void getAllBankAccounts(int user_id) {
        bankAccounts.clear();

        String sql = "select * from bankAccount where user_id = ?";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user_id);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");

                BankAccount bankAccount;

                if (bankAccountMap.containsKey(id)){
                    bankAccount = bankAccountMap.get(id);
                }
                else{
                    int currency_id = rs.getInt("currency_id");

                    Currency currency = cr.getCurrencies().get(currency_id - 1);
                    bankAccount = new BankAccount(rs.getInt("id"), rs.getInt("user_id"), rs.getString("iban"), currency, rs.getDouble("balance"));
                    bankAccountMap.put(id, bankAccount);
                }
                bankAccounts.add(bankAccount);
            }

        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<BankAccount> getBankAccountsById(int user_id)
    {
        getAllBankAccounts(user_id);

        return bankAccounts;
    }

    public void updateBalance(int bank_id, double amount) {

        String sql = "update bankAccount set balance = ? where id = ?";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, bank_id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0){
                System.out.println("Suma modificata cu succes.");
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

    }

    public BankAccount getBankAccountByIban(String iban){

        String sql = "select * from bankAccount where iban = ?";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        BankAccount bankAccount;

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, iban);
            ResultSet rs = ps.executeQuery();

            if (rs.next()){
                int id = rs.getInt("id");
                if (bankAccountMap.containsKey(id)){
                    bankAccount = bankAccountMap.get(id);
                }
                else{
                    int currency_id = rs.getInt("currency_id");

                    Currency currency = cr.getCurrencies().get(currency_id - 1);
                    bankAccount = new BankAccount(rs.getInt("id"), rs.getInt("user_id"), rs.getString("iban"), currency, rs.getDouble("balance"));
                    bankAccountMap.put(id, bankAccount);
                }
                return bankAccount;
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }


}
