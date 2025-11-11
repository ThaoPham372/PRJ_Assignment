package model.report;

import java.math.BigDecimal;

/**
 * Model for top spender statistics
 * Represents a member with their total spending amount
 */
public class TopSpender {
    private Integer memberId;
    private String username;
    private String name;
    private BigDecimal totalSpent;

    public TopSpender() {
    }

    public TopSpender(Integer memberId, String username, String name, BigDecimal totalSpent) {
        this.memberId = memberId;
        this.username = username;
        this.name = name;
        this.totalSpent = totalSpent;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }
}

