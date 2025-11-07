package dao;

import jakarta.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;
import model.User;

/*
    Note: 
 */
public class GenericDAO<T> extends BaseDAO {

    private final Class<T> entityClass;

    public GenericDAO(Class<T> entityClass) {
        super();
        this.entityClass = entityClass;
    }
    
    // Create
    public int save(T entity) {
        try {
            beginTransaction();
            em.persist(entity);
            commitTransaction();
            
            if(entity instanceof User)
                ((User) entity).getUserId();
        } catch (Exception e) {
            rollbackTransaction();
            throw e;
        }
        return -1;
    }
    
    
    // Read
    public T findById(int id) {
        return em.find(entityClass, id);
    }

    public List<T> findAll() {
        try {
            return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                    .getResultList();
        } catch (Exception e){
            System.out.println("Error: Find ALL ---> ");
        }
        return new ArrayList<>(List.of());
    }
    
    public T findByField(String fieldName, Object value){
        try{
            T entity = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :val", entityClass)
                     .setParameter("val", value)
                     .getSingleResult();
            return entity;
        } catch(NoResultException e){
            return null;
        }
    }
    
    // Update
    public int update(T entity) {
        try {
            beginTransaction();
            em.merge(entity);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            return -1;
        }
        if(entity instanceof User)
            return ((User) entity).getUserId();
        return -1;
    }   
    
    
    // Delete
    public int delete(T entity) {
        try {
            beginTransaction();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            return -1;
        }
        if(entity instanceof User) 
            return ((User)entity).getUserId();
        return -1;
    }
}
