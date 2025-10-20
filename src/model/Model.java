package model;

import java.io.Serializable;

public abstract class Model implements Serializable {
    protected int id;
    protected String xmlName;

    public abstract String stringifyCSV();
    public abstract Model unstringifyCSV(String string);
    public abstract String stringifyXML();
    public abstract Model unstringifyXML(String string);
    //TODO sql


    public int getId() {
        return id;
    }

    public String getXmlName() {
        return xmlName;
    }
}
