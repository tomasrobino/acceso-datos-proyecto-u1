import model.Estudiante;
import model.Matricula;
import presentation.MenuConsola;
import repository.Database;
import service.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        // Read persistence type from config.properties
        Properties config = new Properties();
        String persistenceType;
        
        try (FileReader reader = new FileReader("src/config.properties")) {
            config.load(reader);
            persistenceType = config.getProperty("persistencia", "binary");
            System.out.println("Using persistence type: " + persistenceType);
        } catch (IOException e) {
            System.err.println("Warning: Could not read config.properties. Using default persistence type (binary).");
            System.err.println("Error: " + e.getMessage());
        }

        File data = new File("src/data");
        if (!data.exists()) {
            data.mkdir();
        }

        MenuConsola menu = new MenuConsola(new Service<Estudiante>(new Database<Estudiante>("")), new Service<Matricula>(new Database<Matricula>("")));
        menu.mostrarMenu();
    }
}