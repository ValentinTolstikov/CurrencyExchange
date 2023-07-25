package org.example;

import Models.Currency;
import Models.DB;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println(new Currency(1,"USD","Dollar","$"));
        ArrayList<Currency> currencies = DB.selectAllCurrencies();
        System.out.println(currencies.get(1));
    }
}