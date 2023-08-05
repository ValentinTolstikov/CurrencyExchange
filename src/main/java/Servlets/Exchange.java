package Servlets;

import Models.Currency;
import Models.DB;
import Models.Exchangerate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/exchange")
public class Exchange extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from;
        String to;
        String sAmount;
        int amount;
        try {
             from = req.getParameter("from");
             to = req.getParameter("to");
             sAmount = req.getParameter("amount");
             amount = Integer.parseInt(sAmount);
        }catch (Exception ex){
            resp.sendError(500,"Поля введены некорректно");
            return;
        }

        if(DB.selectOneCurrencyByCode(from)==null){
            resp.sendError(500,"Исходная валюта не найдена в бд");
            return;
        }

        if(DB.selectOneCurrencyByCode(to)==null){
            resp.sendError(500,"Валюта конвертации не найдена");
            return;
        }

        double rate = getRate(from,to);

        Currency baseCurrency = DB.selectOneCurrencyByCode(from);
        Currency targetCurrency = DB.selectOneCurrencyByCode(to);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode exchange = mapper.createObjectNode();

        ObjectNode baseCurr = mapper.createObjectNode();
        baseCurr.put("id",baseCurrency.getID());
        baseCurr.put("name",baseCurrency.getFullName());
        baseCurr.put("code",baseCurrency.getCode());
        baseCurr.put("sign",baseCurrency.getSign());

        ObjectNode targetCurr = mapper.createObjectNode();
        targetCurr.put("id",targetCurrency.getID());
        targetCurr.put("name",targetCurrency.getFullName());
        targetCurr.put("code",targetCurrency.getCode());
        targetCurr.put("sign",targetCurrency.getSign());

        exchange.put("baseCurrency",baseCurr);
        exchange.put("targetCurrency",targetCurr);
        exchange.put("rate",rate);
        exchange.put("amount",amount);
        exchange.put("convertedAmount",rate*amount);

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(exchange);
        PrintWriter pw = resp.getWriter();
        pw.println(json);
    }

    private double getRate(String from,String to){

        int fromID = DB.selectOneCurrencyByCode(from).getID();
        int toID = DB.selectOneCurrencyByCode(to).getID();

        Exchangerate exchange = DB.selectOneExchangeratesByBaseCurrencyID(fromID,toID);

        if(exchange==null){
            exchange = DB.selectOneExchangeratesByBaseCurrencyID(toID,fromID);
            if(exchange==null){

                int idDollar = DB.selectOneCurrencyByCode("USD").getID();
                Exchangerate fromExchange = DB.selectOneExchangeratesByBaseCurrencyID(idDollar,fromID);
                double fromCurrencyToUSD = fromExchange.getRate();
                if(fromCurrencyToUSD<1){
                    fromCurrencyToUSD = 1/fromCurrencyToUSD;
                }
                Exchangerate toExchange = DB.selectOneExchangeratesByBaseCurrencyID(idDollar,toID);
                double fromUSDToTarget = toExchange.getRate();
                if(fromUSDToTarget<1){
                    fromUSDToTarget = 1/fromCurrencyToUSD;
                }

                return fromUSDToTarget/fromCurrencyToUSD;

            }
            else{
                return (1/exchange.getRate());
            }
        }
        else{
            return exchange.getRate();
        }
    }
}
