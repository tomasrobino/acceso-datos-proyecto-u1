package repository;

import model.Model;

public class BIN extends BDInterfaz{
    private static BIN miBIN;

    private BIN(String uri) {
        this.uri = uri;
    }

    @Override
    BDInterfaz get(String uri) {
        if (miBIN == null) {
            miBIN = new BIN(uri);
        }
        return miBIN;
    }

    @Override
    String find(int id) {
        return "";
    }

    @Override
    boolean insert(Model model) {
        return false;
    }

    @Override
    boolean update(Model model) {
        return false;
    }

    @Override
    boolean delete(int id) {
        return false;
    }
}
