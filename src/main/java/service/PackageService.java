
package service;

import dao.PackageDAO;
import model.Package;
import java.util.List;

/*
    Note: 
 */
public class PackageService {

    private final PackageDAO packageDAO;

    public PackageService() {
        packageDAO = new PackageDAO();
    }

    // Package
    public List<Package> getAll() {
        List<Package> packageOs = packageDAO.findAll();
        sortById(packageOs);
        return packageOs;
    }

    private void sortById(List<Package> packageOs) {
        packageOs.sort((u1, u2) -> Integer.compare(u1.getId(), u2.getId()));
    }

    public Package getById(int id) {
        return packageDAO.findById(id);
    }

    public int add(Package packageO) {
        return packageDAO.save(packageO);
    }

    public int update(Package packageO) {
        return packageDAO.update(packageO);
    }

    public int delete(Package packageO) {
        return packageDAO.delete(packageO);
    }
}
