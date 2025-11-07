package com.gym.dao;

import jakarta.persistence.NoResultException;
import java.util.List;
import java.util.Optional;
import com.gym.model.User;

/**
 * GenericDAO - Generic Data Access Object for JPA entities
 * Provides common CRUD operations that can be reused by specific DAOs
 */
public class GenericDAO<T> extends BaseDAO<T> {

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
            
            // Refresh to get generated ID
            em.refresh(entity);
            
            // ✅ Return ID based on entity type
            if(entity instanceof User) {
                Integer userId = ((User) entity).getUserId();
                return userId != null ? userId : -1;
            } else if(entity instanceof com.gym.model.PasswordResetToken) {
                // ✅ Handle PasswordResetToken
                Long tokenId = ((com.gym.model.PasswordResetToken) entity).getTokenId();
                return tokenId != null ? tokenId.intValue() : -1;
            }
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[GenericDAO] Error saving entity: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return -1;
    }
    
    
    // Read
    /**
     * Find entity by ID (int)
     */
    public T findById(int id) {
        return em.find(entityClass, id);
    }
    
    /**
     * Find entity by ID (Long) - overload for entities with Long primary keys
     * Note: This method returns T directly. For Optional return type, use findByIdOptional(Long)
     */
    protected T findByIdLong(Long id) {
        return em.find(entityClass, id);
    }
    
    /**
     * Find entity by ID (int) and return Optional
     */
    public Optional<T> findByIdOptional(int id) {
        T entity = em.find(entityClass, id);
        return Optional.ofNullable(entity);
    }
    
    /**
     * Find entity by ID (Long) and return Optional
     */
    public Optional<T> findByIdOptional(Long id) {
        T entity = em.find(entityClass, id);
        return Optional.ofNullable(entity);
    }

    /**
     * Find all entities
     */
    public List<T> findAll() {
        return em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e", entityClass)
                .getResultList();
    }
    
    /**
     * Find entity by field name and value
     */
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
    
    /**
     * Find entity by field name and value, return Optional
     */
    public Optional<T> findByFieldOptional(String fieldName, Object value){
        try{
            T entity = em.createQuery("SELECT e FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :val", entityClass)
                     .setParameter("val", value)
                     .getSingleResult();
            return Optional.of(entity);
        } catch(NoResultException e){
            return Optional.empty();
        }
    }
    
    // Update
    public int update(T entity) {
        try {
            beginTransaction();
            T merged = em.merge(entity);  // ✅ Get merged entity
            commitTransaction();
            
            // Copy back the merged state to original entity
            if (entity instanceof User && merged instanceof User) {
                return ((User) merged).getUserId();
            }
        } catch (Exception e) {
            rollbackTransaction();
            System.err.println("[GenericDAO] Error updating entity: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return -1;
    }   
    
    /**
     * Update entity and return boolean
     */
    public boolean updateEntity(T entity) {
        try {
            beginTransaction();
            em.merge(entity);
            commitTransaction();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            return false;
        }
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
    
    /**
     * Delete entity and return boolean
     */
    public boolean deleteEntity(T entity) {
        try {
            beginTransaction();
            em.remove(em.contains(entity) ? entity : em.merge(entity));
            commitTransaction();
            return true;
        } catch (Exception e) {
            rollbackTransaction();
            return false;
        }
    }
}
