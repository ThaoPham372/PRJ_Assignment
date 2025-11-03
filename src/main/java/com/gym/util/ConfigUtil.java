package com.gym.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigUtil {

    private static final Properties dbProps = new Properties();
    private static final Properties gymInfoProps = new Properties();
    private static final String DB_PROPERTIES_FILE = "db.properties";
    private static final String GYM_INFO_PROPERTIES_FILE = "gym-info.properties";

    static {
        loadDbProperties();
        loadGymInfoProperties();
    }

    private static void loadDbProperties() {
        try (InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream(DB_PROPERTIES_FILE);
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            
            if (is == null) {
                throw new IllegalStateException(DB_PROPERTIES_FILE + " not found on classpath");
            }
            dbProps.load(reader);
        } catch (Exception e) {
            System.err.println("Failed to load " + DB_PROPERTIES_FILE + ": " + e.getMessage());
            throw new RuntimeException("Failed to load database config", e);
        }
    }

    private static void loadGymInfoProperties() {
        try (InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream(GYM_INFO_PROPERTIES_FILE);
             InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            
            if (is == null) {
                System.err.println("⚠️ Không tìm thấy file " + GYM_INFO_PROPERTIES_FILE + " trong classpath!");
                return;
            }
            gymInfoProps.load(reader);
        } catch (Exception e) {
            System.err.println("Failed to load " + GYM_INFO_PROPERTIES_FILE + ": " + e.getMessage());
        }
    }

    /**
     * Lấy property từ db.properties, ưu tiên biến môi trường
     */
    public static String getProperty(String key) {
        // Ưu tiên biến môi trường
        String value = System.getenv(key);
        if (value == null) {
            // Nếu không có thì lấy từ file properties
            value = dbProps.getProperty(key);
        }
        if (value == null) {
            throw new RuntimeException("Missing required property: " + key);
        }
        return value;
    }

    /**
     * Lấy thông tin gym từ gym-info.properties
     */
    public static String getGymInfo(String key) {
        return gymInfoProps.getProperty(key, "Chưa có thông tin");
    }

    /**
     * Lấy tất cả thông tin gym dưới dạng chuỗi formatted
     */
    public static String getAllGymInfo() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("Tên phòng: ").append(getGymInfo("ten")).append("\n");
        sb.append("Địa chỉ: ").append(getGymInfo("diachi")).append("\n");
        sb.append("Giờ mở cửa: ").append(getGymInfo("thoigian")).append("\n");
        sb.append("PT nổi bật: ").append(getGymInfo("pt")).append("\n");
        sb.append("Các gói tập: ").append(getGymInfo("goi")).append("\n");
        sb.append("Ưu đãi: ").append(getGymInfo("uudai")).append("\n");
        sb.append("Dịch vụ: ").append(getGymInfo("dich_vu")).append("\n");
        sb.append("Thiết bị: ").append(getGymInfo("thiet_bi")).append("\n");
        sb.append("Lưu ý: ").append(getGymInfo("luu_y")).append("\n");
        sb.append("Hotline: ").append(getGymInfo("hotline"));
        
        return sb.toString();
    }
}
