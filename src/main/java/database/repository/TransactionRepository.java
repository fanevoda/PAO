package database.repository;

import database.config.DatabaseConfiguration;
import database.domain.*;
import database.service.AuditService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.TreeSet;

public class TransactionRepository {

    private static TransactionRepository instance;
    private TransactionRepository(){};

    TreeSet<Transaction> transactions = new TreeSet<Transaction>();

    public static TransactionRepository getInstance(){
        if(instance == null){
            instance = new TransactionRepository();
        }
        return instance;
    }

    AuditService auditService = AuditService.getInstance();

    private void getTransactions(){

        transactions.clear();

        String sql = "select * from transaction";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                int id = rs.getInt("id");

                String transactionType = rs.getString("transaction_type");

                Transaction transaction = null;

                if (transactionType.equalsIgnoreCase("Deposit")){
                    transaction = new Deposit(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("account_id"), rs.getDouble("amount"), rs.getInt("currency_id"), rs.getTimestamp("timestamp"));
                }
                if (transactionType.equalsIgnoreCase("Withdrawal")){
                    transaction = new Withdrawal(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("account_id"), rs.getDouble("amount"), rs.getInt("currency_id"), rs.getTimestamp("timestamp"));
                }
                if (transactionType.equalsIgnoreCase("Transfer")){
                    transaction = new Transfer(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("account_id"), rs.getDouble("amount"), rs.getInt("currency_id"), rs.getTimestamp("timestamp"));
                }

                transactions.add(transaction);
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }

    }

    public void insertTransaction(Transaction transaction){

        String sql = "insert into transaction (user_id, account_id, amount, currency_id, transaction_type, timestamp) values (?, ?, ?, ?, ?, ?)";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, transaction.getUserId());
            ps.setInt(2, transaction.getAccountId());
            ps.setDouble(3, transaction.getAmount());
            ps.setInt(4, transaction.getCurrencyId());
            ps.setString(5, transaction.getType());
            ps.setTimestamp(6, transaction.getTimestamp());
            ps.execute();

            // nu uita audit

            System.out.println("Am adaugat o tranzactie de tip " + transaction.getType() + " in valoare de " + transaction.getAmount());
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
    }


    public int nextTransactionId(){
        String sql = "select max(id) from transaction";
        Connection conn = DatabaseConfiguration.getDatabaseConnection();

        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }

        }catch(SQLException e){
            e.printStackTrace();
        }

        return 1;
    }


    public void transactionHistory(BankAccount bankAccount){
        getTransactions();

        for (Transaction transaction : transactions){
            if (transaction.getUserId() == bankAccount.getUserId()){
                System.out.println(transaction);
            }
        }

        auditService.writeToFile("log.csv", "Userul " + bankAccount.getUserId() + " a vizualizat istoricul tranzactiilor asociate contului " + bankAccount.getIban());
    }




}