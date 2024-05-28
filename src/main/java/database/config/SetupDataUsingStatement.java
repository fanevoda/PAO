package database.config;

import database.domain.Currency;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SetupDataUsingStatement {

    private SetupDataUsingStatement(){}

    public static void createTables()
    {
        createUserTable();
        createCurrencyTable();
        createBankAccountTable();
        createTransactionTable();
    }



    private static void createUserTable() {
        String createTableSql = """
            CREATE TABLE IF NOT EXISTS user (
                id int PRIMARY KEY AUTO_INCREMENT,
                name varchar(40) ,
                pass varchar(40)
            );
            """;

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);

            // sa nu uit la audit
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createCurrencyTable() {

        String createTableSql = """
            CREATE TABLE IF NOT EXISTS currency (
                id int PRIMARY KEY AUTO_INCREMENT,
                cod varchar(40) ,
                country varchar(40),
                symbol varchar(40),
                exchange_rate DOUBLE
            );
            """;

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);

            // sa nu uit la audit
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void createBankAccountTable(){

        String createTableSql = """
            CREATE TABLE IF NOT EXISTS bankAccount (
                id int PRIMARY KEY AUTO_INCREMENT,
                user_id int,
                iban varchar(50),
                currency_id int,
                balance DOUBLE,
                FOREIGN KEY (user_id) REFERENCES user (id),
                FOREIGN KEY (currency_id) REFERENCES currency (id)
            );
            """;

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);

            // sa nu uit la audit
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void createTransactionTable(){

        String createTableSql = """
            CREATE TABLE IF NOT EXISTS transaction (
                id int PRIMARY KEY AUTO_INCREMENT,
                user_id int,
                account_id int,
                amount double,
                currency_id int,
                transaction_type varchar(50),
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES user (id),
                FOREIGN KEY (account_id) REFERENCES bankAccount (id),
                FOREIGN KEY (currency_id) REFERENCES currency (id)
            );
            """;

        Connection connection = DatabaseConfiguration.getDatabaseConnection();

        try {
            Statement statement = connection.createStatement();
            statement.execute(createTableSql);

            // sa nu uit la audit
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}