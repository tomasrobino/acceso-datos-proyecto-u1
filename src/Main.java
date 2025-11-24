import presentation.MenuConsola;
import repository.EstudianteRepository;
import repository.MatriculaRepository;
import service.Service;

public class Main {
    public static void main(String[] args) {
        MenuConsola menu = new MenuConsola(new Service<>(new EstudianteRepository()), new Service<>(new MatriculaRepository()));
        menu.mostrarMenu();
    }
}
