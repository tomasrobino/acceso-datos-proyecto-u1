package repository;

import model.Asignatura;
import model.Estudiante;
import model.Matricula;
import model.Model;

import java.io.File;

public class AdministradorIO implements BDInterfaz {
    private final TiposPersistencia tipoPersistencia;
    private File archivo;
    private BDInterfaz bd;
    private final Model modelo;

    public AdministradorIO(TiposPersistencia tipoPersistencia, Model modelo, String uri) {
        this.tipoPersistencia = tipoPersistencia;
        this.modelo = modelo;

        if (tipoPersistencia != TiposPersistencia.SQL) {
            archivo = new File(uri);
        }

        switch (tipoPersistencia) {
            case BIN -> bd = new BIN(archivo);
            case CSV -> bd = new CSV(archivo);
            case SQL -> bd = new SQL();
            case XML -> bd = new XML(archivo);
        }
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

    public TiposPersistencia getTipoPersistencia() {
        return tipoPersistencia;
    }
}
