package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.Membership;

/*
    Note: 
 */
public class MembershipDAO {

    private final GenericDAO<Membership> genericDAO;

    public MembershipDAO() {
        genericDAO = new GenericDAO<>(Membership.class);
    }

    public int save(Membership membership) {
        return genericDAO.save(membership);
    }

    public List<Membership> findAll() {
        List<Membership> memberships = genericDAO.findAll();
        return memberships != null ? memberships : new ArrayList<>(List.of());
    }

    public Membership findById(int id) {
        Membership membership = genericDAO.findById(id);
        return membership != null ? membership : null;
    }

    public Membership findByUserId(int id) {
        Membership membership = genericDAO.findByField("name", id);
        return membership;
    }

    public int update(Membership membership) {
        return genericDAO.update(membership);
    }

    public int delete(Membership membership) {
        membership.setStatus("INACTIVE");
        return genericDAO.update(membership);
    }

    public int deleteById(int id) {
        Membership membership = findById(id);
        if (membership != null) {
            membership.setStatus("INACTIVE");
            return genericDAO.update(membership);
        }
        return -1; // Indicate that the membership was not found
    }

    public Membership findByField(String fieldName, Object value) {
        return genericDAO.findByField(fieldName, value);
    }

    /**
     * Lấy tất cả memberships của một member
     * Sử dụng cách lọc từ findAll() để tương thích với GenericDAO hiện tại
     * 
     * @param memberId ID của member
     * @return Danh sách memberships của member
     */
    public List<Membership> findByMemberId(int memberId) {
        try {
            List<Membership> allMemberships = findAll();
            List<Membership> result = new ArrayList<>();
            for (Membership m : allMemberships) {
                if (m.getMember() != null && m.getMember().getId().equals(memberId)) {
                    result.add(m);
                }
            }
            // Sắp xếp theo startDate DESC
            result.sort((m1, m2) -> {
                if (m1.getStartDate() == null || m2.getStartDate() == null) {
                    return 0;
                }
                return m2.getStartDate().compareTo(m1.getStartDate());
            });
            return result;
        } catch (Exception e) {
            System.err.println("[MembershipDAO] Error finding memberships by memberId: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Lấy tất cả memberships ACTIVE của một member (chưa hết hạn)
     * 
     * @param memberId ID của member
     * @return Danh sách memberships ACTIVE của member
     */
    public List<Membership> findActiveByMemberId(int memberId) {
        try {
            Date now = new Date();
            List<Membership> allMemberships = findAll();
            List<Membership> result = new ArrayList<>();
            for (Membership m : allMemberships) {
                if (m.getMember() != null && 
                    m.getMember().getId().equals(memberId) &&
                    "ACTIVE".equalsIgnoreCase(m.getStatus()) &&
                    m.getEndDate() != null &&
                    m.getEndDate().after(now)) {
                    result.add(m);
                }
            }
            // Sắp xếp theo endDate DESC
            result.sort((m1, m2) -> {
                if (m1.getEndDate() == null || m2.getEndDate() == null) {
                    return 0;
                }
                return m2.getEndDate().compareTo(m1.getEndDate());
            });
            return result;
        } catch (Exception e) {
            System.err.println("[MembershipDAO] Error finding active memberships by memberId: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Tìm membership ACTIVE của member với package cụ thể
     * 
     * @param memberId ID của member
     * @param packageId ID của package
     * @return Membership ACTIVE hoặc null nếu không tìm thấy
     */
    public Membership findActiveByMemberIdAndPackageId(int memberId, int packageId) {
        try {
            Date now = new Date();
            List<Membership> allMemberships = findAll();
            for (Membership m : allMemberships) {
                if (m.getMember() != null && 
                    m.getMember().getId().equals(memberId) &&
                    m.getPackageO() != null &&
                    m.getPackageO().getId().equals(packageId) &&
                    "ACTIVE".equalsIgnoreCase(m.getStatus()) &&
                    m.getEndDate() != null &&
                    m.getEndDate().after(now)) {
                    return m;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("[MembershipDAO] Error finding membership by memberId and packageId: " + e.getMessage());
            return null;
        }
    }
}
