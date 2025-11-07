package com.gym.dao;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

import com.gym.model.Trainer;

/**
 * TrainerDAO Interface
 * Äá»‹nh nghÄ©a cÃ¡c phÆ°Æ¡ng thá»©c truy váº¥n dá»¯ liá»‡u cho entity Trainer
 */
public interface ITrainerDAO {

  /**
   * Láº¥y táº¥t cáº£ trainer tá»« database
   * 
   * @return Danh sÃ¡ch táº¥t cáº£ trainer
   */
  List<Trainer> getAllTrainers();

  /**
   * Láº¥y trainer theo user_id
   * 
   * @param id user_id cá»§a trainer
   * @return Trainer object hoáº·c null náº¿u khÃ´ng tÃ¬m tháº¥y
   */
  Trainer getTrainerById(int id);

  /**
   * Láº¥y danh sÃ¡ch trainer cÃ³ Ä‘Ã¡nh giÃ¡ cao nháº¥t
   * 
   * @param limit Sá»‘ lÆ°á»£ng trainer cáº§n láº¥y
   * @return Danh sÃ¡ch trainer Ä‘Æ°á»£c sáº¯p xáº¿p theo rating giáº£m dáº§n
   */
  List<Trainer> getTopRatedTrainers(int limit);

  /**
   * Láº¥y thá»‘ng kÃª trainer trong khoáº£ng thá»i gian
   * 
   * @param from NgÃ y báº¯t Ä‘áº§u
   * @param to   NgÃ y káº¿t thÃºc
   * @return Danh sÃ¡ch trainer vá»›i thá»‘ng kÃª trong khoáº£ng thá»i gian
   */
  List<Trainer> getTrainerStatistics(LocalDate from, LocalDate to);

  /**
   * Láº¥y danh sÃ¡ch trainer vá»›i bÃ¡o cÃ¡o tiáº¿n Ä‘á»™ há»c viÃªn
   * 
   * @return Danh sÃ¡ch trainer kÃ¨m thÃ´ng tin tiáº¿n Ä‘á»™ há»c viÃªn
   */
  List<Trainer> getTrainersWithProgressReport();

  /**
   * Láº¥y sá»‘ buá»•i táº­p theo thÃ¡ng (Ä‘á»ƒ táº¡o biá»ƒu Ä‘á»“ cá»™t)
   * 
   * @param months Sá»‘ thÃ¡ng cáº§n láº¥y (tÃ­nh tá»« thÃ¡ng hiá»‡n táº¡i vá» trÆ°á»›c)
   * @return Danh sÃ¡ch Map chá»©a: month (String), year (Integer), monthNumber
   *         (Integer), count (Long)
   */
  List<java.util.Map<String, Object>> getSessionsByMonth(int months);

  /**
   * Láº¥y phÃ¢n loáº¡i loáº¡i hÃ¬nh táº­p luyá»‡n (Ä‘á»ƒ táº¡o biá»ƒu Ä‘á»“ trÃ²n)
   * 
   * @return Danh sÃ¡ch Map chá»©a: trainingType (String), count (Long)
   */
  List<java.util.Map<String, Object>> getTrainingTypeDistribution();

  /**
   * Láº¥y tá»· lá»‡ hoÃ n thÃ nh buá»•i táº­p theo tuáº§n (Ä‘á»ƒ táº¡o biá»ƒu Ä‘á»“ vÃ¹ng)
   * 
   * @param weeks Sá»‘ tuáº§n cáº§n láº¥y (tÃ­nh tá»« tuáº§n hiá»‡n táº¡i vá» trÆ°á»›c)
   * @return Danh sÃ¡ch Map chá»©a: week (String), weekNumber (Integer),
   *         completionRate (Double)
   */
  List<java.util.Map<String, Object>> getCompletionRateByWeek(int weeks);

  /**
   * Láº¥y xu hÆ°á»›ng há»c viÃªn má»›i theo thÃ¡ng (Ä‘á»ƒ táº¡o biá»ƒu Ä‘á»“ Ä‘Æ°á»ng)
   * 
   * @param months Sá»‘ thÃ¡ng cáº§n láº¥y (tÃ­nh tá»« thÃ¡ng hiá»‡n táº¡i vá» trÆ°á»›c)
   * @return Danh sÃ¡ch Map chá»©a: month (String), year (Integer), monthNumber
   *         (Integer), count (Long)
   */
  List<java.util.Map<String, Object>> getNewStudentsTrendByMonth(int months);

  /**
   * Äáº¿m sá»‘ buá»•i táº­p hoÃ n thÃ nh cá»§a trainer trong khoáº£ng thá»i gian
   * 
   * @param trainerId ID cá»§a trainer
   * @param from      NgÃ y báº¯t Ä‘áº§u
   * @param to        NgÃ y káº¿t thÃºc
   * @return Sá»‘ buá»•i hoÃ n thÃ nh
   */
  int countCompletedSessions(int trainerId, LocalDate from, LocalDate to);

  /**
   * Äáº¿m sá»‘ buá»•i táº­p bá»‹ há»§y cá»§a trainer trong khoáº£ng thá»i gian
   * 
   * @param trainerId ID cá»§a trainer
   * @param from      NgÃ y báº¯t Ä‘áº§u
   * @param to        NgÃ y káº¿t thÃºc
   * @return Sá»‘ buá»•i bá»‹ há»§y
   */
  int countCancelledSessions(int trainerId, LocalDate from, LocalDate to);

