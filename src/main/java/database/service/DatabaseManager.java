package database.service;

import database.config.SetupDataUsingStatement;

public class DatabaseManager {
    private static DatabaseManager instance;
    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
            instantiateDB();
        }
        return instance;
    }

    private static void instantiateDB(){
        SetupDataUsingStatement.createTables();
    }
}
