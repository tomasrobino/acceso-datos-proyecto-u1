package repository;

import model.Clase;
import model.Profesor;

import java.sql.*;
import java.util.ArrayList;

public class ProfesorRepository extends Database<Profesor, Integer> {
    private final ClaseRepository claseRepository;

    public ProfesorRepository(ClaseRepository claseRepository) {
        this.claseRepository = claseRepository;
    }

    @Override
    public Profesor find(Integer id) {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            ArrayList<Clase> clases = new ArrayList<>();

            try (Statement st = conexion.createStatement();
                 ResultSet rsRelaciones = st.executeQuery("SELECT clase_id FROM profesor_clase WHERE profesor_id = " + id)) {

                while (rsRelaciones.next()) {
                    int claseId = rsRelaciones.getInt("clase_id");
                    Clase clase = claseRepository.findWithoutProfesores(claseId);
                    if (clase != null) {
                        clases.add(clase);
                    }
                }
            }

            try (Statement st2 = conexion.createStatement();
                 ResultSet rsProfesor = st2.executeQuery("SELECT * FROM profesores WHERE id = " + id)) {
                if (rsProfesor.next()) {
                    return new Profesor(
                            rsProfesor.getInt("id"),
                            rsProfesor.getString("nombre"),
                            rsProfesor.getString("especialidad"),
                            clases
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Profesor findWithoutClases(Integer id) {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password);
             Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM profesores WHERE id = " + id)) {

            if (rs.next()) {
                return new Profesor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("especialidad"),
                        new ArrayList<>()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Profesor> findAll() {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password);
             Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM profesores")) {

            ArrayList<Profesor> profesores = new ArrayList<>();
            while (rs.next()) {
                int profesorId = rs.getInt("id");
                ArrayList<Clase> clases = new ArrayList<>();

                try (Statement st2 = conexion.createStatement();
                     ResultSet rsRelaciones = st2.executeQuery("SELECT clase_id FROM profesor_clase WHERE profesor_id = " + profesorId)) {

                    while (rsRelaciones.next()) {
                        int claseId = rsRelaciones.getInt("clase_id");
                        Clase clase = claseRepository.findWithoutProfesores(claseId);
                        if (clase != null) {
                            clases.add(clase);
                        }
                    }
                }

                profesores.add(new Profesor(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("especialidad"),
                        clases
                ));
            }
            return profesores;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(Profesor model) {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            conexion.setAutoCommit(false);
            try (PreparedStatement psProfesor = conexion.prepareStatement("INSERT INTO profesores (nombre, especialidad) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psRelacion = conexion.prepareStatement("INSERT INTO profesor_clase (profesor_id, clase_id) VALUES (?, ?)")) {

                psProfesor.setString(1, model.getNombre());
                psProfesor.setString(2, model.getEspecialidad());
                psProfesor.executeUpdate();

                ResultSet generatedKeys = psProfesor.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int profesorId = generatedKeys.getInt(1);

                    for (Clase clase : model.getClases()) {
                        psRelacion.setInt(1, profesorId);
                        psRelacion.setInt(2, clase.getId());
                        psRelacion.executeUpdate();
                    }
                }

                conexion.commit();
                return true;
            } catch (SQLException e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Profesor model) {
        Profesor profesor = find(model.getId());
        if (profesor == null) {
            return false;
        }

        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            conexion.setAutoCommit(false);
            try (PreparedStatement psProfesor = conexion.prepareStatement("UPDATE profesores SET nombre = ?, especialidad = ? WHERE id = ?");
                 PreparedStatement psDelete = conexion.prepareStatement("DELETE FROM profesor_clase WHERE profesor_id = ?");
                 PreparedStatement psInsert = conexion.prepareStatement("INSERT INTO profesor_clase (profesor_id, clase_id) VALUES (?, ?)")) {

                psProfesor.setString(1, model.getNombre());
                psProfesor.setString(2, model.getEspecialidad());
                psProfesor.setInt(3, model.getId());
                psProfesor.executeUpdate();

                psDelete.setInt(1, model.getId());
                psDelete.executeUpdate();

                for (Clase clase : model.getClases()) {
                    psInsert.setInt(1, model.getId());
                    psInsert.setInt(2, clase.getId());
                    psInsert.executeUpdate();
                }

                conexion.commit();
                return true;
            } catch (SQLException e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        Profesor profesor = find(id);
        if (profesor == null) {
            return false;
        }

        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            conexion.setAutoCommit(false);
            try (PreparedStatement psRelacion = conexion.prepareStatement("DELETE FROM profesor_clase WHERE profesor_id = ?");
                 PreparedStatement psProfesor = conexion.prepareStatement("DELETE FROM profesores WHERE id = ?")) {

                psRelacion.setInt(1, id);
                psRelacion.executeUpdate();

                psProfesor.setInt(1, id);
                psProfesor.executeUpdate();

                conexion.commit();
                return true;
            } catch (SQLException e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}