  /**
   * TÃ­nh trung bÃ¬nh rating cá»§a trainer trong khoáº£ng thá»i gian
   * 
   * @param trainerId ID cá»§a trainer
   * @param from      NgÃ y báº¯t Ä‘áº§u
   * @param to        NgÃ y káº¿t thÃºc
   * @return Trung bÃ¬nh rating (0.0 náº¿u khÃ´ng cÃ³ dá»¯ liá»‡u)
   */
  float calculateAverageRating(int trainerId, LocalDate from, LocalDate to);

  /**
   * Láº¥y phÃ¢n bá»• loáº¡i hÃ¬nh táº­p luyá»‡n cá»§a trainer
   * 
   * @param trainerId ID cá»§a trainer
   * @param from      NgÃ y báº¯t Ä‘áº§u
   * @param to        NgÃ y káº¿t thÃºc
   * @return Map vá»›i key lÃ  training_type, value lÃ  sá»‘ lÆ°á»£ng buá»•i táº­p
   */
  java.util.Map<String, Long> getTrainingTypeDistribution(int trainerId, LocalDate from, LocalDate to);

  /**
   * Láº¥y sá»‘ buá»•i táº­p theo tá»«ng thÃ¡ng cá»§a trainer
   * 
   * @param trainerId ID cá»§a trainer
   * @return Map vá»›i key lÃ  YearMonth, value lÃ  sá»‘ lÆ°á»£ng buá»•i táº­p
   */
  java.util.Map<java.time.YearMonth, Long> getMonthlySessionCount(int trainerId);

  /**
   * Láº¥y tá»· lá»‡ hoÃ n thÃ nh theo tá»«ng tuáº§n cá»§a trainer
   * 
   * @param trainerId ID cá»§a trainer
   * @return Map vá»›i key lÃ  sá»‘ tuáº§n (1-52), value lÃ  tá»· lá»‡ hoÃ n thÃ nh (0.0-1.0)
   */
  java.util.Map<Integer, Float> getWeeklyCompletionRate(int trainerId);

  /**
   * Äáº¿m sá»‘ buá»•i táº­p hoÃ n thÃ nh vá»›i filter theo gÃ³i táº­p vÃ  loáº¡i hÃ¬nh táº­p
   * 
   * @param trainerId    ID cá»§a trainer
   * @param from         NgÃ y báº¯t Ä‘áº§u
   * @param to           NgÃ y káº¿t thÃºc
   * @param packageName  TÃªn gÃ³i táº­p (null náº¿u khÃ´ng filter)
   * @param trainingType Loáº¡i hÃ¬nh táº­p (null náº¿u khÃ´ng filter)
   * @return Sá»‘ buá»•i hoÃ n thÃ nh
   */
  int countCompletedSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
      String trainingType);

  /**
   * Äáº¿m sá»‘ buá»•i táº­p bá»‹ há»§y vá»›i filter theo gÃ³i táº­p vÃ  loáº¡i hÃ¬nh táº­p
   * 
   * @param trainerId    ID cá»§a trainer
   * @param from         NgÃ y báº¯t Ä‘áº§u
   * @param to           NgÃ y káº¿t thÃºc
   * @param packageName  TÃªn gÃ³i táº­p (null náº¿u khÃ´ng filter)
   * @param trainingType Loáº¡i hÃ¬nh táº­p (null náº¿u khÃ´ng filter)
   * @return Sá»‘ buá»•i bá»‹ há»§y
   */
  int countCancelledSessionsWithFilter(int trainerId, LocalDate from, LocalDate to, String packageName,
      String trainingType);

  /**
   * Láº¥y Ä‘Ã¡nh giÃ¡ trung bÃ¬nh theo tá»«ng thÃ¡ng cá»§a trainer trong nÄƒm
   * 
   * @param trainerId ID cá»§a trainer
   * @param year      NÄƒm cáº§n láº¥y
   * @return Map vá»›i key lÃ  YearMonth, value lÃ  Ä‘Ã¡nh giÃ¡ trung bÃ¬nh
   */
  Map<YearMonth, Float> getMonthlyAverageRating(int trainerId, int year);

  /**
   * Láº¥y sá»‘ buá»•i táº­p hoÃ n thÃ nh theo tuáº§n Ä‘á»ƒ phÃ¢n tÃ­ch hiá»‡u suáº¥t
   * 
   * @param trainerId ID cá»§a trainer
   * @param weeks     Sá»‘ tuáº§n cáº§n láº¥y
   * @return Map vá»›i key lÃ  sá»‘ tuáº§n, value lÃ  sá»‘ buá»•i hoÃ n thÃ nh
   */
  Map<Integer, Integer> getWeeklyCompletedSessions(int trainerId, int weeks);

  /**
   * Láº¥y sá»‘ buá»•i táº­p hoÃ n thÃ nh theo thÃ¡ng Ä‘á»ƒ phÃ¢n tÃ­ch hiá»‡u suáº¥t
   * 
   * @param trainerId ID cá»§a trainer
   * @param months    Sá»‘ thÃ¡ng cáº§n láº¥y
   * @return Map vá»›i key lÃ  YearMonth, value lÃ  sá»‘ buá»•i hoÃ n thÃ nh
   */
  Map<YearMonth, Integer> getMonthlyCompletedSessions(int trainerId, int months);
}
