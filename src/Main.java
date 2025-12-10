import model.Clase;
import model.Estudiante;
import model.Matricula;
import model.Profesor;
import repository.ClaseRepository;
import repository.EstudianteRepository;
import repository.MatriculaRepository;
import repository.ProfesorRepository;
import service.Service;

import java.io.*;
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

        try {
            estudianteService.crear(new Estudiante("aaa", "bbb", new FileInputStream("foto1.jpg"), new ArrayList<>(matriculaList)));
            estudianteService.crear(new Estudiante("ccc", "ddd", new FileInputStream("foto2.jpg"), new ArrayList<>(matriculaList2)));
        } catch (FileNotFoundException e) {
            estudianteService.crear(new Estudiante("aaa", "bbb", null, new ArrayList<>(matriculaList)));
            estudianteService.crear(new Estudiante("ccc", "ddd", null, new ArrayList<>(matriculaList2)));
        }

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


        // BLOB

        try {
            // Intenta cargar una imagen desde el sistema de archivos
            InputStream imagenEstudiante = new FileInputStream("student_photo.jpg");

            Estudiante estudianteConFoto = new Estudiante(
                    "Juan Pérez",
                    "juan.perez@universidad.com",
                    imagenEstudiante,
                    new ArrayList<>(Arrays.asList(
                            new Matricula(9.5, "2024-01-15"),
                            new Matricula(8.7, "2024-02-20")
                    ))
            );

            estudianteService.crear(estudianteConFoto);
            System.out.println("✓ Estudiante con foto creado exitosamente");

        } catch (FileNotFoundException e) {
            System.out.println("⚠ Imagen 'student_photo.jpg' no encontrada");
            System.out.println("  Puedes colocar una imagen JPG en la raíz del proyecto con ese nombre");

            // Alternativa: crear estudiante sin foto
            Estudiante estudianteSinFoto = new Estudiante(
                    "Juan Pérez",
                    "juan.perez@universidad.com",
                    null,
                    new ArrayList<>(Arrays.asList(
                            new Matricula(9.5, "2024-01-15"),
                            new Matricula(8.7, "2024-02-20")
                    ))
            );
            estudianteService.crear(estudianteSinFoto);
            System.out.println("✓ Estudiante sin foto creado exitosamente");
        }

        // 2. Recuperar estudiante y verificar que tiene foto
        ArrayList<Estudiante> todosEstudiantes = estudianteService.listarTodas();
        Estudiante ultimoEstudiante = todosEstudiantes.getLast();

        System.out.println("\n✓ Estudiante recuperado: " + ultimoEstudiante.getNombre());
        System.out.println("  Email: " + ultimoEstudiante.getEmail());

        if (ultimoEstudiante.getFoto() != null) {
            System.out.println("  Foto: InputStream disponible");

            // 3. Guardar la imagen recuperada en un archivo (demostración)
            try (InputStream fotoStream = ultimoEstudiante.getFoto();
                 FileOutputStream fos = new FileOutputStream("foto_recuperada.jpg")) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fotoStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }

                System.out.println("  ✓ Foto guardada como 'foto_recuperada.jpg'");

            } catch (IOException e) {
                System.out.println("  ⚠ Error al guardar la foto: " + e.getMessage());
            }
        } else {
            System.out.println("  Foto: No disponible (null)");
        }

        // 4. Actualizar estudiante con nueva foto
        try {
            InputStream nuevaFoto = new FileInputStream("student_photo_updated.jpg");

            Estudiante estudianteActualizar = new Estudiante(
                    ultimoEstudiante.getId(),
                    ultimoEstudiante.getNombre(),
                    "nuevo.email@universidad.com",
                    nuevaFoto,
                    ultimoEstudiante.getMatriculas()
            );

            estudianteService.actualizar(estudianteActualizar);
            System.out.println("\n✓ Estudiante actualizado con nueva foto");

        } catch (FileNotFoundException e) {
            System.out.println("\n⚠ Imagen 'student_photo_updated.jpg' no encontrada para actualización");
            System.out.println("  La actualización de foto se omitió");
        }

        // 5. Demostración con múltiples estudiantes e imágenes
        System.out.println("\n=== Creando múltiples estudiantes con fotos ===");

        String[] nombres = {"María González", "Carlos Rodríguez", "Ana Martínez"};
        String[] emails = {"maria@uni.com", "carlos@uni.com", "ana@uni.com"};
        String[] fotos = {"foto_maria.jpg", "foto_carlos.jpg", "foto_ana.jpg"};

        for (int i = 0; i < nombres.length; i++) {
            try {
                InputStream foto = new FileInputStream(fotos[i]);
                Estudiante est = new Estudiante(
                        nombres[i],
                        emails[i],
                        foto,
                        new ArrayList<>(List.of(new Matricula(8.0 + i, "2024-03-" + (10 + i))))
                );
                estudianteService.crear(est);
                System.out.println("✓ " + nombres[i] + " - con foto");
            } catch (FileNotFoundException e) {
                Estudiante est = new Estudiante(
                        nombres[i],
                        emails[i],
                        null,
                        new ArrayList<>(List.of(new Matricula(8.0 + i, "2024-03-" + (10 + i))))
                );
                estudianteService.crear(est);
                System.out.println("✓ " + nombres[i] + " - sin foto (archivo no encontrado)");
            }
        }
    }
}