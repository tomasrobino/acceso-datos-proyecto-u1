import model.Estudiante;
import model.Matricula;
import presentation.MenuConsola;
import repository.EstudianteRepository;
import repository.MatriculaRepository;
import service.Service;

public class Main {
    public static void main(String[] args) {
        MenuConsola menu = new MenuConsola(new Service<Estudiante>(new EstudianteRepository()), new Service<Matricula>(new MatriculaRepository()));
        menu.mostrarMenu();
    }
}
