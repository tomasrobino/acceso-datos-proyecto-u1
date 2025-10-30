import presentation.MenuConsola;
import repository.csv.AsignaturaCSV;
import repository.csv.EstudianteCSV;
import repository.csv.MatriculaCSV;
import repository.sequencial.AsignaturaSecuencial;
import repository.sequencial.EstudianteSecuencial;
import repository.sequencial.MatriculaSecuencial;
import repository.xml.AsignaturaXML;
import repository.xml.EstudianteXML;
import repository.xml.MatriculaXML;
import service.AsignaturaService;
import service.EstudianteService;
import service.MatriculaService;

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
            persistenceType = config.getProperty("persistence.type", "binary");
            System.out.println("Using persistence type: " + persistenceType);
        } catch (IOException e) {
            System.err.println("Warning: Could not read config.properties. Using default persistence type (binary).");
            System.err.println("Error: " + e.getMessage());
        }
        EstudianteService es;
        AsignaturaService as;
        MatriculaService ms;

        switch (persistenceType) {
            case "binary":
                es = new EstudianteService(new EstudianteSecuencial("src/data/estudiantes.bin"));
                as = new AsignaturaService(new AsignaturaSecuencial("src/data/asignaturas.bin"));
                ms = new MatriculaService(new MatriculaSecuencial("src/data/matriculas.bin"));
                break;
            case "csv":
                es = new EstudianteService(new EstudianteCSV("src/data/estudiantes.csv"));
                as = new AsignaturaService(new AsignaturaCSV("src/data/asignaturas.csv"));
                ms = new MatriculaService(new MatriculaCSV("src/data/matriculas.csv"));
                break;
            case "xml":
                es = new EstudianteService(new EstudianteXML("src/data/estudiantes.xml"));
                as = new AsignaturaService(new AsignaturaXML("src/data/asignaturas.xml"));
                ms = new MatriculaService(new MatriculaXML("src/data/matriculas.xml"));
                break;
            default:
                throw new IllegalArgumentException("Invalid persistence type: " + persistenceType);
        }

        MenuConsola menu = new MenuConsola(es, as, ms);
        menu.mostrarMenu();
    }
}