import model.Clase;
import model.Estudiante;
import model.Matricula;
import model.Profesor;
import repository.ClaseRepository;
import repository.EstudianteRepository;
import repository.MatriculaRepository;
import repository.ProfesorRepository;
import service.Service;

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

        // NO PONGO PRINTS PORQUE SE VE MEJOR CON EL DEBUGGER



        // Estudiante y Matricula
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
        estudianteService.crear(new Estudiante("ccc", "ddd", "yyyy".getBytes(StandardCharsets.UTF_8), new ArrayList<>(matriculaList2)));
        ArrayList<Estudiante> estudianteList = estudianteService.listarTodas();
        int primerEstudianteId = estudianteList.getFirst().getId();
        matriculaService.crear(new Matricula(7.0, "2022-01-01", primerEstudianteId));
        estudianteList = estudianteService.listarTodas();
        Matricula primeraMatricula = estudianteService.buscarPorId(primerEstudianteId).getMatriculas().getFirst();
        matriculaService.actualizar(new Matricula(primeraMatricula.getId(), 8.0, "sfdsfdsffsd", primeraMatricula.getEstudiante_id()));
        primeraMatricula = matriculaService.buscarPorId(primeraMatricula.getId());
        System.out.println(matriculaService.listarTodas());
        estudianteService.eliminar(primerEstudianteId);
        System.out.println(estudianteService.listarTodas());
        System.out.println(matriculaService.listarTodas());


        // Profesor y Clase

        // Crear clases primero (sin profesores asignados aún)
        claseService.crear(new Clase("Matemáticas", "Lunes 9:00-11:00", new ArrayList<>()));
        claseService.crear(new Clase("Física", "Martes 10:00-12:00", new ArrayList<>()));
        claseService.crear(new Clase("Química", "Miércoles 8:00-10:00", new ArrayList<>()));

        // Obtener las clases creadas
        ArrayList<Clase> clasesList = claseService.listarTodas();
        Clase claseMatematicas = clasesList.get(0);
        Clase claseFisica = clasesList.get(1);
        Clase claseQuimica = clasesList.get(2);

        // Crear profesores con sus clases asignadas
        List<Clase> clasesProfesor1 = Arrays.asList(claseMatematicas, claseFisica);
        List<Clase> clasesProfesor2 = Arrays.asList(claseFisica, claseQuimica);

        profesorService.crear(new Profesor("Dr. García", "Matemáticas", new ArrayList<>(clasesProfesor1)));
        profesorService.crear(new Profesor("Dra. López", "Física", new ArrayList<>(clasesProfesor2)));

        // Listar todos los profesores
        ArrayList<Profesor> profesoresList = profesorService.listarTodas();
        int primerProfesorId = profesoresList.getFirst().getId();

        // Buscar un profesor específico y ver sus clases
        Profesor profesorBuscado = profesorService.buscarPorId(primerProfesorId);
        System.out.println("Profesor encontrado: " + profesorBuscado.getNombre());

        // Actualizar un profesor (cambiar especialidad y reasignar clases)
        Profesor profesorActualizar = profesoresList.getFirst();
        profesorActualizar.setEspecialidad("Matemáticas Avanzadas");
        profesorActualizar.setClases(new ArrayList<>(Arrays.asList(claseMatematicas, claseQuimica)));
        profesorService.actualizar(profesorActualizar);

        // Verificar la actualización
        Profesor profesorActualizado = profesorService.buscarPorId(primerProfesorId);
        System.out.println("Especialidad actualizada: " + profesorActualizado.getEspecialidad());

        // Actualizar una clase (cambiar horario y reasignar profesores)
        Clase claseActualizar = claseService.buscarPorId(claseMatematicas.getId());
        claseActualizar.setHorario("Lunes 14:00-16:00");
        ArrayList<Profesor> nuevosProfesores = new ArrayList<>();
        nuevosProfesores.add(profesoresList.get(1)); // Solo la Dra. López
        claseActualizar.setProfesores(nuevosProfesores);
        claseService.actualizar(claseActualizar);

        // Verificar las clases
        System.out.println("Todas las clases:");
        System.out.println(claseService.listarTodas());

        // Eliminar un profesor (esto también eliminará sus relaciones en profesor_clase)
        profesorService.eliminar(profesoresList.get(1).getId());

        // Verificar después de eliminar
        System.out.println("Profesores después de eliminar:");
        System.out.println(profesorService.listarTodas());

        // Verificar que las clases siguen existiendo pero sin ese profesor
        System.out.println("Clases después de eliminar profesor:");
        System.out.println(claseService.listarTodas());
    }
}
