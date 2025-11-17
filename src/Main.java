import model.Estudiante;
import model.Matricula;
import presentation.MenuConsola;
import repository.Database;
import service.EstudianteService;
import service.MatriculaService;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        // Read persistence type from config.properties
        Properties config = new Properties();
        String persistenceType = "binary";
        
        try (FileReader reader = new FileReader("src/config.properties")) {
            config.load(reader);
            persistenceType = config.getProperty("persistencia", "binary");
            System.out.println("Using persistence type: " + persistenceType);
        } catch (IOException e) {
            System.err.println("Warning: Could not read config.properties. Using default persistence type (binary).");
            System.err.println("Error: " + e.getMessage());
        }
        EstudianteService es;
        MatriculaService ms;

        File data = new File("src/data");
        if (!data.exists()) {
            data.mkdir();
        }

        try {
            File file = new File("src/data/estudiantes.bin");
            if (!file.exists()) {
                file.createNewFile();
            }
            file = new File("src/data/asignaturas.bin");
            if (!file.exists()) {
                file.createNewFile();
            }
            file = new File("src/data/matriculas.bin");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Database<Matricula> dm = new Database<Matricula>("");
        es = new EstudianteService(new Database<Estudiante>(""), dm);
        ms = new MatriculaService(dm);

        MenuConsola menu = new MenuConsola(es, ms);
        menu.mostrarMenu();
    }
}