package repository;

import model.Model;

import java.io.*;

public class BIN extends BDInterfaz{
    private static BIN miBIN;

    private BIN(String uri) {
        this.uri = uri;
    }

    @Override
    BDInterfaz get(String uri) {
        if (miBIN == null) {
            miBIN = new BIN(uri);
        }
        return miBIN;
    }

    @Override
    String find(int id) {
        try {
            FileInputStream fis = new FileInputStream(uri);
            ObjectInputStream in = new ObjectInputStream(fis);
            Model modelo;
            while ( (modelo = (Model)in.readObject()) != null) {
                if (modelo.getId() == id) {
                    return modelo;
                }
            }
            in.close();
            fis.close();
            return null;
        } catch(ClassNotFoundException | IOException e) {
            return null;
        }
    }

    @Override
    boolean insert(Model model) {
        return false;
    }

    @Override
    boolean update(Model model) {
        return false;
    }

    @Override
    boolean delete(int id) {
        return false;
    }
}
