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

    //TODO: create func: createEntityManager
    public BaseDAO() {
        if(emf == null)
            emf = Persistence.createEntityManagerFactory("gymPU");
        em = emf.createEntityManager();
    }

    /**
     * Get EntityManagerFactory (for creating new EntityManager instances)
     */
    protected static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("gymPU");
        }
        return emf;
    }

    /**
     * Create a new EntityManager instance
     */
    protected EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public void beginTransaction() {
        if (em != null && em.isOpen() && !em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    public void commitTransaction() {
        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    public void rollbackTransaction() {
        if (em != null && em.isOpen() && em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}