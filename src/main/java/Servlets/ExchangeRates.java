package Servlets;

import Models.Currency;
import Models.DB;
import Models.Exchangerates;
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
        List<Exchangerates> lstExchanges = DB.selectAllExchangeRates();
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
}
