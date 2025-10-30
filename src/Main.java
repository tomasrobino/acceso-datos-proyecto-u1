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
            persistenceType = config.getProperty("persistence.type", "binary");
            System.out.println("Using persistence type: " + persistenceType);
        } catch (IOException e) {
            System.err.println("Warning: Could not read config.properties. Using default persistence type (binary).");
            System.err.println("Error: " + e.getMessage());
        }
        EstudianteService es;
        AsignaturaService as;
        MatriculaService ms;

        File data = new File("src/data");
        if (!data.exists()) {
            data.mkdir();
        }

        switch (persistenceType) {
            case "binary":
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
                es = new EstudianteService(new EstudianteSecuencial("src/data/estudiantes.bin"));
                as = new AsignaturaService(new AsignaturaSecuencial("src/data/asignaturas.bin"));
                ms = new MatriculaService(new MatriculaSecuencial("src/data/matriculas.bin"));

                break;
            case "csv":
                try {
                    File file = new File("src/data/estudiantes.csv");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    file = new File("src/data/asignaturas.csv");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    file = new File("src/data/matriculas.csv");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                es = new EstudianteService(new EstudianteCSV("src/data/estudiantes.csv"));
                as = new AsignaturaService(new AsignaturaCSV("src/data/asignaturas.csv"));
                ms = new MatriculaService(new MatriculaCSV("src/data/matriculas.csv"));
                break;
            case "xml":
                try {
                    File file = new File("src/data/estudiantes.xml");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    file = new File("src/data/asignaturas.xml");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                    file = new File("src/data/matriculas.xml");
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
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