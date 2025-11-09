package model;

import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) class for daily nutrition totals
 * Used to return aggregated nutrition data for a day
 * Follows DTO pattern - immutable data container with no business logic
 */
public class DailyIntakeDTO {
    private BigDecimal caloriesKcal;
    private BigDecimal proteinG;
    private BigDecimal carbsG;
    private BigDecimal fatG;

    // Constants for default values
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    // Constructors
    /**
     * Default constructor - initializes all values to zero
     */
    public DailyIntakeDTO() {
        this.caloriesKcal = ZERO;
        this.proteinG = ZERO;
        this.carbsG = ZERO;
        this.fatG = ZERO;
    }

    /**
     * Constructor with calories and protein only
     * @param caloriesKcal daily calories in kcal
     * @param proteinG daily protein in grams
     */
    public DailyIntakeDTO(BigDecimal caloriesKcal, BigDecimal proteinG) {
        this.caloriesKcal = ensureNotNull(caloriesKcal);
        this.proteinG = ensureNotNull(proteinG);
        this.carbsG = ZERO;
        this.fatG = ZERO;
    }

    /**
     * Full constructor with all nutrition values
     * @param caloriesKcal daily calories in kcal
     * @param proteinG daily protein in grams
     * @param carbsG daily carbohydrates in grams
     * @param fatG daily fat in grams
     */
    public DailyIntakeDTO(BigDecimal caloriesKcal, BigDecimal proteinG, BigDecimal carbsG, BigDecimal fatG) {
        this.caloriesKcal = ensureNotNull(caloriesKcal);
        this.proteinG = ensureNotNull(proteinG);
        this.carbsG = ensureNotNull(carbsG);
        this.fatG = ensureNotNull(fatG);
    }
    
    /**
     * Helper method to ensure BigDecimal is never null
     * Follows Null Object Pattern
     * @param value BigDecimal value to check
     * @return value if not null, otherwise BigDecimal.ZERO
     */
    private BigDecimal ensureNotNull(BigDecimal value) {
        return value != null ? value : ZERO;
    }

    // Getters and Setters
    public BigDecimal getCaloriesKcal() {
        return caloriesKcal;
    }

    public void setCaloriesKcal(BigDecimal caloriesKcal) {
        this.caloriesKcal = ensureNotNull(caloriesKcal);
    }

    public BigDecimal getProteinG() {
        return proteinG;
    }

    public void setProteinG(BigDecimal proteinG) {
        this.proteinG = ensureNotNull(proteinG);
    }

    public BigDecimal getCarbsG() {
        return carbsG;
    }

    public void setCarbsG(BigDecimal carbsG) {
        this.carbsG = ensureNotNull(carbsG);
    }

    public BigDecimal getFatG() {
        return fatG;
    }

    public void setFatG(BigDecimal fatG) {
        this.fatG = ensureNotNull(fatG);
    }
}

