package presentation;

import model.Asignatura;
import model.Estudiante;
import model.Matricula;
import service.AsignaturaService;
import service.EstudianteService;
import service.MatriculaService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuConsola {
    private final Scanner scanner;
    private boolean salir;
    private final EstudianteService estudianteService;
    private final AsignaturaService asignaturaService;
    private final MatriculaService matriculaService;

    public MenuConsola(EstudianteService estudianteService, AsignaturaService asignaturaService, MatriculaService matriculaService) {
        this.scanner = new Scanner(System.in);
        this.salir = false;
        this.estudianteService = estudianteService;
        this.asignaturaService = asignaturaService;
        this.matriculaService = matriculaService;
    }

    public void mostrarMenu() {
        while (!salir) {
            System.out.println("\n========================================");
            System.out.println("       MENÚ PRINCIPAL");
            System.out.println("========================================");
            System.out.println("1. Gestión de Estudiantes");
            System.out.println("2. Gestión de Asignaturas");
            System.out.println("3. Gestión de Matrículas");
            System.out.println("4. Salir");
            System.out.println("========================================");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion();
            procesarOpcion(opcion);
        }
        
        cerrar();
    }

    private int leerOpcion() {
        try {
            return Integer.parseInt(scanner.nextLine());
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
                menuAsignaturas();
                break;
            case 3:
                menuMatriculas();
                break;
            case 4:
                salir();
                break;
            default:
                System.out.println("Opción no válida. Por favor, intente nuevamente.");
        }
    }

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
            case 1 -> {
                agregarEstudiante();
                yield false;
            }
            case 2 -> {
                listarEstudiantes();
                yield false;
            }
            case 3 -> {
                buscarEstudiante();
                yield false;
            }
            case 4 -> {
                actualizarEstudiante();
                yield false;
            }
            case 5 -> {
                eliminarEstudiante();
                yield false;
            }
            case 6 -> true;
            default -> {
                System.out.println("Opción no válida. Por favor, intente nuevamente.");
                yield false;
            }
        };
    }

    private void menuAsignaturas() {
        boolean volver = false;
        while (!volver) {
            System.out.println("\n========================================");
            System.out.println("     GESTIÓN DE ASIGNATURAS");
            System.out.println("========================================");
            System.out.println("1. Agregar asignatura");
            System.out.println("2. Listar asignaturas");
            System.out.println("3. Buscar asignatura");
            System.out.println("4. Actualizar asignatura");
            System.out.println("5. Eliminar asignatura");
            System.out.println("6. Volver al menú principal");
            System.out.println("========================================");
            System.out.print("Seleccione una opción: ");

            int opcion = leerOpcion();
            volver = procesarOpcionAsignaturas(opcion);
        }
    }

    private boolean procesarOpcionAsignaturas(int opcion) {
        return switch (opcion) {
            case 1 -> {
                agregarAsignatura();
                yield false;
            }
            case 2 -> {
                listarAsignaturas();
                yield false;
            }
            case 3 -> {
                buscarAsignatura();
                yield false;
            }
            case 4 -> {
                actualizarAsignatura();
                yield false;
            }
            case 5 -> {
                eliminarAsignatura();
                yield false;
            }
            case 6 -> true;
            default -> {
                System.out.println("Opción no válida. Por favor, intente nuevamente.");
                yield false;
            }
        };
    }

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
            case 1 -> {
                agregarMatricula();
                yield false;
            }
            case 2 -> {
                listarMatriculas();
                yield false;
            }
            case 3 -> {
                buscarMatricula();
                yield false;
            }
            case 4 -> {
                actualizarMatricula();
                yield false;
            }
            case 5 -> {
                eliminarMatricula();
                yield false;
            }
            case 6 -> true;
            default -> {
                System.out.println("Opción no válida. Por favor, intente nuevamente.");
                yield false;
            }
        };
    }

    // Métodos para Estudiantes
    private void agregarEstudiante() {
        System.out.println("\n--- Agregar Estudiante ---");
        try {
            System.out.print("Ingrese ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese email: ");
            String email = scanner.nextLine();

            Estudiante estudiante = new Estudiante(id, nombre, email);

            if (estudianteService.crear(estudiante)) {
                System.out.println("✓ Estudiante agregado exitosamente.");
            } else {
                System.out.println("✗ Error al agregar el estudiante.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        } catch (DateTimeParseException e) {
            System.out.println("✗ Error: Fecha inválida. Use el formato dd/MM/yyyy.");
        } catch (Exception e) {
            System.out.println("✗ Error al agregar estudiante: " + e.getMessage());
        }
    }

    private void listarEstudiantes() {
        System.out.println("\n--- Listar Estudiantes ---");
        ArrayList<Estudiante> estudiantes = estudianteService.listarTodas();

        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }

        for (Estudiante estudiante : estudiantes) {
            System.out.println("Id:" + estudiante.getId() + ", Nombre: " + estudiante.getNombre() + ", Email: " + estudiante.getEmail());
        }
    }

    private void buscarEstudiante() {
        System.out.println("\n--- Buscar Estudiante ---");
        try {
            System.out.print("Ingrese el ID del estudiante: ");
            int id = Integer.parseInt(scanner.nextLine());

            Estudiante estudiante = estudianteService.buscarPorId(id);

            if (estudiante != null) {
                System.out.println("\n✓ Estudiante encontrado:");
                System.out.println("ID: " + estudiante.getId());
                System.out.println("Nombre: " + estudiante.getNombre());
                System.out.println("Email: " + estudiante.getEmail());
            } else {
                System.out.println("✗ No se encontró estudiante con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        }
    }

    private void actualizarEstudiante() {
        System.out.println("\n--- Actualizar Estudiante ---");
        try {
            System.out.print("Ingrese el ID del estudiante a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Estudiante existente = estudianteService.buscarPorId(id);
            if (existente == null) {
                System.out.println("✗ No se encontró estudiante con ID: " + id);
                return;
            }

            System.out.println("\nEstudiante actual: " + existente.getNombre());
            System.out.println("Deje en blanco para mantener el valor actual.\n");

            System.out.print("Nuevo nombre [" + existente.getNombre() + "]: ");
            String nombre = scanner.nextLine();
            if (nombre.trim().isEmpty()) nombre = existente.getNombre();

            System.out.print("Nuevo email [" + existente.getEmail() + "]: ");
            String email = scanner.nextLine();
            if (email.trim().isEmpty()) email = existente.getEmail();

            Estudiante actualizado = new Estudiante(id, nombre, email);

            if (estudianteService.actualizar(actualizado)) {
                System.out.println("✓ Estudiante actualizado exitosamente.");
            } else {
                System.out.println("✗ Error al actualizar el estudiante.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        } catch (DateTimeParseException e) {
            System.out.println("✗ Error: Fecha inválida. Use el formato dd/MM/yyyy.");
        } catch (Exception e) {
            System.out.println("✗ Error al actualizar estudiante: " + e.getMessage());
        }
    }

    private void eliminarEstudiante() {
        System.out.println("\n--- Eliminar Estudiante ---");
        try {
            System.out.print("Ingrese el ID del estudiante a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Estudiante estudiante = estudianteService.buscarPorId(id);
            if (estudiante == null) {
                System.out.println("✗ No se encontró estudiante con ID: " + id);
                return;
            }

            System.out.println("\n¿Está seguro que desea eliminar al estudiante " + estudiante.getNombre() + "?");
            System.out.print("Escriba 'SI' para confirmar: ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("SI")) {
                if (estudianteService.eliminar(id)) {
                    System.out.println("✓ Estudiante eliminado exitosamente.");
                } else {
                    System.out.println("✗ Error al eliminar el estudiante.");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        }
    }

    // Métodos para Asignaturas
    private void agregarAsignatura() {
        System.out.println("\n--- Agregar Asignatura ---");
        try {
            System.out.print("Ingrese ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese créditos: ");
            int creditos = Integer.parseInt(scanner.nextLine());

            Asignatura asignatura = new Asignatura(id, nombre, creditos);

            if (asignaturaService.crear(asignatura)) {
                System.out.println("✓ Asignatura agregada exitosamente.");
            } else {
                System.out.println("✗ Error al agregar la asignatura.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID y créditos deben ser números válidos.");
        } catch (Exception e) {
            System.out.println("✗ Error al agregar asignatura: " + e.getMessage());
        }
    }

    private void listarAsignaturas() {
        System.out.println("\n--- Listar Asignaturas ---");
        ArrayList<Asignatura> asignaturas = asignaturaService.listarTodas();

        if (asignaturas.isEmpty()) {
            System.out.println("No hay asignaturas registradas.");
            return;
        }
        for (Asignatura asignatura : asignaturas) {
            System.out.println("Id: " + asignatura.getId() + " Nombre: "+asignatura.getNombre()+" Creditos: "+asignatura.getCreditos());
        }
    }

    private void buscarAsignatura() {
        System.out.println("\n--- Buscar Asignatura ---");
        try {
            System.out.print("Ingrese el ID de la asignatura: ");
            int id = Integer.parseInt(scanner.nextLine());

            Asignatura asignatura = asignaturaService.buscarPorId(id);

            if (asignatura != null) {
                System.out.println("\n✓ Asignatura encontrada:");
                System.out.println("ID: " + asignatura.getId());
                System.out.println("Nombre: " + asignatura.getNombre());
                System.out.println("Créditos: " + asignatura.getCreditos());
            } else {
                System.out.println("✗ No se encontró asignatura con ID: " + id);
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        }
    }

    private void actualizarAsignatura() {
        System.out.println("\n--- Actualizar Asignatura ---");
        try {
            System.out.print("Ingrese el ID de la asignatura a actualizar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Asignatura existente = asignaturaService.buscarPorId(id);
            if (existente == null) {
                System.out.println("✗ No se encontró asignatura con ID: " + id);
                return;
            }

            System.out.println("\nAsignatura actual: " + existente.getNombre());
            System.out.println("Deje en blanco para mantener el valor actual.\n");

            System.out.print("Nuevo nombre [" + existente.getNombre() + "]: ");
            String nombre = scanner.nextLine();
            if (nombre.trim().isEmpty()) nombre = existente.getNombre();

            System.out.print("Nuevos créditos [" + existente.getCreditos() + "]: ");
            String creditosStr = scanner.nextLine();
            int creditos = existente.getCreditos();
            if (!creditosStr.trim().isEmpty()) {
                creditos = Integer.parseInt(creditosStr);
            }

            Asignatura actualizada = new Asignatura(id, nombre, creditos);

            if (asignaturaService.actualizar(actualizada)) {
                System.out.println("✓ Asignatura actualizada exitosamente.");
            } else {
                System.out.println("✗ Error al actualizar la asignatura.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID y créditos deben ser números válidos.");
        } catch (Exception e) {
            System.out.println("✗ Error al actualizar asignatura: " + e.getMessage());
        }
    }

    private void eliminarAsignatura() {
        System.out.println("\n--- Eliminar Asignatura ---");
        try {
            System.out.print("Ingrese el ID de la asignatura a eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            Asignatura asignatura = asignaturaService.buscarPorId(id);
            if (asignatura == null) {
                System.out.println("✗ No se encontró asignatura con ID: " + id);
                return;
            }

            System.out.println("\n¿Está seguro que desea eliminar la asignatura " + asignatura.getNombre() + "?");
            System.out.print("Escriba 'SI' para confirmar: ");
            String confirmacion = scanner.nextLine();

            if (confirmacion.equalsIgnoreCase("SI")) {
                if (asignaturaService.eliminar(id)) {
                    System.out.println("✓ Asignatura eliminada exitosamente.");
                } else {
                    System.out.println("✗ Error al eliminar la asignatura.");
                }
            } else {
                System.out.println("Operación cancelada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: ID debe ser un número válido.");
        }
    }

    // Métodos para Matrículas
    private void agregarMatricula() {
        System.out.println("\n--- Agregar Matrícula ---");
        try {
            System.out.print("Ingrese ID: ");
            int id = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese nota: ");
            double nota = Double.parseDouble(scanner.nextLine());

            System.out.print("Ingrese fecha de matrícula (dd/MM/yyyy): ");
            String fechaStr = scanner.nextLine();

            System.out.print("Ingrese ID de la asignatura: ");
            int idAsignatura = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese nombre de la asignatura: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese créditos de la asignatura: ");
            int creditos = Integer.parseInt(scanner.nextLine());

            Asignatura asignatura = new Asignatura(idAsignatura, nombre, creditos);

            Matricula matricula = new Matricula(id, nota, fechaStr, asignatura);

            if (matriculaService.crear(matricula)) {
                System.out.println("✓ Matrícula agregada exitosamente.");
            } else {
                System.out.println("✗ Error al agregar la matrícula.");
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Error: Los IDs deben ser números válidos.");
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

        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.printf("║ %-5s ║ %-15s ║ %-15s ║ %-15s ║%n", "ID", "ID Estudiante", "ID Asignatura", "Fecha");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");

        for (Matricula matricula : matriculas) {
            System.out.printf("║ %-5d ║ %-15d ║ %-15d ║ %-15s ║%n",
                    matricula.getId(),
                    matricula.getIdEstudiante(),
                    matricula.getIdAsignatura(),
                    matricula.getFechaMatricula().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
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
                System.out.println("ID Estudiante: " + matricula.getIdEstudiante());
                System.out.println("ID Asignatura: " + matricula.getIdAsignatura());
                System.out.println("Fecha de Matrícula: " + matricula.getFechaMatricula().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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

            System.out.println("\nMatrícula actual: ID Estudiante " + existente.getIdEstudiante() + ", ID Asignatura " + existente.getIdAsignatura());
            System.out.println("Deje en blanco para mantener el valor actual.\n");

            System.out.print("Nuevo ID de estudiante [" + existente.getIdEstudiante() + "]: ");
            String idEstudianteStr = scanner.nextLine();
            int idEstudiante = existente.getIdEstudiante();
            if (!idEstudianteStr.trim().isEmpty()) {
                idEstudiante = Integer.parseInt(idEstudianteStr);
            }

            System.out.print("Nuevo ID de asignatura [" + existente.getIdAsignatura() + "]: ");
            String idAsignaturaStr = scanner.nextLine();
            int idAsignatura = existente.getIdAsignatura();
            if (!idAsignaturaStr.trim().isEmpty()) {
                idAsignatura = Integer.parseInt(idAsignaturaStr);
            }

            System.out.print("Nueva fecha de matrícula (dd/MM/yyyy) [" + existente.getFechaMatricula().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "]: ");
            String fechaStr = scanner.nextLine();
            LocalDate fechaMatricula = existente.getFechaMatricula();
            if (!fechaStr.trim().isEmpty()) {
                fechaMatricula = LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            }

            Matricula actualizada = new Matricula(id, idEstudiante, idAsignatura, fechaMatricula);

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

            System.out.println("\n¿Está seguro que desea eliminar esta matrícula?");
            System.out.print("Escriba 'SI' para confirmar: ");
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

    private void salir() {
        System.out.println("\n¡Gracias por usar la aplicación!");
        this.salir = true;
    }

    private void cerrar() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
