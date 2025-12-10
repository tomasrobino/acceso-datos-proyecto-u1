import model.Clase;
import model.Estudiante;
import model.Matricula;
import model.Profesor;
import repository.ClaseRepository;
import repository.EstudianteRepository;
import repository.MatriculaRepository;
import repository.ProfesorRepository;
import service.Service;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Setting up database
        try (Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost:3306", "root", "");
             Statement stmt = conn.createStatement()) {

            stmt.execute("CREATE SCHEMA IF NOT EXISTS proyecto_u2");
            stmt.execute("USE proyecto_u2");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS estudiantes (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(255) NOT NULL,
                    email VARCHAR(255) NOT NULL,
                    foto BLOB
                );
            """);
            // Uso ON DELETE CASCADE porque no puede haber matriculas que no correspondan a ningun estudiante,
            // por lo que si se elimina un estudiante, se deben eliminar también sus matrículas
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

        // Creating repositories
        MatriculaRepository matriculaRepository = new MatriculaRepository();
        ProfesorRepository profesorRepository = new ProfesorRepository();
        ClaseRepository claseRepository = new ClaseRepository(profesorRepository);
        profesorRepository.setClaseRepository(claseRepository);

        // Creating services
        Service<Matricula> matriculaService = new Service<>(matriculaRepository);
        Service<Estudiante> estudianteService = new Service<>(new EstudianteRepository(matriculaRepository));
        Service<Profesor> profesorService = new Service<>(profesorRepository);
        Service<Clase> claseService = new Service<>(claseRepository);

        List<Matricula> matriculaList = Arrays.asList(
                new Matricula(1.0, "2022-01-01"),
                new Matricula(2.0, "2022-01-02"),
                new Matricula(3.0, "2022-01-03")
        );

        List<Matricula> matriculaList2 = Arrays.asList(
                new Matricula(4.0, "2022-01-01"),
                new Matricula(5.0, "2022-01-02"),
                new Matricula(6.0, "2022-01-03")
        );

        estudianteService.crear(new Estudiante("aaa", "bbb", "xxxx".getBytes(StandardCharsets.UTF_8), new ArrayList<>(matriculaList)));
        estudianteService.crear(new Estudiante("ccc", "ddd", "yyyy".getBytes(StandardCharsets.UTF_8), new ArrayList<>(matriculaList)));
        ArrayList<Estudiante> estudianteList = estudianteService.listarTodas();
        System.out.println(estudianteList);
    }
}
