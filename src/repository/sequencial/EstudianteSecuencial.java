package repository.sequencial;

import repository.BDInterfaz;

public class EstudianteSecuencial extends Secuencial {
    private static EstudianteSecuencial miEstudianteSecuencial;

    private EstudianteSecuencial(String uri) {
        this.uri = uri;
    }

    @Override
    public BDInterfaz get(String uri) {
        if (miEstudianteSecuencial == null) {
            miEstudianteSecuencial = new EstudianteSecuencial(uri);
        }
        return miEstudianteSecuencial;
    }
}
