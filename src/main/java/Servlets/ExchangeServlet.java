package Servlets;

import Models.Currency;
import Models.DB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exchangeRate/*")
public class ExchangeServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getPathInfo() == null || req.getPathInfo().equals("/")){
            resp.sendError(400,"Указана не корректная валютная пара");
            return;
        }
        String CurrencyCode = req.getPathInfo().replaceFirst("/","").toUpperCase();

        if(CurrencyCode.length()!=6){
            resp.sendError(400,"Указана не корректная валютная пара");
            return;
        }
        String TargetCode = CurrencyCode.substring(0,3);
        String CurrentCode = CurrencyCode.substring(3,6);
        int TargetId;
        int CurrencyId;

        Currency currency = DB.selectOneCurrencyByCode(TargetCode);
        TargetId = currency.getID();
        currency = DB.selectOneCurrencyByCode(CurrentCode);
        CurrencyId = currency.getID();



    }
}
