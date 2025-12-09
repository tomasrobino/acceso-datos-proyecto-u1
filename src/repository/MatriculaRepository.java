package repository;

import model.Matricula;

import java.sql.*;
import java.util.ArrayList;

public class MatriculaRepository extends Database<Matricula, Integer> {
    @Override
    public Matricula find(Integer id) {
        try {
            Connection conexion = DriverManager.getConnection(uri, usuario, password);

            Statement st = conexion.createStatement();
            // PreparedStatement is not necessary because "id" is an integer
            ResultSet rs = st.executeQuery("SELECT * FROM matriculas WHERE id = " + id);
            rs.next();
            Matricula matricula = new Matricula(rs.getInt("id"), rs.getDouble("nota"), rs.getString("fecha") );
            rs.close();
            st.close();
            conexion.close();
            return matricula;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Matricula> findAll() {
        return null;
    }

    @Override
    public boolean insert(Matricula model) {
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
