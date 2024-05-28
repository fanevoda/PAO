package database.domain;

public class Currency {
    private int id;
    private String name;
    private String symbol;
    private double exchangeRate;
    public Currency(int id, String name, String symbol, double exchangeRate) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.exchangeRate = exchangeRate;
    }

    public String getName() {
        return name;
    }

    public int getId(){
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getExchangeRate() {
        return exchangeRate;
    }
}
