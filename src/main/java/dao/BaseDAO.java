package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public abstract class BaseDAO {

    private static EntityManagerFactory emf = null;
    protected EntityManager em;

    static {
        try {
            emf = Persistence.createEntityManagerFactory("gymPU");
            System.out.println("EMF created successfully!");
        } catch (Exception e) {
            System.out.println("======================================================");
            System.out.println("CANNOT connect to Database:");
            e.printStackTrace();  // in full stack trace
            System.out.println("======================================================");
        }
    }

    public BaseDAO() {
        em = emf.createEntityManager();
    }

    public void beginTransaction() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    public void commitTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    public void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    public void close() {
        if (em.isOpen()) {
            em.close();
        }
    }
}
