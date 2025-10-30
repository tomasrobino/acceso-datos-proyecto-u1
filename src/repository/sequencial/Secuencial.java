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
        ArrayList<Model> lista = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(uri);
            ObjectInputStream in = new ObjectInputStream(fis);
            while (true) {
                try {
                    Model modelo = (Model) in.readObject();
                    lista.add(modelo);
                } catch (EOFException e) {
                    in.close();
                    fis.close();
                    return lista;
                }
            }
        } catch(ClassNotFoundException | IOException e) {
            return lista;
        }
    }

    @Override
    public boolean insert(Model model) {
        FileOutputStream fos;
        try {
            ArrayList<Model> lista = findAll();
            lista.add(model);
            fos = new FileOutputStream(uri);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            for (Model value : lista) {
                out.writeObject(value);
            }
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
        FileOutputStream fos;
        try {
            ArrayList<Model> lista = findAll();

            fos = new FileOutputStream(uri);
            ObjectOutputStream out = new ObjectOutputStream(fos);

            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).getId() != id) {
                    out.writeObject(lista.get(i));
                }
            }
            out.close();
            fos.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
