package com.gym.dto;

import java.io.Serializable;

/**
 * DTO cho phân tích hiệu suất theo tuần/tháng
 */
public class PerformanceTrendDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private int weekNumber;
  private int monthNumber;
  private int year;
  private int completedSessions;
  private double changePercent;
  private TrendDirection trendDirection;

  public enum TrendDirection {
    UP, DOWN, STABLE
  }

  public PerformanceTrendDTO() {
  }

  public PerformanceTrendDTO(int weekNumber, int completedSessions, double changePercent,
      TrendDirection trendDirection) {
    this.weekNumber = weekNumber;
    this.completedSessions = completedSessions;
    this.changePercent = changePercent;
    this.trendDirection = trendDirection;
  }

  public PerformanceTrendDTO(int monthNumber, int year, int completedSessions, double changePercent,
      TrendDirection trendDirection) {
    this.monthNumber = monthNumber;
    this.year = year;
    this.completedSessions = completedSessions;
    this.changePercent = changePercent;
    this.trendDirection = trendDirection;
  }

  // Getters and Setters
  public int getWeekNumber() {
    return weekNumber;
  }

  public void setWeekNumber(int weekNumber) {
    this.weekNumber = weekNumber;
  }

  public int getMonthNumber() {
    return monthNumber;
  }

  public void setMonthNumber(int monthNumber) {
    this.monthNumber = monthNumber;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getCompletedSessions() {
    return completedSessions;
  }

  public void setCompletedSessions(int completedSessions) {
    this.completedSessions = completedSessions;
  }

  public double getChangePercent() {
    return changePercent;
  }

  public void setChangePercent(double changePercent) {
    this.changePercent = changePercent;
  }

  public TrendDirection getTrendDirection() {
    return trendDirection;
  }

  public void setTrendDirection(TrendDirection trendDirection) {
    this.trendDirection = trendDirection;
  }
}

