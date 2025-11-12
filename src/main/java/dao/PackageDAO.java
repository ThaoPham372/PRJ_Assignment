package dao;

import java.util.ArrayList;
import java.util.List;
import model.Package;

/*
    Note: 
 */
public class PackageDAO {
    GenericDAO<Package> genericDAO;

    public PackageDAO() {
        genericDAO = new GenericDAO<>(Package.class);
    }

    public int save(Package packageO) {
        genericDAO.save(packageO);
        return packageO.getId();
    }

    public List<Package> findAll() {
        List<Package> packageOs = genericDAO.findAll();
        return packageOs != null ? packageOs : new ArrayList<>(List.of());
    }

    public Package findById(int id) {
        System.out.println(">>Package: Find by ID");
        Package packageO;
        try {
            packageO = genericDAO.findById(id);
            System.out.println("result: " + packageO);
        } catch (Exception e) {
            System.out.println("PackageDAO -> Error find by id: " + e.getMessage());
            return null;
        }
        System.out.println("------------------------------- FOUND");
        return packageO;
    }

    public Package findByPackageName(String packageOname) {
        Package packageO;
        try {
            packageO = genericDAO.findByField("packageOname", packageOname);
        } catch (Exception e) {
            System.out.println("PackageDAO -> Error find by packageO name: " + e.getMessage());
            return null;
        }
        return packageO;
    }


    public int update(Package packageO) {
        int id = genericDAO.update(packageO);
        return id;
    }

    public int delete(Package packageO) {
        packageO.setIsActive(false);
        int packageOId = genericDAO.update(packageO);
        return packageOId;
    }

}
