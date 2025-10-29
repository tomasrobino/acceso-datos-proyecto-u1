package repository.csv;

import model.Model;
import repository.BDInterfaz;

import java.io.*;

abstract class CSV extends BDInterfaz {
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
