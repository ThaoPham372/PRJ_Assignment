package model.report;

import java.math.BigDecimal;

public class ReportSummary{

    private long totalMembers;
    private long newMembersThisMonth;
    private double memberGrowthRate;

    private BigDecimal revenueThisMonth;
    private BigDecimal revenueLastMonth;
    private double revenueGrowthRate;

    private int checkInsToday;
    private double avgCheckIns;

    public ReportSummary() {
        this.revenueThisMonth = BigDecimal.ZERO;
        this.revenueLastMonth = BigDecimal.ZERO;
    }
    

    public long getTotalMembers() { return totalMembers; }
    public void setTotalMembers(long totalMembers) { this.totalMembers = totalMembers; }
    public long getNewMembersThisMonth() { return newMembersThisMonth; }
    public void setNewMembersThisMonth(long newMembersThisMonth) { this.newMembersThisMonth = newMembersThisMonth; }
    public double getMemberGrowthRate() { return memberGrowthRate; }
    public void setMemberGrowthRate(double memberGrowthRate) { this.memberGrowthRate = memberGrowthRate; }
    public BigDecimal getRevenueThisMonth() { return revenueThisMonth; }
    public void setRevenueThisMonth(BigDecimal revenueThisMonth) { this.revenueThisMonth = revenueThisMonth; }
    public BigDecimal getRevenueLastMonth() { return revenueLastMonth; }
    public void setRevenueLastMonth(BigDecimal revenueLastMonth) { this.revenueLastMonth = revenueLastMonth; }
    public double getRevenueGrowthRate() { return revenueGrowthRate; }
    public void setRevenueGrowthRate(double revenueGrowthRate) { this.revenueGrowthRate = revenueGrowthRate; }
    public int getCheckInsToday() { return checkInsToday; }
    public void setCheckInsToday(int checkInsToday) { this.checkInsToday = checkInsToday; }
    public double getAvgCheckIns() { return avgCheckIns; }
    public void setAvgCheckIns(double avgCheckIns) { this.avgCheckIns = avgCheckIns; }
}