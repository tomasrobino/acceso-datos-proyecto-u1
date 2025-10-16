package model;

import java.io.Serializable;

public abstract class Model implements Serializable {
    protected int id;
    protected String xmlName;

    public abstract String stringifyCSV();
    public abstract String stringifyXML();
    //TODO sql


    public int getId() {
        return id;
    }

    public String getXmlName() {
        return xmlName;
    }
}
