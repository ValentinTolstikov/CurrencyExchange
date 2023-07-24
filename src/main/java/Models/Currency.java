package Models;

import java.io.Serializable;

public class Currency implements Serializable {
    private int ID;
    private String Code;
    private String FullName;
    private String Sign;


    public Currency(int ID, String code, String fullName, String sign) {
        this.ID = ID;
        Code = code;
        FullName = fullName;
        Sign = sign;
    }

    @Override
    public String toString(){
        return "ID:"+ID+"/Code:"+Code+"/FullName:"+FullName+"/Sign:"+Sign;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getSign() {
        return Sign;
    }

    public void setSign(String sign) {
        Sign = sign;
    }
}
