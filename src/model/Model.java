package model;

import java.io.Serializable;

public interface Model extends Serializable {
    String stringifyCSV();
    String stringifyXML();
    //TODO sql
}
