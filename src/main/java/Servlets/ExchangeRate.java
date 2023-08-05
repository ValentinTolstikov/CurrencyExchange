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

@WebServlet("/exchangeRate/*")
public class ExchangeRate extends HttpServlet{
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
            this.doPatch(req, resp);
        }
        else{
            if(method.equals("GET")){
                doGet(req,resp);
            }
        }
    }
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
        String BaseCode = CurrencyCode.substring(0,3);
        String TargetCode = CurrencyCode.substring(3,6);

        int TargetId;
        int baseId;

        Currency target = DB.selectOneCurrencyByCode(TargetCode);
        if(target == null){
            resp.sendError(404,"Обменный курс не найден");
            return;
        }
        TargetId = target.getID();
        Currency base = DB.selectOneCurrencyByCode(BaseCode);
        if(base == null){
            resp.sendError(404,"Обменный курс не найден");
            return;
        }
        baseId = base.getID();
        Exchangerate exchangerate = DB.selectOneExchangeratesByBaseCurrencyID(baseId,TargetId);
        if(exchangerate == null){
            resp.sendError(404,"Обменный курс не найден");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode currency = mapper.createObjectNode();

        currency.put("ID", exchangerate.getID());

        currency.put("Base currency id", base.getID());
        currency.put("Base currency Code", base.getCode());
        currency.put("Base currency FullName", base.getFullName());
        currency.put("Base currency Sign", base.getSign());

        currency.put("Target currency id", target.getID());
        currency.put("Target currency Code", target.getCode());
        currency.put("Target currency FullName", target.getFullName());
        currency.put("Target currency Sign", target.getSign());

        currency.put("Rate",exchangerate.getRate());

        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(currency);
        PrintWriter pw = resp.getWriter();
        pw.println(json);
    }
    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        double rate = Double.parseDouble(req.getParameter("rate"));

        if(rate<=0){
            resp.sendError(500,"Неверный обменный курс");
            return;
        }

        if(req.getPathInfo() == null || req.getPathInfo().equals("/")){
            resp.sendError(400,"Указана не корректная валютная пара");
            return;
        }
        String CurrencyCode = req.getPathInfo().replaceFirst("/","").toUpperCase();

        if(CurrencyCode.length()!=6){
            resp.sendError(400,"Указана не корректная валютная пара");
            return;
        }
        String BaseCode = CurrencyCode.substring(0,3);
        String TargetCode = CurrencyCode.substring(3,6);

        int TargetId;
        int baseId;

        Currency target = DB.selectOneCurrencyByCode(TargetCode);
        if(target == null){
            resp.sendError(404,"Обменный курс не найден");
            return;
        }
        TargetId = target.getID();

        Currency base = DB.selectOneCurrencyByCode(BaseCode);
        if(base == null){
            resp.sendError(404,"Обменный курс не найден");
            return;
        }
        baseId = base.getID();

        Exchangerate exchangerate = DB.selectOneExchangeratesByBaseCurrencyID(baseId,TargetId);
        if(exchangerate == null){
            resp.sendError(404,"Обменный курс не найден");
            return;
        }
        else{
            exchangerate.setRate(rate);
            int code = DB.updateExchangerate(exchangerate);

            if(code==0) {
                ObjectMapper mapper = new ObjectMapper();
                ObjectNode currency = mapper.createObjectNode();

                currency.put("ID", exchangerate.getID());

                currency.put("Base currency id", base.getID());
                currency.put("Base currency Code", base.getCode());
                currency.put("Base currency FullName", base.getFullName());
                currency.put("Base currency Sign", base.getSign());

                currency.put("Target currency id", target.getID());
                currency.put("Target currency Code", target.getCode());
                currency.put("Target currency FullName", target.getFullName());
                currency.put("Target currency Sign", target.getSign());

                currency.put("Rate", exchangerate.getRate());

                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(currency);
                PrintWriter pw = resp.getWriter();
                pw.println(json);
            }
            else{
                resp.sendError(500,"Somthing wrong");
                return;
            }

        }

    }
}
