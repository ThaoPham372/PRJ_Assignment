package com.gym.dao;

import com.gym.model.GymInfo;
import jakarta.persistence.EntityManager;

import java.util.List;

public class GymInfoDAO {

    
    public String loadGymInfo() {
        StringBuilder sb = new StringBuilder(); 
        EntityManager em = null;
        try {
            em = com.gym.util.JPAUtil.createEntityManager();

            List<GymInfo> gyms = em.createQuery(
                    "SELECT g FROM GymInfo g ORDER BY g.createdDate DESC", GymInfo.class)
                    .getResultList();

            if (gyms == null || gyms.isEmpty()) {
                sb.append("⚠️ Không có thông tin phòng gym trong cơ sở dữ liệu!\n");
            } else {
                sb.append("🏢 DANH SÁCH CÁC CƠ SỞ GYMFIT:\n\n");
                for (GymInfo gym : gyms) {
                    sb.append("📍 CƠ SỞ: ").append(gym.getName()).append("\n");
                    sb.append("   - Địa chỉ: ").append(gym.getAddress()).append("\n");
                    sb.append("   - Hotline: ").append(gym.getHotline()).append("\n");
                    // Chỉ hiện Email nếu có
                    if (gym.getEmail() != null && !gym.getEmail().isEmpty()) {
                        sb.append("   - Email: ").append(gym.getEmail()).append("\n");
                    }
                    sb.append("-------------------------------------\n");
                }
            }

            sb.append("\n️ CÁC GÓI TẬP ÁP DỤNG TOÀN HỆ THỐNG:\n");

            List<com.gym.model.membership.Package> packages = em.createQuery(
                    "SELECT p FROM Package p WHERE p.isActive = true ORDER BY p.price ASC",
                    com.gym.model.membership.Package.class)
                    .getResultList();

            if (packages == null || packages.isEmpty()) {
                sb.append("   ⚠️ Hiện chưa có gói tập nào được thiết lập.\n");
            } else {
                for (com.gym.model.membership.Package pkg : packages) {
                    sb.append("✅ Gói: ").append(pkg.getName().toUpperCase()).append("\n");
                    sb.append("   ▪️ Thời hạn: ").append(pkg.getDurationMonths()).append(" tháng\n");

                    // Format giá tiền cho đẹp (ví dụ: 5,000,000 VND)
                    sb.append("   ▪️ Giá: ")
                            .append(pkg.getPrice() != null ? String.format("%,.0f", pkg.getPrice()) : "Liên hệ")
                            .append(" VND\n");

                    sb.append("   ▪️ Quyền lợi: ")
                            .append(pkg.hasUnlimitedSessions() ? "Tập không giới hạn" : pkg.getMaxSessions() + " buổi tập")
                            .append("\n");

                    if (pkg.getDescription() != null && !pkg.getDescription().isEmpty()) {
                        sb.append("   ▪️ Mô tả: ").append(pkg.getDescription()).append("\n");
                    }
                    sb.append("\n"); // Xuống dòng giữa các gói cho dễ đọc
                }
            }

            sb.append("=====================================\n");

        } catch (Exception e) {
            e.printStackTrace();
            sb.append("⚠️ Lỗi khi đọc thông tin hệ thống: ").append(e.getMessage());
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return sb.toString();
    }


}