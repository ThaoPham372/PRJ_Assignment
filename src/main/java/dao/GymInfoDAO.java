package dao;

import model.GymInfo;
import model.Package;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GymInfoDAO - Data Access Object for GymInfo
 * Sử dụng GenericDAO để tái sử dụng code
 */
public class GymInfoDAO {

    GenericDAO<GymInfo> genericDAO;

    public GymInfoDAO() {
        genericDAO = new GenericDAO<>(GymInfo.class);
    }

    public int save(GymInfo gymInfo) {
        genericDAO.save(gymInfo);
        return gymInfo.getGymId() != null ? gymInfo.getGymId().intValue() : -1;
    }

    public List<GymInfo> findAll() {
        List<GymInfo> gyms = genericDAO.findAll();
        return gyms != null ? gyms : List.of();
    }

    public List<GymInfo> findAllOrderByCreatedDate() {
        List<GymInfo> gyms = findAll();
        return gyms.stream()
                .sorted((g1, g2) -> {
                    if (g1.getCreatedDate() == null && g2.getCreatedDate() == null) return 0;
                    if (g1.getCreatedDate() == null) return 1;
                    if (g2.getCreatedDate() == null) return -1;
                    return g2.getCreatedDate().compareTo(g1.getCreatedDate()); // DESC
                })
                .collect(Collectors.toList());
    }

    public GymInfo findById(int id) {
        return genericDAO.findById(id);
    }

    public int update(GymInfo gymInfo) {
        return genericDAO.update(gymInfo);
    }

    public int delete(GymInfo gymInfo) {
        return genericDAO.delete(gymInfo);
    }

    /**
     * Load gym info và packages để format thành string
     * Sử dụng PackageDAO để lấy packages
     */
    public String loadGymInfo() {
        StringBuilder sb = new StringBuilder();
        try {
            // Lấy gyms từ GenericDAO
            List<GymInfo> gyms = findAllOrderByCreatedDate();

            if (gyms == null || gyms.isEmpty()) {
                sb.append("⚠️ Không có thông tin phòng gym trong cơ sở dữ liệu!\n");
            } else {
                sb.append("🏢 DANH SÁCH CÁC CƠ SỞ GYMFIT:\n\n");
                for (GymInfo gym : gyms) {
                    sb.append("📍 CƠ SỞ: ").append(gym.getName() != null ? gym.getName() : "N/A").append("\n");
                    sb.append("   - Địa chỉ: ").append(gym.getAddress() != null ? gym.getAddress() : "N/A").append("\n");
                    sb.append("   - Hotline: ").append(gym.getHotline() != null ? gym.getHotline() : "N/A").append("\n");
                    if (gym.getEmail() != null && !gym.getEmail().isEmpty()) {
                        sb.append("   - Email: ").append(gym.getEmail()).append("\n");
                    }
                    sb.append("-------------------------------------\n");
                }
            }

            sb.append("\n️ CÁC GÓI TẬP ÁP DỤNG TOÀN HỆ THỐNG:\n");

            // Sử dụng PackageDAO để lấy packages
            PackageDAO packageDAO = new PackageDAO();
            List<Package> packages = packageDAO.findAll().stream()
                    .filter(p -> p.getIsActive() != null && p.getIsActive())
                    .sorted((p1, p2) -> {
                        if (p1.getPrice() == null && p2.getPrice() == null) return 0;
                        if (p1.getPrice() == null) return 1;
                        if (p2.getPrice() == null) return -1;
                        return p1.getPrice().compareTo(p2.getPrice()); // ASC
                    })
                    .collect(Collectors.toList());

            if (packages == null || packages.isEmpty()) {
                sb.append("   ⚠️ Hiện chưa có gói tập nào được thiết lập.\n");
            } else {
                for (Package pkg : packages) {
                    sb.append("✅ Gói: ").append(pkg.getName() != null ? pkg.getName().toUpperCase() : "N/A").append("\n");
                    sb.append("   ▪️ Thời hạn: ").append(pkg.getDurationMonths()).append(" tháng\n");

                    // Format giá tiền
                    if (pkg.getPrice() != null) {
                        sb.append("   ▪️ Giá: ")
                                .append(String.format("%,.0f", pkg.getPrice().doubleValue()))
                                .append(" VND\n");
                    } else {
                        sb.append("   ▪️ Giá: Liên hệ\n");
                    }

                    // Quyền lợi
                    if (pkg.getMaxSessions() == null || pkg.getMaxSessions() == 0) {
                        sb.append("   ▪️ Quyền lợi: Tập không giới hạn\n");
                    } else {
                        sb.append("   ▪️ Quyền lợi: ").append(pkg.getMaxSessions()).append(" buổi tập\n");
                    }

                    if (pkg.getDescription() != null && !pkg.getDescription().isEmpty()) {
                        sb.append("   ▪️ Mô tả: ").append(pkg.getDescription()).append("\n");
                    }
                    sb.append("\n");
                }
            }

            sb.append("=====================================\n");

        } catch (Exception e) {
            e.printStackTrace();
            sb.append("⚠️ Lỗi khi đọc thông tin hệ thống: ").append(e.getMessage());
        }
        return sb.toString();
    }
}
