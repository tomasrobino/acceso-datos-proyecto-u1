package repository;

import model.Estudiante;
import model.Matricula;

import java.sql.*;
import java.util.ArrayList;

public class EstudianteRepository extends Database<Estudiante, Integer>{
    public final MatriculaRepository matriculaRepository;

    public EstudianteRepository(MatriculaRepository matriculaRepository) {
        this.matriculaRepository = matriculaRepository;
    }

    @Override
    public Estudiante find(Integer id) {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            try (Statement st = conexion.createStatement();
                 ResultSet rsMatriculas = st.executeQuery("SELECT * FROM matriculas WHERE estudiante_id = " + id)) {
                // PreparedStatement is not necessary because "id" is an integer
                ArrayList<Matricula> matriculas = new ArrayList<>();
                while (rsMatriculas.next()) {
                    matriculas.add(new Matricula(
                        rsMatriculas.getInt("id"),
                        rsMatriculas.getDouble("nota"),
                        rsMatriculas.getString("fecha"),
                        rsMatriculas.getInt("estudiante_id")
                    ));
                }

                try (Statement st2 = conexion.createStatement();
                     ResultSet rsEstudiante = st2.executeQuery("SELECT * FROM estudiantes WHERE id = " + id)) {
                    rsEstudiante.next();
                    return new Estudiante(
                            rsEstudiante.getInt("id"),
                            rsEstudiante.getString("nombre"),
                            rsEstudiante.getString("email"),
                            rsEstudiante.getBytes("foto"),
                            matriculas
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Estudiante> findAll() {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password);
             Statement st = conexion.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM estudiantes")) {

            ArrayList<Estudiante> estudiantes = new ArrayList<>();
            while (rs.next()) {
                try (Statement st2 = conexion.createStatement();
                     ResultSet rsMatriculas = st2.executeQuery("SELECT * FROM matriculas WHERE estudiante_id = " + rs.getInt("id"))) {

                    ArrayList<Matricula> matriculas = new ArrayList<>();
                    while (rsMatriculas.next()) {
                        matriculas.add(new Matricula(rsMatriculas.getInt("id"), rsMatriculas.getDouble("nota"), rsMatriculas.getString("fecha"), rsMatriculas.getInt("estudiante_id")));
                    }

                    estudiantes.add(new Estudiante(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rs.getBytes("foto"),
                            matriculas));
                }
            }

            return estudiantes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(Estudiante model) {
        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            conexion.setAutoCommit(false);
            try (PreparedStatement psEstudiante = conexion.prepareStatement("INSERT INTO estudiantes (nombre, email, foto) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement psMatriculas = conexion.prepareStatement("INSERT INTO matriculas (estudiante_id, nota, fecha) VALUES (?, ?, ?)")) {

                psEstudiante.setString(1, model.getNombre());
                psEstudiante.setString(2, model.getEmail());
                psEstudiante.setBytes(3, model.getFoto());
                psEstudiante.executeUpdate();
                ResultSet rs = psEstudiante.getGeneratedKeys();
                rs.next();

                for (Matricula matricula : model.getMatriculas()) {
                    psMatriculas.setInt(1, rs.getInt(1));
                    psMatriculas.setDouble(2, matricula.getNota());
                    psMatriculas.setString(3, matricula.getFecha());
                    psMatriculas.executeUpdate();
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
    public boolean update(Estudiante model) {
        Estudiante estudiante = find(model.getId());
        if (estudiante == null) {
            return false;
        }

        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            conexion.setAutoCommit(false);
            try (PreparedStatement psEstudiante = conexion.prepareStatement("UPDATE estudiantes SET nombre = ?, email = ?, foto = ? WHERE id = ?");
                 PreparedStatement psMatriculas = conexion.prepareStatement("INSERT INTO matriculas (estudiante_id, nota, fecha) VALUES (?, ?, ?)")) {

                for (Matricula matricula : estudiante.getMatriculas()) {
                    matriculaRepository.delete(matricula.getId());
                }

                for (Matricula matricula : model.getMatriculas()) {
                    psMatriculas.setInt(1, model.getId());
                    psMatriculas.setDouble(2, matricula.getNota());
                    psMatriculas.setString(3, matricula.getFecha());
                    psMatriculas.executeUpdate();
                }

                psEstudiante.setString(1, model.getNombre());
                psEstudiante.setString(2, model.getEmail());
                psEstudiante.setBytes(3, model.getFoto());
                psEstudiante.executeUpdate();

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
        Estudiante estudiante = find(id);
        if (estudiante == null) {
            return false;
        }

        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            conexion.setAutoCommit(false);
            try (PreparedStatement psEstudiante = conexion.prepareStatement("DELETE FROM estudiantes WHERE id = ?")) {
                psEstudiante.setInt(1, id);
                psEstudiante.executeQuery();

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
