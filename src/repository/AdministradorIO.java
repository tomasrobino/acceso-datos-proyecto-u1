package repository;

import model.Asignatura;
import model.Estudiante;
import model.Matricula;
import model.Model;

import java.io.File;

public class AdministradorIO implements BDInterfaz {
    private static AdministradorIO instance;
    private final TiposPersistencia tipo;
    private File archivo;
    private BDInterfaz bd;
    private Model modelo;

    private AdministradorIO(TiposPersistencia tipoPersistencia, Model modelo, File archivo) {
        this.tipo = tipoPersistencia;
        this.archivo = archivo;
        this.modelo = modelo;
        switch (tipoPersistencia) {
            case BIN -> bd = new BIN(archivo);
            case CSV -> bd = new CSV(archivo);
            case SQL -> bd = new SQL();
            case XML -> bd = new XML(archivo);
        }
    }

    private AdministradorIO(TiposPersistencia tipo, Model modelo) {
        this.tipo = tipo;
        this.modelo = modelo;
    }

    public static AdministradorIO getInstance(TiposPersistencia tipo, Model modelo, String archivo) {
        if (instance == null) {
            if (tipo == TiposPersistencia.SQL) {
                instance = new AdministradorIO(tipo, modelo);
            } else {
                File file = new File(archivo);
                // error
                if (!file.exists()) return null;

                instance = new AdministradorIO(tipo, modelo, file);
            }
        }
        return instance;
    }

    @Override
    public Model find(int id) {
        return bd.find(id);
    }

    @Override
    public boolean insert(Matricula matricula) {
        return bd.insert(matricula);
    }

    @Override
    public boolean insert(Estudiante estudiante) {
        return bd.insert(estudiante);
    }

    @Override
    public boolean insert(Asignatura asignatura) {
        return bd.insert(asignatura);
    }

    @Override
    public boolean update(Matricula matricula) {
        return bd.update(matricula);
    }

    @Override
    public boolean update(Estudiante estudiante) {
        return bd.update(estudiante);
    }

    @Override
    public boolean update(Asignatura asignatura) {
        return bd.update(asignatura);
    }

    @Override
    public boolean delete(int id) {
        return bd.delete(id);
    }

    public TiposPersistencia getTipo() {
        return tipo;
    }
}
