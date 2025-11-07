package com.gym.dao;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import com.gym.model.Schedule;
import com.gym.model.ScheduleStatus;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.transaction.Transactional;

public class ScheduleDAO implements IScheduleDAO {

  private final EntityManagerFactory entityManagerFactory;

  public ScheduleDAO() {
    this.entityManagerFactory = Persistence.createEntityManagerFactory("gym-pu");
  }

  @Override
  @Transactional
  public Schedule save(Schedule schedule) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      em.getTransaction().begin();
      em.persist(schedule);
      em.getTransaction().commit();
      return schedule;
    } finally {
      if (em.getTransaction().isActive())
        em.getTransaction().rollback();
      em.close();
    }
  }

  @Override
  @Transactional
  public Schedule create(Schedule schedule) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      em.getTransaction().begin();
      em.persist(schedule);
      em.getTransaction().commit();
      return schedule;
    } finally {
      if (em.getTransaction().isActive())
        em.getTransaction().rollback();
      em.close();
    }
  }

  @Override
  @Transactional
  public Schedule update(Schedule schedule) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      em.getTransaction().begin();
      Schedule merged = em.merge(schedule);
      em.getTransaction().commit();
      return merged;
    } finally {
      if (em.getTransaction().isActive())
        em.getTransaction().rollback();
      em.close();
    }
  }

  @Override
  public List<Schedule> findAll() {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Use JOIN FETCH to eagerly load student and user to avoid lazy loading issues
      return em.createQuery(
          "SELECT s FROM Schedule s " +
              "LEFT JOIN FETCH s.student stu " +
              "LEFT JOIN FETCH stu.user u " +
              "ORDER BY s.trainingDate, s.startTime",
          Schedule.class)
          .getResultList();
    } finally {
      em.close();
    }
  }

  @Override
  public List<Schedule> findByDateRange(java.time.LocalDate start, java.time.LocalDate end) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      // Use JOIN FETCH to eagerly load student and user to avoid lazy loading issues
      return em.createQuery(
          "SELECT s FROM Schedule s " +
              "LEFT JOIN FETCH s.student stu " +
              "LEFT JOIN FETCH stu.user u " +
              "WHERE s.trainingDate >= :start AND s.trainingDate <= :end " +
              "ORDER BY s.trainingDate, s.startTime",
          Schedule.class)
          .setParameter("start", start)
          .setParameter("end", end)
          .getResultList();
    } finally {
      em.close();
    }
  }

  @Override
  public List<Schedule> findByTrainerAndMonth(int trainerId, int year, int month) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      java.time.YearMonth ym = java.time.YearMonth.of(year, month);
      java.time.LocalDate start = ym.atDay(1);
      java.time.LocalDate end = ym.atEndOfMonth();
      // Note: Schedule entity doesn't have trainer field, so we filter by month only
      // All schedules are assumed to belong to the logged-in PT
      // Use JOIN FETCH to eagerly load student and user to avoid lazy loading issues
      return em.createQuery(
          "SELECT s FROM Schedule s " +
              "LEFT JOIN FETCH s.student stu " +
              "LEFT JOIN FETCH stu.user u " +
              "WHERE s.trainingDate >= :start AND s.trainingDate <= :end " +
              "ORDER BY s.trainingDate, s.startTime",
          Schedule.class)
          .setParameter("start", start)
          .setParameter("end", end)
          .getResultList();
    } finally {
      em.close();
    }
  }

  @Override
  public Schedule findById(Integer id) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      return em.find(Schedule.class, id);
    } finally {
      em.close();
    }
  }

  @Override
  @Transactional
  public void updateStatus(Integer id, ScheduleStatus status) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      em.getTransaction().begin();
      Schedule s = em.find(Schedule.class, id);
      if (s != null) {
        s.setStatus(status);
        s.setUpdatedAt(Timestamp.from(Instant.now()));
        em.merge(s);
      }
      em.getTransaction().commit();
    } finally {
      if (em.getTransaction().isActive())
        em.getTransaction().rollback();
      em.close();
    }
  }

  @Override
  @Transactional
  public void deleteById(Integer id) {
    EntityManager em = entityManagerFactory.createEntityManager();
    try {
      em.getTransaction().begin();
      Schedule s = em.find(Schedule.class, id);
      if (s != null) {
        em.remove(s);
      }
      em.getTransaction().commit();
    } finally {
      if (em.getTransaction().isActive())
        em.getTransaction().rollback();
      em.close();
    }
  }
}
