package com.gym.model;

import java.math.BigDecimal;

/**
 * DTO class for daily nutrition totals
 * Used to return aggregated nutrition data for a day
 */
public class DailyIntakeDTO {
    private BigDecimal caloriesKcal;
    private BigDecimal proteinG;
    private BigDecimal carbsG;
    private BigDecimal fatG;

    // Constructors
    public DailyIntakeDTO() {
        this.caloriesKcal = BigDecimal.ZERO;
        this.proteinG = BigDecimal.ZERO;
        this.carbsG = BigDecimal.ZERO;
        this.fatG = BigDecimal.ZERO;
    }

    public DailyIntakeDTO(BigDecimal caloriesKcal, BigDecimal proteinG) {
        this.caloriesKcal = caloriesKcal != null ? caloriesKcal : BigDecimal.ZERO;
        this.proteinG = proteinG != null ? proteinG : BigDecimal.ZERO;
        this.carbsG = BigDecimal.ZERO;
        this.fatG = BigDecimal.ZERO;
    }

    public DailyIntakeDTO(BigDecimal caloriesKcal, BigDecimal proteinG, BigDecimal carbsG, BigDecimal fatG) {
        this.caloriesKcal = caloriesKcal != null ? caloriesKcal : BigDecimal.ZERO;
        this.proteinG = proteinG != null ? proteinG : BigDecimal.ZERO;
        this.carbsG = carbsG != null ? carbsG : BigDecimal.ZERO;
        this.fatG = fatG != null ? fatG : BigDecimal.ZERO;
    }

    // Getters and Setters
    public BigDecimal getCaloriesKcal() {
        return caloriesKcal;
    }

    public void setCaloriesKcal(BigDecimal caloriesKcal) {
        this.caloriesKcal = caloriesKcal != null ? caloriesKcal : BigDecimal.ZERO;
    }

    public BigDecimal getProteinG() {
        return proteinG;
    }

    public void setProteinG(BigDecimal proteinG) {
        this.proteinG = proteinG != null ? proteinG : BigDecimal.ZERO;
    }

    public BigDecimal getCarbsG() {
        return carbsG;
    }

    public void setCarbsG(BigDecimal carbsG) {
        this.carbsG = carbsG != null ? carbsG : BigDecimal.ZERO;
    }

    public BigDecimal getFatG() {
        return fatG;
    }

    public void setFatG(BigDecimal fatG) {
        this.fatG = fatG != null ? fatG : BigDecimal.ZERO;
    }
}

