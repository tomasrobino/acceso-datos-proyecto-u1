package repository.sequencial;

import repository.BDInterfaz;

public class MatriculaSecuencial extends Secuencial {
    private static MatriculaSecuencial miEstudianteSequencial;

    private MatriculaSecuencial(String uri) {
        this.uri = uri;
    }

    @Override
    public BDInterfaz get(String uri) {
        if (miEstudianteSequencial == null) {
            miEstudianteSequencial = new MatriculaSecuencial(uri);
        }
        return miEstudianteSequencial;
    }
}
