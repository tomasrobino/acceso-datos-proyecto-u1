package repository.csv;

import model.Asignatura;
import model.Model;
import repository.BDInterfaz;

import java.io.*;
import java.util.ArrayList;

public class AsignaturaCSV extends BDInterfaz {
    private static AsignaturaCSV miAsignaturaCSV = null;

    public BDInterfaz get(String uri) {
        if (miAsignaturaCSV == null) {
            miAsignaturaCSV = new AsignaturaCSV(uri);
        }
        return miAsignaturaCSV;
    }

    private AsignaturaCSV(String uri) {
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
                    return new Asignatura(id, data[1], Integer.parseInt(data[2]), data[3]);
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
        try {
            BufferedReader br = new BufferedReader(new FileReader(uri));
            String line;
            ArrayList<Model> lista = new ArrayList<>();
            while ( (line = br.readLine()) != null ) {
                String[] data = line.split(",");
                lista.add(new Asignatura(Integer.parseInt(data[0]), data[1], Integer.parseInt(data[2]), data[3]));
            }
            br.close();
            return lista;
        } catch (IOException e) {
            return null;
        }
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
