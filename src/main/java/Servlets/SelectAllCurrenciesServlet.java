package Servlets;

import Models.Currency;
import Models.DB;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/currencies")
public class SelectAllCurrenciesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<Currency> currencies = DB.selectAllCurrencies();
        ObjectMapper mapper = new ObjectMapper();
        for(int i=0;i<currencies.size();i++) {
            try {
                ObjectNode currency = mapper.createObjectNode();
                // create a JSON object
                currency.put("ID", currencies.get(i).getID());
                currency.put("Code", currencies.get(i).getCode());
                currency.put("FullName", currencies.get(i).getFullName());
                currency.put("Sign", currencies.get(i).getSign());
                // convert `ObjectNode` to pretty-print JSON
                // without pretty-print, use `user.toString()` method
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(currency);
                PrintWriter pw = response.getWriter();
                pw.println(json);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
        String code = request.getParameter("code");
        String name = request.getParameter("name");
        String sign = request.getParameter("sign");

        if(code.isEmpty()||name.isEmpty()||sign.isEmpty()){
            response.setStatus(500);
        }
        else {
            Currency c = new Currency(code,name,sign);
            DB.insertCurrency(c);
            doGet(request,response);
        }
    }
}
