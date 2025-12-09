package repository;

import model.Clase;
import model.Profesor;

import java.sql.*;
import java.util.ArrayList;

public class ClaseRepository extends Database<Clase, Integer> {
    private final ProfesorRepository profesorRepository;

    public ClaseRepository(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @Override
    public Clase find(Integer id) {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            ArrayList<Profesor> profesores = new ArrayList<>();

            try (Statement st = conexion.createStatement();
                 ResultSet rsRelaciones = st.executeQuery("SELECT profesor_id FROM profesor_clase WHERE clase_id = " + id)) {

                while (rsRelaciones.next()) {
                    int profesorId = rsRelaciones.getInt("profesor_id");
                    Profesor profesor = profesorRepository.findWithoutClases(profesorId);
                    if (profesor != null) {
                        profesores.add(profesor);
                    }
                }
            }

            try (Statement st2 = conexion.createStatement();
                 ResultSet rsClase = st2.executeQuery("SELECT * FROM clases WHERE id = " + id)) {
                if (rsClase.next()) {
                    return new Clase(
                            rsClase.getInt("id"),
                            rsClase.getString("nombre"),
                            rsClase.getString("horario"),
                            profesores
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Clase findWithoutProfesores(Integer id) {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password);
             Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM clases WHERE id = " + id)) {

            if (rs.next()) {
                return new Clase(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("horario"),
                        new ArrayList<>()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Clase> findAll() {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password);
             Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM clases")) {

            ArrayList<Clase> clases = new ArrayList<>();
            while (rs.next()) {
                int claseId = rs.getInt("id");
                ArrayList<Profesor> profesores = new ArrayList<>();

                try (Statement st2 = conexion.createStatement();
                     ResultSet rsRelaciones = st2.executeQuery("SELECT profesor_id FROM profesor_clase WHERE clase_id = " + claseId)) {

                    while (rsRelaciones.next()) {
                        int profesorId = rsRelaciones.getInt("profesor_id");
                        Profesor profesor = profesorRepository.findWithoutClases(profesorId);
                        if (profesor != null) {
                            profesores.add(profesor);
                        }
                    }
                }

                clases.add(new Clase(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("horario"),
                        profesores
                ));
            }
            return clases;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(Clase model) {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            conexion.setAutoCommit(false);
            try (PreparedStatement psClase = conexion.prepareStatement("INSERT INTO clases (nombre, horario) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psRelacion = conexion.prepareStatement("INSERT INTO profesor_clase (profesor_id, clase_id) VALUES (?, ?)")) {

                psClase.setString(1, model.getNombre());
                psClase.setString(2, model.getHorario());
                psClase.executeUpdate();

                ResultSet generatedKeys = psClase.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int claseId = generatedKeys.getInt(1);

                    for (Profesor profesor : model.getProfesores()) {
                        psRelacion.setInt(1, profesor.getId());
                        psRelacion.setInt(2, claseId);
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
    public boolean update(Clase model) {
        Clase clase = find(model.getId());
        if (clase == null) {
            return false;
        }

        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            conexion.setAutoCommit(false);
            try (PreparedStatement psClase = conexion.prepareStatement("UPDATE clases SET nombre = ?, horario = ? WHERE id = ?");
                 PreparedStatement psDelete = conexion.prepareStatement("DELETE FROM profesor_clase WHERE clase_id = ?");
                 PreparedStatement psInsert = conexion.prepareStatement("INSERT INTO profesor_clase (profesor_id, clase_id) VALUES (?, ?)")) {

                psClase.setString(1, model.getNombre());
                psClase.setString(2, model.getHorario());
                psClase.setInt(3, model.getId());
                psClase.executeUpdate();

                psDelete.setInt(1, model.getId());
                psDelete.executeUpdate();

                for (Profesor profesor : model.getProfesores()) {
                    psInsert.setInt(1, profesor.getId());
                    psInsert.setInt(2, model.getId());
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
        Clase clase = find(id);
        if (clase == null) {
            return false;
        }

        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            conexion.setAutoCommit(false);
            try (PreparedStatement psRelacion = conexion.prepareStatement("DELETE FROM profesor_clase WHERE clase_id = ?");
                 PreparedStatement psClase = conexion.prepareStatement("DELETE FROM clases WHERE id = ?")) {

                psRelacion.setInt(1, id);
                psRelacion.executeUpdate();

                psClase.setInt(1, id);
                psClase.executeUpdate();

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