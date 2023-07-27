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

@WebServlet("/currency/*")
public class SelectCurrencyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        if(request.getPathInfo() == null || request.getPathInfo().equals("/")){
            response.sendError(400,"Указана не корректная валюта");
            return;
        }

        String CurrencyCode = request.getPathInfo().replaceFirst("/","").toUpperCase();

        Currency currency = DB.selectOneCurrencyByCode(CurrencyCode);

        if(currency==null){
            response.sendError(500,"Данная валюта отсутствует в базе");
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode Currency = mapper.createObjectNode();
        // create a JSON object
        Currency.put("ID", currency.getID());
        Currency.put("Code", currency.getCode());
        Currency.put("FullName",currency.getFullName());
        Currency.put("Sign",currency.getSign());
        // convert `ObjectNode` to pretty-print JSON
        // without pretty-print, use `user.toString()` method
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(currency);
        PrintWriter pw = response.getWriter();
        pw.println(json);
    }
}
