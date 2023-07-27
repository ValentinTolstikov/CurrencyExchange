package Models;

import java.sql.*;
import java.util.ArrayList;

public class DB {
    private static String url = "jdbc:mysql://localhost:3306/currencyenchange";
    private static String username = "root";
    private static String password = "2005";
    //Methods for Currencies
    public static ArrayList<Currency> selectAllCurrencies() {

        ArrayList<Currency> Currencies = new ArrayList<Currency>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM currencies");
                while (resultSet.next()) {

                    int ID;
                    String Code;
                    String FullName;
                    String Sign;

                    ID = resultSet.getInt("ID");
                    Code = resultSet.getString("Code");
                    FullName = resultSet.getString("FullName");
                    Sign = resultSet.getString("Sign");
                    Currency currency = new Currency(ID, Code, FullName, Sign);
                    Currencies.add(currency);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return Currencies;
    }

    public static Currency selectOneCurrencyByCode(String Name) {
        Currency currency = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                Connection con = DriverManager.getConnection(url, username, password);
                String sql = "SELECT * FROM currencies WHERE Code='"+Name+"'";
                Statement statement = con.createStatement();
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        int ID;
                        String Code;
                        String FullName;
                        String Sign;

                        ID = resultSet.getInt("ID");
                        Code = resultSet.getString("Code");
                        FullName = resultSet.getString("FullName");
                        Sign = resultSet.getString("Sign");
                        currency = new Currency(ID, Code, FullName, Sign);
                    }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return currency;
    }
    public static Currency selectOneCurrencyById(int Id) {
        Currency currency = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            Connection con = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM currencies WHERE ID = "+Id;
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                int ID;
                String Code;
                String FullName;
                String Sign;

                ID = resultSet.getInt("ID");
                Code = resultSet.getString("Code");
                FullName = resultSet.getString("FullName");
                Sign = resultSet.getString("Sign");
                currency = new Currency(ID, Code, FullName, Sign);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return currency;
    }

    public static int insertCurrency(Currency currency) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "INSERT INTO currencies (ID,Code,FullName,Sign) Values (?,?,?,?)";
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setInt(1, currency.getID());
                    preparedStatement.setString(2, currency.getCode());
                    preparedStatement.setString(3, currency.getFullName());
                    preparedStatement.setString(4, currency.getSign());
                    return preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }

    public static int updateCurrency(Currency currency) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "UPDATE currencies SET ID = ?, Code = ?, FullName = ?, Sign = ? WHERE ID = ?";
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setInt(1, currency.getID());
                    preparedStatement.setString(2, currency.getCode());
                    preparedStatement.setString(3, currency.getFullName());
                    preparedStatement.setString(4, currency.getSign());
                    preparedStatement.setInt(5, currency.getID());

                    return preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }

    public static int deleteCurrency(int id) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "DELETE FROM currencies WHERE ID = ?";
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setInt(1, id);

                    return preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }

    //Methods for ExchangeRates

    public static ArrayList<Exchangerates> selectAllExchangeRates() {

        ArrayList<Exchangerates> Exchanges = new ArrayList<Exchangerates>();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                Statement statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM exchangerates");
                while (resultSet.next()) {

                    int ID;
                    int BaseCurrencyId;
                    int TargetCurrencyId;
                    double Rate;

                    ID = resultSet.getInt("ID");
                    BaseCurrencyId = resultSet.getInt("BaseCurrencyId");
                    TargetCurrencyId = resultSet.getInt("TargetCurrencyId");
                    Rate = resultSet.getDouble("Rate");
                    Exchangerates exchangerates = new Exchangerates(ID, BaseCurrencyId, TargetCurrencyId, Rate);
                    Exchanges.add(exchangerates);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return Exchanges;
    }
    public static Exchangerates selectOneExchangeratesById(int id) {
        Exchangerates exchangerate = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "SELECT * FROM exchangerates WHERE ID=?";
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setInt(1, id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {

                        int ID;
                        int BaseCurrencyID;
                        int TargetCurrencyID;
                        double Rate;

                        ID = resultSet.getInt("ID");
                        BaseCurrencyID = resultSet.getInt("BaseCurrencyId");
                        TargetCurrencyID = resultSet.getInt("TargetCurrencyId");
                        Rate = resultSet.getDouble("Rate");
                        exchangerate = new Exchangerates(ID, BaseCurrencyID,TargetCurrencyID,Rate);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return exchangerate;
    }
    public static Exchangerates selectOneExchangeratesByName(int base,int target) {
        Exchangerates exchangerate = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "SELECT * FROM exchangerates WHERE (BaseCurrencyId=?)and (TargetCurrencyId=?)";
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setInt(1, base);
                    preparedStatement.setInt(2,target);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {

                        int ID;
                        int BaseCurrencyID;
                        int TargetCurrencyID;
                        double Rate;

                        ID = resultSet.getInt("ID");
                        BaseCurrencyID = resultSet.getInt("BaseCurrencyId");
                        TargetCurrencyID = resultSet.getInt("TargetCurrencyId");
                        Rate = resultSet.getDouble("Rate");
                        exchangerate = new Exchangerates(ID, BaseCurrencyID,TargetCurrencyID,Rate);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return exchangerate;
    }
    public static int insertExchangerate(Exchangerates exchangerates) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "INSERT INTO exchangerates (ID,BaseCurrencyId,TargetCurrencyId,Rate) Values (?,?,?,?)";
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setInt(1, exchangerates.getID());
                    preparedStatement.setInt(2, exchangerates.getBaseCurrencyId());
                    preparedStatement.setInt(3, exchangerates.getTargetCurrencyId());
                    preparedStatement.setDouble(4, exchangerates.getRate());
                    return preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }
    public static int updateExchangerate(Exchangerates exchangerate) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "UPDATE exchangerates SET ID = ?, BaseCurrencyId = ?, TargetCurrencyId = ?, Rate = ?,  WHERE ID = ?";
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setInt(1, exchangerate.getID());
                    preparedStatement.setInt(2, exchangerate.getBaseCurrencyId());
                    preparedStatement.setInt(3, exchangerate.getTargetCurrencyId());
                    preparedStatement.setDouble(4, exchangerate.getRate());
                    preparedStatement.setInt(5, exchangerate.getID());

                    return preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }
    public static int deleteExchangeRate(int id) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "DELETE FROM exchangerates WHERE ID = ?";
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setInt(1, id);

                    return preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return 0;
    }
}
