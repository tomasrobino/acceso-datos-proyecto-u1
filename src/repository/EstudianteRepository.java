package repository;

import model.Estudiante;
import model.Matricula;

import java.sql.*;
import java.util.ArrayList;

public class EstudianteRepository extends Database<Estudiante, Integer>{

    @Override
    public Estudiante find(Integer id) {
        try{
            Connection conexion = DriverManager.getConnection(uri, usuario, password);

            Statement st = conexion.createStatement();
            ResultSet rsMatriculas = st.executeQuery("SELECT * FROM matriculas WHERE id_estudiante = " + id);
            ArrayList<Matricula> matriculas = new ArrayList<>();
            while(rsMatriculas.next()){
                matriculas.add( new Matricula(rsMatriculas.getInt("id"), rsMatriculas.getDouble("nota"), rsMatriculas.getString("fecha") ));
            }

            PreparedStatement ps = conexion.prepareStatement("SELECT * FROM estudiantes WHERE id = ?");
            ps.setInt(1, (Integer) id);
            ResultSet rsEstudiante = ps.executeQuery();
            rsEstudiante.next();

            conexion.close();
            return new Estudiante(rsEstudiante.getInt("id"), rsEstudiante.getString("nombre"), rsEstudiante.getString("email"), matriculas);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Estudiante> findAll() {
        try{
            Connection conexion = DriverManager.getConnection(uri, usuario, password);
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM estudiantes");

            ArrayList<Estudiante> estudiantes = new ArrayList<>();
            while(rs.next()){
                Statement st2 = conexion.createStatement();
                ResultSet rsMatriculas = st2.executeQuery("SELECT * FROM matriculas WHERE id_estudiante = " + rs.getInt("id"));
                ArrayList<Matricula> matriculas = new ArrayList<>();
                while(rsMatriculas.next()){
                    matriculas.add( new Matricula(rsMatriculas.getInt("id"), rsMatriculas.getDouble("nota"), rsMatriculas.getString("fecha") ));
                }

                estudiantes.add(new Estudiante(rs.getInt("id"), rs.getString("nombre"), rs.getString("email"), matriculas));
            }

            conexion.close();
            return estudiantes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean insert(Estudiante model) {
        try {
            Connection conexion = DriverManager.getConnection(uri, usuario, password);

            try {
                conexion.setAutoCommit(false);
                PreparedStatement psEstudiante = conexion.prepareStatement("INSERT INTO estudiantes (nombre, email) VALUES (?, ?)");
                PreparedStatement psMatriculas = conexion.prepareStatement("INSERT INTO matriculas (id_estudiante, nota, fecha) VALUES (?, ?, ?)");

                for (Matricula matricula : model.getMatriculas()) {
                    psMatriculas.setInt(1, model.getId());
                    psMatriculas.setDouble(2, matricula.getNota());
                    psMatriculas.setString(3, matricula.getFecha());
                    psMatriculas.executeUpdate();
                }

                psEstudiante.setString(1, model.getNombre());
                psEstudiante.setString(2, model.getEmail());
                psEstudiante.executeUpdate();

                conexion.commit();
                conexion.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexion.rollback();
                conexion.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean update(Estudiante model) {
        Estudiante es = find(model.getId());
        if (es == null) {
            return false;
        }

        try {
            Connection conexion = DriverManager.getConnection(uri, usuario, password);

            try {
                conexion.setAutoCommit(false);
                PreparedStatement psEstudiante = conexion.prepareStatement("UPDATE estudiantes SET nombre = ?, email = ? WHERE id = ?");
                PreparedStatement psMatriculas = conexion.prepareStatement("INSERT INTO matriculas (id_estudiante, nota, fecha) VALUES (?, ?, ?)");

                for (Matricula matricula : es.getMatriculas()) {
                    delete(matricula.getId());
                }

                for (Matricula matricula : model.getMatriculas()) {
                    psMatriculas.setInt(1, model.getId());
                    psMatriculas.setDouble(2, matricula.getNota());
                    psMatriculas.setString(3, matricula.getFecha());
                    psMatriculas.executeUpdate();
                }

                psEstudiante.setString(1, model.getNombre());
                psEstudiante.setString(2, model.getEmail());
                psEstudiante.executeUpdate();

                conexion.commit();
                conexion.close();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conexion.rollback();
                conexion.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }
}
