import presentation.MenuConsola;
import repository.ClaseRepository;
import repository.EstudianteRepository;
import repository.MatriculaRepository;
import repository.ProfesorRepository;
import service.Service;

public class Main {
    public static void main(String[] args) {
        MatriculaRepository matriculaRepository = new MatriculaRepository();
        ProfesorRepository profesorRepository = new ProfesorRepository();
        ClaseRepository claseRepository = new ClaseRepository(profesorRepository);
        profesorRepository.setClaseRepository(claseRepository);
        MenuConsola menu = new MenuConsola(new Service<>(new EstudianteRepository(matriculaRepository)), new Service<>(matriculaRepository), new Service<>(profesorRepository), new Service<>(claseRepository));
        menu.mostrarMenu();
    }
}
