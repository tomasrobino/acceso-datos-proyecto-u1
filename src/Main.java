import presentation.MenuConsola;
import repository.EstudianteRepository;
import repository.MatriculaRepository;
import service.Service;

public class Main {
    public static void main(String[] args) {
        MatriculaRepository matriculaRepository = new MatriculaRepository();
        MenuConsola menu = new MenuConsola(new Service<>(new EstudianteRepository(matriculaRepository)), new Service<>(matriculaRepository));
        menu.mostrarMenu();
    }
}
