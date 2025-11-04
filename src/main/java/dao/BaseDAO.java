package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public abstract class BaseDAO<T> {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("gym-pu"); // tên persistence-unit

    protected EntityManager em;

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