package repository.csv;

import model.Estudiante;
import model.Model;
import repository.BDInterfaz;

import java.io.*;
import java.util.ArrayList;

public class EstudianteCSV extends BDInterfaz {
    private static EstudianteCSV miEstudianteCSV = null;

    public BDInterfaz get(String uri) {
        if (miEstudianteCSV == null) {
            miEstudianteCSV = new EstudianteCSV(uri);
        }
        return miEstudianteCSV;
    }

    private EstudianteCSV(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    @Override
    public Model find(int id) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(uri));
            String line;
            while ( (line = br.readLine()) != null ) {
                String[] data = line.split(",");
                if (Integer.parseInt(data[0]) == id) {
                    br.close();
                    return new Estudiante(id, data[1], data[2], data[3]);
                }
            }
            br.close();
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public ArrayList<Model> findAll() {
        return null;
    }

    @Override
    public boolean insert(Model model) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(uri, true));
            bw.write(model.stringifyCSV());
            bw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean update(Model model) {
        File file1 = new File(uri);
        File file2 = new File(uri+"_temp");
        boolean ret = false;

        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file2));

            String line;
            while ( (line = br.readLine()) != null ) {
                String[] data = line.split(",");
                if (Integer.parseInt(data[0]) == model.getId()) {
                    bw.write(model.stringifyCSV());
                    ret = true;
                } else {
                    bw.write(line);
                }
            }
            br.close();
            bw.close();

            if (!file1.delete() || !file2.renameTo(file1)) return false;
        } catch (IOException e) {
            return false;
        }
        return ret;
    }

    @Override
    public boolean delete(int id) {
        File file1 = new File(uri);
        File file2 = new File(uri+"_temp");
        boolean ret = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file1));
            BufferedWriter bw = new BufferedWriter(new FileWriter(file2));


            String line;
            while ( (line = br.readLine()) != null ) {
                String[] data = line.split(",");
                if ((Integer.parseInt(data[0]) == id)) {
                    ret = true;
                } else bw.write(line);
            }
            br.close();
            bw.close();

            if (!file1.delete() || !file2.renameTo(file1)) return false;
        } catch (IOException e) {
            return false;
        }

        return ret;
    }
}
