package repository;

import model.Matricula;

import java.sql.*;
import java.util.ArrayList;

public class MatriculaRepository extends Database<Matricula, Integer> {
    @Override
    public Matricula find(Integer id) {
        try (
            Connection conexion = DriverManager.getConnection(uri, usuario, password);
            Statement st = conexion.createStatement();
            // PreparedStatement is not necessary because "id" is an integer
            ResultSet rs = st.executeQuery("SELECT * FROM matriculas WHERE id = " + id)
        ) {
            rs.next();
            return new Matricula(rs.getInt("id"), rs.getDouble("nota"), rs.getString("fecha"), rs.getInt("estudiante_id") );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Matricula> findAll() {
        try (
            Connection conexion = DriverManager.getConnection(uri, usuario, password);
            Statement st = conexion.createStatement();
            // PreparedStatement is not necessary because "id" is an integer
            ResultSet rs = st.executeQuery("SELECT * FROM matriculas")
        ) {
            ArrayList<Matricula> matriculas = new ArrayList<>();
            while (rs.next()) {
                matriculas.add(new Matricula(rs.getInt("id"), rs.getDouble("nota"), rs.getString("fecha"), rs.getInt("estudiante_id") ));
            }
            return matriculas;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(Matricula model) {
        if (model == null) {
            return false;
        }

        try (Connection conexion = DriverManager.getConnection(uri, usuario, password)) {
            try (
                Statement st = conexion.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM estudiantes WHERE id = " + model.getEstudiante_id())
            ) {
                if (!rs.next()) {
                    return false;
                }
            }

            try (PreparedStatement ps = conexion.prepareStatement("INSERT INTO matriculas (estudiante_id, nota, fecha) VALUES (?, ?, ?)")) {
                ps.setInt(1, model.getEstudiante_id());
                ps.setDouble(2, model.getNota());
                ps.setString(3, model.getFecha());
                ps.executeUpdate();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(Matricula model) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
