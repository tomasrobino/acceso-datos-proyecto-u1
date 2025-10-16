package repository;

import model.Model;

import java.io.*;

public class CSV extends BDInterfaz {
    private static CSV miCSV = null;

    public static CSV getCSV(String uri) {
        if (miCSV == null) {
            miCSV = new CSV(uri);
        }
        return miCSV;
    }

    private CSV(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }

    @Override
    String find(int id) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(uri));
        String line;
        while ( (line = br.readLine()) != null ) {
            String[] data = line.split(",");
            if (Integer.parseInt(data[0]) == id) {
                br.close();
                return line;
            }
        }
        br.close();
        return null;
    }

    @Override
    void insert(Model model) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(uri, true));
        bw.write(model.stringifyCSV());
        bw.close();
    }

    @Override
    boolean update(Model model) throws IOException {
        File file1 = new File(uri);
        File file2 = new File(uri+"_temp");
        BufferedReader br = new BufferedReader(new FileReader(file1));
        BufferedWriter bw = new BufferedWriter(new FileWriter(file2));
        boolean ret = false;

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

        if (!file1.delete() && file2.renameTo(file1)) throw new IOException();
        return ret;
    }

    @Override
    boolean delete(int id) throws IOException {
        File file1 = new File(uri);
        File file2 = new File(uri+"_temp");
        BufferedReader br = new BufferedReader(new FileReader(file1));
        BufferedWriter bw = new BufferedWriter(new FileWriter(file2));
        boolean ret = false;

        String line;
        while ( (line = br.readLine()) != null ) {
            String[] data = line.split(",");
            if ((Integer.parseInt(data[0]) == id)) {
                ret = true;
            } else bw.write(line);
        }
        br.close();
        bw.close();

        if (!file1.delete() && file2.renameTo(file1)) throw new IOException();
        return ret;
    }
}
