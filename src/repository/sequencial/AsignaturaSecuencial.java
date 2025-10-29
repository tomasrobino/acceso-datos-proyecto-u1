package repository.sequencial;

import repository.BDInterfaz;

public class AsignaturaSecuencial extends Secuencial {
    private static AsignaturaSecuencial miEstudianteSequencial;

    private AsignaturaSecuencial(String uri) {
        this.uri = uri;
    }

    @Override
    public BDInterfaz get(String uri) {
        if (miEstudianteSequencial == null) {
            miEstudianteSequencial = new AsignaturaSecuencial(uri);
        }
        return miEstudianteSequencial;
    }
}
