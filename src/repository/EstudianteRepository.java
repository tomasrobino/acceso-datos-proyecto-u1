package repository;

import model.Estudiante;
import model.Matricula;

import java.sql.*;
import java.util.ArrayList;

public class EstudianteRepository extends Database<Estudiante, Integer>{

    @Override
    public Estudiante find(Integer id) {
        try {
            Connection conexion = DriverManager.getConnection(uri, usuario, password);

            Statement st = conexion.createStatement();
            // PreparedStatement is not necessary because "id" is an integer
            ResultSet rsMatriculas = st.executeQuery("SELECT * FROM matriculas WHERE id_estudiante = " + id);
            ArrayList<Matricula> matriculas = new ArrayList<>();
            while(rsMatriculas.next()){
                matriculas.add( new Matricula(rsMatriculas.getInt("id"), rsMatriculas.getDouble("nota"), rsMatriculas.getString("fecha") ));
            }
            rsMatriculas.close();
            st.close();

            Statement st2 = conexion.createStatement();
            ResultSet rsEstudiante = st2.executeQuery("SELECT * FROM estudiantes WHERE id = "+ id);
            rsEstudiante.next();
            Estudiante estudiante = new Estudiante(rsEstudiante.getInt("id"), rsEstudiante.getString("nombre"), rsEstudiante.getString("email"), matriculas);
            rsEstudiante.close();
            st2.close();
            conexion.close();
            return estudiante;
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
                rsMatriculas.close();
                st2.close();
            }
            rs.close();
            st.close();
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
                PreparedStatement psMatriculas = conexion.prepareStatement("INSERT INTO matriculas (estudiante_id, nota, fecha) VALUES (?, ?, ?)");

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
                psMatriculas.close();
                psEstudiante.close();
                return true;
            } catch (SQLException e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.setAutoCommit(true);
                conexion.close();
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

        try {
            Connection conexion = DriverManager.getConnection(uri, usuario, password);

            try {
                conexion.setAutoCommit(false);
                PreparedStatement psEstudiante = conexion.prepareStatement("UPDATE estudiantes SET nombre = ?, email = ? WHERE id = ?");
                PreparedStatement psMatriculas = conexion.prepareStatement("INSERT INTO matriculas (estudiante_id, nota, fecha) VALUES (?, ?, ?)");

                for (Matricula matricula : estudiante.getMatriculas()) {
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

                psMatriculas.close();
                psEstudiante.close();
                conexion.commit();
                return true;
            } catch (SQLException e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.setAutoCommit(true);
                conexion.close();
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

        try {
            Connection conexion = DriverManager.getConnection(uri, usuario, password);

            try {
                conexion.setAutoCommit(false);
                PreparedStatement psEstudiante = conexion.prepareStatement("DELETE FROM estudiantes WHERE id = ?");

                for (Matricula matricula : estudiante.getMatriculas()) {
                    delete(matricula.getId());
                }

                psEstudiante.setInt(1, id);
                psEstudiante.executeQuery();

                conexion.commit();
                return true;
            } catch (SQLException e) {
                conexion.rollback();
                e.printStackTrace();
            } finally {
                conexion.setAutoCommit(true);
                conexion.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
