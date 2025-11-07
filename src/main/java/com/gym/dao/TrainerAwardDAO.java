package com.gym.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.gym.model.TrainerAward;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

/**
 * TrainerAwardDAO Implementation
 * Thực thi các phương thức truy vấn dữ liệu cho entity TrainerAward sử dụng JPA
 */
public class TrainerAwardDAO implements ITrainerAwardDAO {

  private final EntityManagerFactory entityManagerFactory;

  public TrainerAwardDAO() {
    this.entityManagerFactory = Persistence.createEntityManagerFactory("gym-pu");
  }

  @Override
  public TrainerAward save(TrainerAward award) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      em.getTransaction().begin();
      if (award.getId() == null) {
        em.persist(award);
      } else {
        award = em.merge(award);
      }
      em.getTransaction().commit();
      return award;
    } catch (Exception ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      System.err.println("Error saving trainer award: " + ex.getMessage());
      ex.printStackTrace();
      return null;
    } finally {
      em.close();
    }
  }

  @Override
  public List<TrainerAward> findByTrainerId(int trainerId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      TypedQuery<TrainerAward> query = em.createQuery(
          "SELECT ta FROM TrainerAward ta WHERE ta.trainer.userId = :trainerId ORDER BY ta.awardedMonth DESC",
          TrainerAward.class);
      query.setParameter("trainerId", trainerId);
      return query.getResultList();
    } catch (Exception ex) {
      System.err.println("Error finding trainer awards by trainerId: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public List<TrainerAward> findByTrainerIdAndMonth(int trainerId, LocalDate month) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      LocalDate monthStart = month.withDayOfMonth(1);
      LocalDate monthEnd = month.withDayOfMonth(month.lengthOfMonth());

      TypedQuery<TrainerAward> query = em.createQuery(
          "SELECT ta FROM TrainerAward ta WHERE ta.trainer.userId = :trainerId "
              + "AND ta.awardedMonth >= :monthStart AND ta.awardedMonth <= :monthEnd "
              + "ORDER BY ta.awardedMonth DESC",
          TrainerAward.class);
      query.setParameter("trainerId", trainerId);
      query.setParameter("monthStart", monthStart);
      query.setParameter("monthEnd", monthEnd);
      return query.getResultList();
    } catch (Exception ex) {
      System.err.println("Error finding trainer awards by trainerId and month: " + ex.getMessage());
      ex.printStackTrace();
      return new ArrayList<>();
    } finally {
      em.close();
    }
  }

  @Override
  public boolean existsByTrainerIdAndAwardNameAndMonth(int trainerId, String awardName, LocalDate month) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      LocalDate monthStart = month.withDayOfMonth(1);
      LocalDate monthEnd = month.withDayOfMonth(month.lengthOfMonth());

      TypedQuery<Long> query = em.createQuery(
          "SELECT COUNT(ta) FROM TrainerAward ta WHERE ta.trainer.userId = :trainerId "
              + "AND ta.awardName = :awardName "
              + "AND ta.awardedMonth >= :monthStart AND ta.awardedMonth <= :monthEnd",
          Long.class);
      query.setParameter("trainerId", trainerId);
      query.setParameter("awardName", awardName);
      query.setParameter("monthStart", monthStart);
      query.setParameter("monthEnd", monthEnd);

      Long count = query.getSingleResult();
      return count != null && count > 0;
    } catch (Exception ex) {
      System.err.println("Error checking existence of trainer award: " + ex.getMessage());
      ex.printStackTrace();
      return false;
    } finally {
      em.close();
    }
  }

  @Override
  public void deleteById(int id) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      em.getTransaction().begin();
      TrainerAward award = em.find(TrainerAward.class, id);
      if (award != null) {
        em.remove(award);
      }
      em.getTransaction().commit();
    } catch (Exception ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      System.err.println("Error deleting trainer award by id: " + ex.getMessage());
      ex.printStackTrace();
    } finally {
      em.close();
    }
  }

  @Override
  public void deleteByTrainerId(int trainerId) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      em.getTransaction().begin();
      Query query = em.createQuery("DELETE FROM TrainerAward ta WHERE ta.trainer.userId = :trainerId");
      query.setParameter("trainerId", trainerId);
      query.executeUpdate();
      em.getTransaction().commit();
    } catch (Exception ex) {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      System.err.println("Error deleting trainer awards by trainerId: " + ex.getMessage());
      ex.printStackTrace();
    } finally {
      em.close();
    }
  }
}

