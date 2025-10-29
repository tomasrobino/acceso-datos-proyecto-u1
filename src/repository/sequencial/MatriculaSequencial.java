package repository.sequencial;

import model.Model;
import repository.BDInterfaz;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MatriculaSequencial extends BDInterfaz {
    private static MatriculaSequencial miEstudianteSequencial;

    private MatriculaSequencial(String uri) {
        this.uri = uri;
    }

    @Override
    public BDInterfaz get(String uri) {
        if (miEstudianteSequencial == null) {
            miEstudianteSequencial = new MatriculaSequencial(uri);
        }
        return miEstudianteSequencial;
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
            while ( (modelo = (Model)in.readObject()) != null) {
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
        return false;
    }

    @Override
    public boolean update(Model model) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
