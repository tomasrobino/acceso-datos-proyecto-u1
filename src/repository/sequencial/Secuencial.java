package repository.sequencial;

import model.Model;
import repository.BDInterfaz;

import java.io.*;
import java.util.ArrayList;

abstract class Secuencial extends BDInterfaz {
    @Override
    public BDInterfaz get(String uri) {
        return null;
    }

    @Override
    public Model find(int id) {
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
    public ArrayList<Model> findAll() {
        try {
            FileInputStream fis = new FileInputStream(uri);
            ObjectInputStream in = new ObjectInputStream(fis);
            Model modelo;
            ArrayList<Model> lista = new ArrayList<>();
            while ( (modelo = (Model) in.readObject()) != null) {
                lista.add(modelo);
            }
            in.close();
            fis.close();
            return lista;
        } catch(ClassNotFoundException | IOException e) {
            return null;
        }
    }

    @Override
    public boolean insert(Model model) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(uri);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(model);
            out.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Model model) {
        FileOutputStream fos;
        try {
            ArrayList<Model> lista = findAll();

            fos = new FileOutputStream(uri);
            ObjectOutputStream out = new ObjectOutputStream(fos);

            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId() == model.getId()) {
                    lista.set(i, model);
                }
                out.writeObject(lista.get(i));
            }
            out.close();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
