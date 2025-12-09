package presentation;

import model.Clase;
import model.Estudiante;
import model.Matricula;
import model.Profesor;
import service.Service;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuConsola {
    private final Scanner scanner;
    private boolean salir;
    private final Service<Estudiante> estudianteService;
    private final Service<Matricula> matriculaService;
    private final Service<Clase> claseService;
    private final Service<Profesor> profesorService;

    public MenuConsola(Service<Estudiante> estudianteService, Service<Matricula> matriculaService, Service<Profesor> profesorService, Service<Clase> claseService) {
        this.scanner = new Scanner(System.in);
        this.salir = false;
        this.estudianteService = estudianteService;
        this.matriculaService = matriculaService;
        this.profesorService = profesorService;
        this.claseService = claseService;
    }

    public void mostrarMenu() {
        while (!salir) {
            System.out.println("\n========================================");
            System.out.println("       MENÚ PRINCIPAL");
            System.out.println("========================================");
            System.out.println("1. Gestión de Estudiantes");
            System.out.println("2. Gestión de Profesores");
            System.out.println("3. Gestión de Clases");
            System.out.println("4. Gestión de Matrículas");
            System.out.println("5. Salir");
            System.out.println("========================================");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion();
            procesarOpcion(opcion);
        }

        scanner.close();
    }

    private int leerOpcion() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Error: Por favor ingrese un número válido.");
            return -1;
        }
    }

    private void procesarOpcion(int opcion) {
        switch (opcion) {
            case 1:
                menuEstudiantes();
                break;
            case 2:
                menuProfesores();
                break;
            case 3:
                menuClases();
                break;
            case 4:
                menuMatriculas();
                break;
            case 5:
                salir = true;
                break;
            default:
                System.out.println("Opción no válida. Por favor, intente nuevamente.");
        }
    }

    // ==========================================
    // SECCIÓN ESTUDIANTES
    // ==========================================
    private void menuEstudiantes() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n========================================");
            System.out.println("     GESTIÓN DE ESTUDIANTES");
            System.out.println("========================================");
            System.out.println("1. Agregar estudiante");
            System.out.println("2. Listar estudiantes");
            System.out.println("3. Buscar estudiante");
            System.out.println("4. Actualizar estudiante");
            System.out.println("5. Eliminar estudiante");
            System.out.println("6. Volver al menú principal");
            System.out.println("========================================");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion();
            volver = procesarOpcionEstudiantes(opcion);
        }
    }

    private boolean procesarOpcionEstudiantes(int opcion) {
        return switch (opcion) {
            case 1 -> { agregarEstudiante(); yield false; }
            case 2 -> { listarEstudiantes(); yield false; }
            case 3 -> { buscarEstudiante(); yield false; }
            case 4 -> { actualizarEstudiante(); yield false; }
            case 5 -> { eliminarEstudiante(); yield false; }
            case 6 -> true;
            default -> { System.out.println("Opción no válida."); yield false; }
        };
    }

    private void agregarEstudiante() {
        System.out.println("\n--- Agregar Estudiante ---");
        try {
            System.out.print("Ingrese ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Ingrese nombre: ");
            String nombre = scanner.nextLine();
            System.out.print("Ingrese email: ");
            String email = scanner.nextLine();

            // Lógica existente para matrículas
            System.out.println("Ingrese la cantidad de matriculas existentes para asignar:");
            int cantidad = Integer.parseInt(scanner.nextLine());
            ArrayList<Matricula> matriculas = new ArrayList<>();
            for (int i = 0; i < cantidad; i++) {
                System.out.print("Ingrese el ID de la matricula #" + (i+1) + ": ");
                int matriculaId = Integer.parseInt(scanner.nextLine());
                Matricula matricula = matriculaService.buscarPorId(matriculaId);
                if (matricula == null) {
                    System.out.println("✗ Error: ID de matricula no existe.");
                } else {
                    matriculas.add(matricula);
                }
            }

            Estudiante estudiante = new Estudiante(id, nombre, email, matriculas);

            if (estudianteService.crear(estudiante)) {
                System.out.println("✓ Estudiante agregado exitosamente.");
            } else {
                System.out.println("✗ Error al agregar el estudiante.");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void listarEstudiantes() {
        System.out.println("\n--- Listar Estudiantes ---");
        ArrayList<Estudiante> estudiantes = estudianteService.listarTodas();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        for (Estudiante e : estudiantes) {
            System.out.println("ID: " + e.getId() + " | Nombre: " + e.getNombre() + " | Email: " + e.getEmail());
            // Opcional: Mostrar matrículas asociadas si es necesario
        }
    }

    private void buscarEstudiante() {
        System.out.println("\n--- Buscar Estudiante ---");
        try {
            System.out.print("Ingrese ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            Estudiante e = estudianteService.buscarPorId(id);
            if (e != null) {
                System.out.println("Encontrado: " + e.getNombre() + " (" + e.getEmail() + ")");
            } else {
                System.out.println("✗ No encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ ID inválido.");
        }
    }

    private void actualizarEstudiante() {
        System.out.println("\n--- Actualizar Estudiante ---");
        try {
            System.out.print("Ingrese ID del estudiante a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Estudiante e = estudianteService.buscarPorId(id);
            if (e == null) {
                System.out.println("✗ Estudiante no encontrado.");
                return;
            }

            System.out.print("Nuevo nombre [" + e.getNombre() + "]: ");
            String nombre = scanner.nextLine();
            if (nombre.isEmpty()) nombre = e.getNombre();

            System.out.print("Nuevo email [" + e.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (email.isEmpty()) email = e.getEmail();

            // Para simplificar, en actualización pedimos re-asignar matrículas o mantener lista vacía/existente
            // Aquí replicamos la lógica de agregar para actualizar relaciones
            System.out.println("Re-asignar matrículas (ingrese cantidad, 0 para limpiar): ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            ArrayList<Matricula> matriculas = new ArrayList<>();
            for (int i = 0; i < cantidad; i++) {
                System.out.print("ID Matricula #" + (i+1) + ": ");
                int matId = Integer.parseInt(scanner.nextLine());
                Matricula m = matriculaService.buscarPorId(matId);
                if (m != null) matriculas.add(m);
            }

            Estudiante actualizado = new Estudiante(id, nombre, email, matriculas);
            if (estudianteService.actualizar(actualizado)) System.out.println("✓ Actualizado.");
            else System.out.println("✗ Error al actualizar.");

        } catch (Exception ex) {
            System.out.println("✗ Error: " + ex.getMessage());
        }
    }

    private void eliminarEstudiante() {
        System.out.println("\n--- Eliminar Estudiante ---");
        try {
            System.out.print("Ingrese ID: ");
            int id = Integer.parseInt(scanner.nextLine());
            if (estudianteService.eliminar(id)) System.out.println("✓ Eliminado.");
            else System.out.println("✗ Error o no encontrado.");
        } catch (NumberFormatException e) {
            System.out.println("✗ ID inválido.");
        }
    }

    // ==========================================
    // SECCIÓN PROFESORES (NUEVO)
    // ==========================================
    private void menuProfesores() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n========================================");
            System.out.println("     GESTIÓN DE PROFESORES");
            System.out.println("========================================");
            System.out.println("1. Agregar profesor");
            System.out.println("2. Listar profesores");
            System.out.println("3. Buscar profesor");
            System.out.println("4. Actualizar profesor");
            System.out.println("5. Eliminar profesor");
            System.out.println("6. Volver al menú principal");
            System.out.println("========================================");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion();
            volver = procesarOpcionProfesores(opcion);
        }
    }

    private boolean procesarOpcionProfesores(int opcion) {
        return switch (opcion) {
            case 1 -> { agregarProfesor(); yield false; }
            case 2 -> { listarProfesores(); yield false; }
            case 3 -> { buscarProfesor(); yield false; }
            case 4 -> { actualizarProfesor(); yield false; }
            case 5 -> { eliminarProfesor(); yield false; }
            case 6 -> true;
            default -> {
                System.out.println("Opción no válida.");
                yield false;
            }
        };
    }

    private void agregarProfesor() {
        System.out.println("\n--- Agregar Profesor ---");
        try {
            System.out.print("Ingrese ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese especialidad: ");
            String especialidad = scanner.nextLine();

            // Asociación con Clases
            System.out.println("Ingrese la cantidad de clases a asignar a este profesor:");
            int cantidad = Integer.parseInt(scanner.nextLine());
            ArrayList<Clase> clases = new ArrayList<>();
            for (int i = 0; i < cantidad; i++) {
                System.out.print("Ingrese el ID de la clase #" + (i+1) + ": ");
                int claseId = Integer.parseInt(scanner.nextLine());
                Clase clase = claseService.buscarPorId(claseId);
                if (clase == null) {
                    System.out.println("✗ Error: ID de clase no existe.");
                } else {
                    clases.add(clase);
                }
            }

            Profesor profesor = new Profesor(id, nombre, especialidad, clases);

            if (profesorService.crear(profesor)) {
                System.out.println("✓ Profesor agregado exitosamente.");
            } else {
                System.out.println("✗ Error al agregar el profesor.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("✗ Error al agregar profesor: " + e.getMessage());
        }
    }

    private void listarProfesores() {
        System.out.println("\n--- Listar Profesores ---");
        ArrayList<Profesor> profesores = profesorService.listarTodas();

        if (profesores.isEmpty()) {
            System.out.println("No hay profesores registrados.");
            return;
        }

        for (Profesor p : profesores) {
            System.out.println("ID: " + p.getId() + ", Nombre: " + p.getNombre() + ", Especialidad: " + p.getEspecialidad());
            System.out.print("   ↳ Clases asignadas: ");
            if (p.getClases().isEmpty()) {
                System.out.println("Ninguna");
            } else {
                for (Clase c : p.getClases()) {
                    System.out.print("[" + c.getNombre() + "] ");
                }
                System.out.println();
            }
        }
    }

    private void buscarProfesor() {
        System.out.println("\n--- Buscar Profesor ---");
        try {
            System.out.print("Ingrese el ID del profesor: ");
            int id = Integer.parseInt(scanner.nextLine());

            Profesor p = profesorService.buscarPorId(id);

            if (p != null) {
                System.out.println("\n✓ Profesor encontrado:");
                System.out.println("ID: " + p.getId());
                System.out.println("Nombre: " + p.getNombre());
                System.out.println("Especialidad: " + p.getEspecialidad());
                System.out.println("Clases asignadas: " + p.getClases().size());
            } else {
                System.out.println("✗ No se encontró profesor con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        }
    }

    private void actualizarProfesor() {
        System.out.println("\n--- Actualizar Profesor ---");
        try {
            System.out.print("Ingrese el ID del profesor a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Profesor existente = profesorService.buscarPorId(id);
            if (existente == null) {
                System.out.println("✗ No se encontró profesor con ID: " + id);
                return;
            }

            System.out.println("Deje en blanco para mantener el valor actual.");

            System.out.print("Nuevo nombre [" + existente.getNombre() + "]: ");
            String nombre = scanner.nextLine();
            if (nombre.trim().isEmpty()) nombre = existente.getNombre();

            System.out.print("Nueva especialidad [" + existente.getEspecialidad() + "]: ");
            String especialidad = scanner.nextLine();
            if (especialidad.trim().isEmpty()) especialidad = existente.getEspecialidad();

            // Re-asignar clases
            System.out.println("Re-asignar clases (Ingrese cantidad, 0 para dejar sin clases):");
            int cantidad = Integer.parseInt(scanner.nextLine());
            ArrayList<Clase> clases = new ArrayList<>();
            for (int i = 0; i < cantidad; i++) {
                System.out.print("Ingrese el ID de la clase #" + (i+1) + ": ");
                int claseId = Integer.parseInt(scanner.nextLine());
                Clase clase = claseService.buscarPorId(claseId);
                if (clase != null) {
                    clases.add(clase);
                } else {
                    System.out.println("✗ Clase no encontrada, omitida.");
                }
            }

            Profesor actualizado = new Profesor(id, nombre, especialidad, clases);

            if (profesorService.actualizar(actualizado)) {
                System.out.println("✓ Profesor actualizado exitosamente.");
            } else {
                System.out.println("✗ Error al actualizar el profesor.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void eliminarProfesor() {
        System.out.println("\n--- Eliminar Profesor ---");
        try {
            System.out.print("Ingrese el ID del profesor a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Profesor p = profesorService.buscarPorId(id);
            if (p == null) {
                System.out.println("✗ No se encontró profesor con ID: " + id);
                return;
            }

            System.out.println("¿Eliminar a " + p.getNombre() + "? (SI/NO): ");
            if (scanner.nextLine().equalsIgnoreCase("SI")) {
                if (profesorService.eliminar(id)) System.out.println("✓ Profesor eliminado.");
                else System.out.println("✗ Error al eliminar.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        }
    }

    // ==========================================
    // SECCIÓN CLASES (NUEVO)
    // ==========================================
    private void menuClases() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n========================================");
            System.out.println("     GESTIÓN DE CLASES");
            System.out.println("========================================");
            System.out.println("1. Agregar clase");
            System.out.println("2. Listar clases");
            System.out.println("3. Buscar clase");
            System.out.println("4. Actualizar clase");
            System.out.println("5. Eliminar clase");
            System.out.println("6. Volver al menú principal");
            System.out.println("========================================");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion();
            volver = procesarOpcionClases(opcion);
        }
    }

    private boolean procesarOpcionClases(int opcion) {
        return switch (opcion) {
            case 1 -> { agregarClase(); yield false; }
            case 2 -> { listarClases(); yield false; }
            case 3 -> { buscarClase(); yield false; }
            case 4 -> { actualizarClase(); yield false; }
            case 5 -> { eliminarClase(); yield false; }
            case 6 -> true;
            default -> {
                System.out.println("Opción no válida.");
                yield false;
            }
        };
    }

    private void agregarClase() {
        System.out.println("\n--- Agregar Clase ---");
        try {
            System.out.print("Ingrese ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese nombre de la clase: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese horario: ");
            String horario = scanner.nextLine();

            // Asociación con Profesores
            System.out.println("Ingrese la cantidad de profesores a asignar:");
            int cantidad = Integer.parseInt(scanner.nextLine());
            ArrayList<Profesor> profesores = new ArrayList<>();
            for (int i = 0; i < cantidad; i++) {
                System.out.print("Ingrese el ID del profesor #" + (i+1) + ": ");
                int profId = Integer.parseInt(scanner.nextLine());
                Profesor prof = profesorService.buscarPorId(profId);
                if (prof == null) {
                    System.out.println("✗ Error: ID de profesor no existe.");
                } else {
                    profesores.add(prof);
                }
            }

            Clase clase = new Clase(id, nombre, horario, profesores);

            if (claseService.crear(clase)) {
                System.out.println("✓ Clase agregada exitosamente.");
            } else {
                System.out.println("✗ Error al agregar la clase.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("✗ Error al agregar clase: " + e.getMessage());
        }
    }

    private void listarClases() {
        System.out.println("\n--- Listar Clases ---");
        ArrayList<Clase> clases = claseService.listarTodas();

        if (clases.isEmpty()) {
            System.out.println("No hay clases registradas.");
            return;
        }

        for (Clase c : clases) {
            System.out.println("ID: " + c.getId() + ", Nombre: " + c.getNombre() + ", Horario: " + c.getHorario());
            System.out.print("   ↳ Profesores: ");
            if (c.getProfesores().isEmpty()) {
                System.out.println("Ninguno");
            } else {
                for (Profesor p : c.getProfesores()) {
                    System.out.print("[" + p.getNombre() + "] ");
                }
                System.out.println();
            }
        }
    }

    private void buscarClase() {
        System.out.println("\n--- Buscar Clase ---");
        try {
            System.out.print("Ingrese el ID de la clase: ");
            int id = Integer.parseInt(scanner.nextLine());

            Clase c = claseService.buscarPorId(id);

            if (c != null) {
                System.out.println("\n✓ Clase encontrada:");
                System.out.println("ID: " + c.getId());
                System.out.println("Nombre: " + c.getNombre());
                System.out.println("Horario: " + c.getHorario());
                System.out.println("Profesores asignados: " + c.getProfesores().size());
            } else {
                System.out.println("✗ No se encontró clase con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        }
    }

    private void actualizarClase() {
        System.out.println("\n--- Actualizar Clase ---");
        try {
            System.out.print("Ingrese el ID de la clase a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Clase existente = claseService.buscarPorId(id);
            if (existente == null) {
                System.out.println("✗ No se encontró clase con ID: " + id);
                return;
            }

            System.out.println("Deje en blanco para mantener el valor actual.");

            System.out.print("Nuevo nombre [" + existente.getNombre() + "]: ");
            String nombre = scanner.nextLine();
            if (nombre.trim().isEmpty()) nombre = existente.getNombre();

            System.out.print("Nuevo horario [" + existente.getHorario() + "]: ");
            String horario = scanner.nextLine();
            if (horario.trim().isEmpty()) horario = existente.getHorario();

            // Re-asignar profesores
            System.out.println("Re-asignar profesores (Ingrese cantidad, 0 para dejar sin profesores):");
            int cantidad = Integer.parseInt(scanner.nextLine());
            ArrayList<Profesor> profesores = new ArrayList<>();
            for (int i = 0; i < cantidad; i++) {
                System.out.print("Ingrese el ID del profesor #" + (i+1) + ": ");
                int profId = Integer.parseInt(scanner.nextLine());
                Profesor prof = profesorService.buscarPorId(profId);
                if (prof != null) {
                    profesores.add(prof);
                } else {
                    System.out.println("✗ Profesor no encontrado, omitido.");
                }
            }

            Clase actualizada = new Clase(id, nombre, horario, profesores);

            if (claseService.actualizar(actualizada)) {
                System.out.println("✓ Clase actualizada exitosamente.");
            } else {
                System.out.println("✗ Error al actualizar la clase.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }

    private void eliminarClase() {
        System.out.println("\n--- Eliminar Clase ---");
        try {
            System.out.print("Ingrese el ID de la clase a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Clase c = claseService.buscarPorId(id);
            if (c == null) {
                System.out.println("✗ No se encontró clase con ID: " + id);
                return;
            }

            System.out.println("¿Eliminar la clase " + c.getNombre() + "? (SI/NO): ");
            if (scanner.nextLine().equalsIgnoreCase("SI")) {
                if (claseService.eliminar(id)) System.out.println("✓ Clase eliminada.");
                else System.out.println("✗ Error al eliminar.");
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        }
    }

    // ==========================================
    // SECCIÓN MATRÍCULAS
    // ==========================================
    private void menuMatriculas() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n========================================");
            System.out.println("     GESTIÓN DE MATRÍCULAS");
            System.out.println("========================================");
            System.out.println("1. Agregar matrícula");
            System.out.println("2. Listar matrículas");
            System.out.println("3. Buscar matrícula");
            System.out.println("4. Actualizar matrícula");
            System.out.println("5. Eliminar matrícula");
            System.out.println("6. Volver al menú principal");
            System.out.println("========================================");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion();
            volver = procesarOpcionMatriculas(opcion);
        }
    }

    private boolean procesarOpcionMatriculas(int opcion) {
        return switch (opcion) {
            case 1 -> { agregarMatricula(); yield false; }
            case 2 -> { listarMatriculas(); yield false; }
            case 3 -> { buscarMatricula(); yield false; }
            case 4 -> { actualizarMatricula(); yield false; }
            case 5 -> { eliminarMatricula(); yield false; }
            case 6 -> true;
            default -> {
                System.out.println("Opción no válida.");
                yield false;
            }
        };
    }

    private void agregarMatricula() {
        System.out.println("\n--- Agregar Matrícula ---");
        try {
            System.out.print("Ingrese ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese nota: ");
            double nota = Double.parseDouble(scanner.nextLine());

            System.out.print("Ingrese fecha de matrícula (dd/MM/yyyy): ");
            String fechaStr = scanner.nextLine();

            System.out.print("Ingrese ID del estudiante al que corresponde: ");
            int estudiante_id = Integer.parseInt(scanner.nextLine());

            Matricula matricula = new Matricula(id, nota, fechaStr, estudiante_id);

            if (matriculaService.crear(matricula)) {
                System.out.println("✓ Matrícula agregada exitosamente.");
            } else {
                System.out.println("✗ Error al agregar la matrícula.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: Los IDs y notas deben ser números válidos.");
        } catch (DateTimeParseException e) {
            System.out.println("✗ Error: Fecha inválida. Use el formato dd/MM/yyyy.");
        } catch (Exception e) {
            System.out.println("✗ Error al agregar matrícula: " + e.getMessage());
        }
    }

    private void listarMatriculas() {
        System.out.println("\n--- Listar Matrículas ---");
        ArrayList<Matricula> matriculas = matriculaService.listarTodas();

        if (matriculas.isEmpty()) {
            System.out.println("No hay matrículas registradas.");
            return;
        }
        for (Matricula matricula : matriculas) {
            System.out.println("Id: "+matricula.getId()+" | Nota: "+matricula.getNota()+" | Fecha: "+matricula.getFecha()+ " | ID Estudiante: "+matricula.getEstudiante_id());
        }
    }

    private void buscarMatricula() {
        System.out.println("\n--- Buscar Matrícula ---");
        try {
            System.out.print("Ingrese el ID de la matrícula: ");
            int id = Integer.parseInt(scanner.nextLine());

            Matricula matricula = matriculaService.buscarPorId(id);

            if (matricula != null) {
                System.out.println("\n✓ Matrícula encontrada:");
                System.out.println("ID: " + matricula.getId());
                System.out.println("Nota: " + matricula.getNota());
                System.out.println("Fecha: " + matricula.getFecha());
                System.out.println("ID del estudiante: " + matricula.getEstudiante_id());
            } else {
                System.out.println("✗ No se encontró matrícula con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        }
    }

    private void actualizarMatricula() {
        System.out.println("\n--- Actualizar Matrícula ---");
        try {
            System.out.print("Ingrese el ID de la matrícula a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Matricula existente = matriculaService.buscarPorId(id);
            if (existente == null) {
                System.out.println("✗ No se encontró matrícula con ID: " + id);
                return;
            }

            System.out.print("Nueva nota de matrícula: ");
            double nota = Double.parseDouble(scanner.nextLine());

            System.out.print("Nueva fecha de matrícula: ");
            String fecha = scanner.nextLine();

            System.out.print("Nuevo id del estudiante: ");
            int estudiante_id = Integer.parseInt(scanner.nextLine());

            Matricula actualizada = new Matricula(id, nota, fecha, estudiante_id);

            if (matriculaService.actualizar(actualizada)) {
                System.out.println("✓ Matrícula actualizada exitosamente.");
            } else {
                System.out.println("✗ Error al actualizar la matrícula.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: Los IDs deben ser números válidos.");
        } catch (DateTimeParseException e) {
            System.out.println("✗ Error: Fecha inválida. Use el formato dd/MM/yyyy.");
        } catch (Exception e) {
            System.out.println("✗ Error al actualizar matrícula: " + e.getMessage());
        }
    }

    private void eliminarMatricula() {
        System.out.println("\n--- Eliminar Matrícula ---");
        try {
            System.out.print("Ingrese el ID de la matrícula a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Matricula matricula = matriculaService.buscarPorId(id);
            if (matricula == null) {
                System.out.println("✗ No se encontró matrícula con ID: " + id);
                return;
            }

            System.out.println("\n¿Está seguro que desea eliminar esta matrícula? (SI/NO)");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("SI")) {
                if (matriculaService.eliminar(id)) {
                    System.out.println("✓ Matrícula eliminada exitosamente.");
                } else {
                    System.out.println("✗ Error al eliminar la matrícula.");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        }
    }
}