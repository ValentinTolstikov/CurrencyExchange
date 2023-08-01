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
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRates extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Exchangerate> lstExchanges = DB.selectAllExchangeRates();
        ObjectMapper mapper = new ObjectMapper();
        for(int i=0;i<lstExchanges.size();i++) {
            try {
                ObjectNode currency = mapper.createObjectNode();
                // create a JSON object
                currency.put("ID", lstExchanges.get(i).getID());
                Currency base = DB.selectOneCurrencyById(lstExchanges.get(i).getBaseCurrencyId());
                Currency target = DB.selectOneCurrencyById(lstExchanges.get(i).getTargetCurrencyId());

                currency.put("Base currency id", base.getID());
                currency.put("Base currency Code", base.getCode());
                currency.put("Base currency FullName", base.getFullName());
                currency.put("Base currency Sign", base.getSign());

                currency.put("Target currency id", target.getID());
                currency.put("Target currency Code", target.getCode());
                currency.put("Target currency FullName", target.getFullName());
                currency.put("Target currency Sign", target.getSign());

                currency.put("Rate", lstExchanges.get(i).getRate());
                // convert `ObjectNode` to pretty-print JSON
                // without pretty-print, use `user.toString()` method
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(currency);
                PrintWriter pw = resp.getWriter();
                pw.println(json);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String baseCurrencyCode = req.getParameter("baseCurrency");
        String targetCurrencyCode = req.getParameter("targetCurrency");
        double rate;
        try {
            rate = Double.parseDouble(req.getParameter("rate"));
        } catch (Exception e){
            resp.sendError(500,"Курс введен неверно");
            return;
        }

        List<Exchangerate> allExchanges = DB.selectAllExchangeRates();

        boolean isInDB=false;

        Currency dbBaseCurrency;
        Currency dbTargetCurrency;

        int dbTargetCurrencyId = -1;

        int dbBaseCurrencyId = -1;

        if(DB.selectOneCurrencyByCode(baseCurrencyCode)==null||DB.selectOneCurrencyByCode(targetCurrencyCode)==null){
                resp.sendError(500,"В базе отсутствует одна из валют");
                return;
        }
        else {
            dbBaseCurrency=DB.selectOneCurrencyByCode(baseCurrencyCode);
            dbBaseCurrencyId = dbBaseCurrency.getID();
            dbTargetCurrency=DB.selectOneCurrencyByCode(targetCurrencyCode);
            dbTargetCurrencyId = dbTargetCurrency.getID();
                if(DB.selectOneExchangeratesByBaseCurrencyID(dbBaseCurrencyId,dbTargetCurrencyId)!=null){
                    resp.sendError(409,"Валютная пара с таким кодом уже существует");
                    return;
                }
                else {
                    Exchangerate exchangerate = new Exchangerate(dbBaseCurrencyId,dbTargetCurrencyId,rate);
                    int code = DB.insertExchangerate(exchangerate);

                    ObjectMapper mapper = new ObjectMapper();
                    ObjectNode ex = mapper.createObjectNode();

                    ex.put("ID",exchangerate.getID());
                    ex.put("Base currency id", dbBaseCurrency.getID());
                    ex.put("Base currency Code", dbBaseCurrency.getCode());
                    ex.put("Base currency FullName", dbBaseCurrency.getFullName());
                    ex.put("Base currency Sign", dbBaseCurrency.getSign());

                    ex.put("Target currency id", dbTargetCurrency.getID());
                    ex.put("Target currency Code", dbTargetCurrency.getCode());
                    ex.put("Target currency FullName", dbTargetCurrency.getFullName());
                    ex.put("Target currency Sign", dbTargetCurrency.getSign());
                    ex.put("Rate",exchangerate.getRate());

                    String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(ex);
                    PrintWriter pw = resp.getWriter();
                    pw.println(json);


                }
        }
    }
}
