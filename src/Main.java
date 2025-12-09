import presentation.MenuConsola;
import repository.ClaseRepository;
import repository.EstudianteRepository;
import repository.MatriculaRepository;
import repository.ProfesorRepository;
import service.Service;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        // Setting up database
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "");
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE SCHEMA IF NOT EXISTS proyecto_u2");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS estudiantes (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL
                );
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS matriculas (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    estudiante_id INT NOT NULL,
                    nota DOUBLE,
                    fecha VARCHAR(50),
                    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id)
                        ON DELETE CASCADE
                        ON UPDATE CASCADE
                );
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS profesores (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nombre VARCHAR(255),
                    especialidad VARCHAR(255)
                );
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS clases (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    nombre VARCHAR(255),
                    horario VARCHAR(255)
                );
            """);
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS profesor_clase (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    profesor_id INT,
                    clase_id INT,
                    FOREIGN KEY (profesor_id) REFERENCES profesores(id),
                    FOREIGN KEY (clase_id) REFERENCES clases(id)
                );
            """);
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }

        MatriculaRepository matriculaRepository = new MatriculaRepository();
        ProfesorRepository profesorRepository = new ProfesorRepository();
        ClaseRepository claseRepository = new ClaseRepository(profesorRepository);
        profesorRepository.setClaseRepository(claseRepository);
        MenuConsola menu = new MenuConsola(new Service<>(new EstudianteRepository(matriculaRepository)), new Service<>(matriculaRepository), new Service<>(profesorRepository), new Service<>(claseRepository));
        menu.mostrarMenu();
    }
}
