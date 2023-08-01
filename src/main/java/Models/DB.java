package Models;

import java.sql.*;
import java.util.ArrayList;

public class DB {
    private final static String url = "jdbc:mysql://localhost:3306/currencyenchange";
    private final static String username = "root";
    private final static String password = "2005";
    //Methods for Currencies
    public static ArrayList<Currency> selectAllCurrencies() {

        ArrayList<Currency> Currencies = new ArrayList<>();
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

    public static ArrayList<Exchangerate> selectAllExchangeRates() {

        ArrayList<Exchangerate> Exchanges = new ArrayList<>();
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
                    Exchangerate exchangerate = new Exchangerate(ID, BaseCurrencyId, TargetCurrencyId, Rate);
                    Exchanges.add(exchangerate);
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return Exchanges;
    }
    public static Exchangerate selectOneExchangeratesById(int id) {
        Exchangerate exchangerate = null;
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
                        exchangerate = new Exchangerate(ID, BaseCurrencyID,TargetCurrencyID,Rate);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return exchangerate;
    }
    public static Exchangerate selectOneExchangeratesByBaseCurrencyID(int base, int target) {
        Exchangerate exchangerate = null;
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
                        exchangerate = new Exchangerate(ID, BaseCurrencyID,TargetCurrencyID,Rate);
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return exchangerate;
    }
    public static int insertExchangerate(Exchangerate exchangerate) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "INSERT into exchangerates (ID,BaseCurrencyId,TargetCurrencyId,Rate) values  (?,?,?,?)";
                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                    preparedStatement.setInt(1,exchangerate.getID());
                    preparedStatement.setInt(2, exchangerate.getBaseCurrencyId());
                    preparedStatement.setInt(3, exchangerate.getTargetCurrencyId());
                    preparedStatement.setDouble(4, exchangerate.getRate());
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            return 1;
        }
        return 0;
    }
    public static int updateExchangerate(Exchangerate exchangerate) {

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
