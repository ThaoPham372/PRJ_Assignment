package service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import Utils.ConfigManager;
import dao.GymInfoDAO;
import dao.PackageDAO;
import model.GymInfo;
import model.Package;
import model.ai.AIResponse;
import model.ai.GeminiRequest;
import model.ai.GeminiResponse;
import model.ai.RequestPayload;

public class ChatAIService {

    private static final String API_KEY = ConfigManager.getInstance().getProperty("GEMINI_API_KEY");
    private static final String API_URL
            = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + API_KEY;

    private final HttpClient httpClient;
    private final Gson gson;
    private final GymInfoDAO gymInfoDAO;
    private final PackageDAO packageDAO;
    
    // Cache thông tin gym để tránh query DB nhiều lần
    private static String cachedGymInfo = null;
    private static long lastCacheTime = 0;
    private static final long CACHE_DURATION = 30 * 60 * 1000; // 30 phút

    public ChatAIService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
        this.gson = new Gson();
        this.gymInfoDAO = new GymInfoDAO();
        this.packageDAO = new PackageDAO();
    }

    /**
     * Get AI response without member context (for guests)
     */
    public AIResponse getAIResponse(String userMessage) {
        return getAIResponseWithMemberContext(userMessage, null);
    }

    /**
     * Get AI response with member context (for logged-in members)
     */
    public AIResponse getAIResponseWithMemberContext(String userMessage, Integer memberId) {
        try {
            // Kiểm tra API key
            if (API_KEY == null || API_KEY.trim().isEmpty()) {
                return new AIResponse("Hệ thống đang bị gián đoạn, hãy thử lại sau nhé...");
            }

            String requestBody = buildGeminiPayload(userMessage, memberId);
            HttpRequest httpRequest = buildHttpRequest(requestBody);
            
            // Timeout cho request
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, 
                HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                String aiReply = parseGeminiResponse(httpResponse.body());
                return new AIResponse(aiReply);
            } else {
                System.err.println("[ChatAI] API Error: " + httpResponse.statusCode() + " - " + httpResponse.body());
                return new AIResponse("Hệ thống đang bị gián đoạn, hãy thử lại sau nhé...");
            }
        } catch (Exception e) {
            System.err.println("[ChatAI] Exception: " + e.getMessage());
            e.printStackTrace();
            return new AIResponse("Hệ thống đang bị gián đoạn, hãy thử lại sau nhé...");
        }
    }

    /**
     * Lấy thông tin gym với cache để tối ưu hiệu suất
     */
    private String getCachedGymInfo() {
        long currentTime = System.currentTimeMillis();
        
        // Kiểm tra cache còn hiệu lực không
        if (cachedGymInfo != null && (currentTime - lastCacheTime) < CACHE_DURATION) {
            return cachedGymInfo;
        }
        
        // Cache hết hạn hoặc chưa có, load lại từ DB
        try {
            cachedGymInfo = loadOptimizedGymInfo();
            lastCacheTime = currentTime;
            System.out.println("[ChatAI] Gym info cache refreshed");
        } catch (Exception e) {
            System.err.println("[ChatAI] Error loading gym info: " + e.getMessage());
            // Fallback về cache cũ nếu có
            if (cachedGymInfo == null) {
                cachedGymInfo = "⚠️ Không thể tải thông tin phòng gym lúc này.";
            }
        }
        
        return cachedGymInfo;
    }

    /**
     * Load thông tin gym tối ưu từ database
     */
    private String loadOptimizedGymInfo() {
        StringBuilder sb = new StringBuilder();
        
        try {
            // Lấy thông tin gyms
            List<GymInfo> gyms = gymInfoDAO.findAll();
            
            sb.append("🏢 HỆ THỐNG PHÒNG TẬP GYMFIT:\n\n");
            
            if (gyms != null && !gyms.isEmpty()) {
                for (GymInfo gym : gyms) {
                    sb.append("📍 ").append(gym.getName() != null ? gym.getName() : "Cơ sở GymFit").append("\n");
                    if (gym.getAddress() != null) {
                        sb.append("   - Địa chỉ: ").append(gym.getAddress()).append("\n");
                    }
                    if (gym.getHotline() != null) {
                        sb.append("   - Hotline: ").append(gym.getHotline()).append("\n");
                    }
                    sb.append("\n");
                }
            } else {
                sb.append("📍 Cơ sở chính: Liên hệ để biết thông tin chi tiết\n\n");
            }

            // Lấy thông tin packages
            List<Package> packages = packageDAO.findAll().stream()
                    .filter(p -> p.getIsActive() != null && p.getIsActive())
                    .sorted((p1, p2) -> {
                        if (p1.getPrice() == null && p2.getPrice() == null) return 0;
                        if (p1.getPrice() == null) return 1;
                        if (p2.getPrice() == null) return -1;
                        return p1.getPrice().compareTo(p2.getPrice());
                    })
                    .collect(Collectors.toList());

            sb.append("💪 CÁC GÓI TẬP:\n");
            
            if (packages != null && !packages.isEmpty()) {
                for (Package pkg : packages) {
                    sb.append("✅ ").append(pkg.getName() != null ? pkg.getName() : "Gói tập").append("\n");
                    sb.append("   - Thời hạn: ").append(pkg.getDurationMonths()).append(" tháng\n");
                    
                    if (pkg.getPrice() != null) {
                        sb.append("   - Giá: ").append(String.format("%,.0f", pkg.getPrice().doubleValue())).append(" VND\n");
                    }
                    
                    if (pkg.getDescription() != null && !pkg.getDescription().trim().isEmpty()) {
                        sb.append("   - ").append(pkg.getDescription()).append("\n");
                    }
                    sb.append("\n");
                }
            } else {
                sb.append("   Liên hệ để biết thông tin chi tiết các gói tập\n");
            }
            
        } catch (Exception e) {
            System.err.println("[ChatAI] Error in loadOptimizedGymInfo: " + e.getMessage());
            sb.append("⚠️ Lỗi khi tải thông tin hệ thống. Vui lòng liên hệ trực tiếp để biết thêm chi tiết.\n");
        }
        
        return sb.toString();
    }


    private HttpRequest buildHttpRequest(String requestBody) {
        return HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(15))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();
    }

    private String buildGeminiPayload(String userMessage, Integer memberId) {
        String gymInfo = getCachedGymInfo();
        String memberInfo = getMemberInfo(memberId);
        String systemPrompt = buildSystemPrompt(gymInfo, memberInfo, userMessage);

        RequestPayload.Part part = new RequestPayload.Part(systemPrompt);
        RequestPayload.Content content = new RequestPayload.Content(new RequestPayload.Part[]{part});
        GeminiRequest geminiRequest = new GeminiRequest(new RequestPayload.Content[]{content});

        return gson.toJson(geminiRequest);
    }

    /**
     * Lấy thông tin member từ database
     */
    private String getMemberInfo(Integer memberId) {
        if (memberId == null) {
            return ""; // Guest user
        }

        try {
            dao.MemberDAO memberDAO = new dao.MemberDAO();
            model.Member member = memberDAO.findById(memberId);
            
            if (member == null) {
                return "";
            }

            StringBuilder info = new StringBuilder();
            info.append("\n===== THÔNG TIN CÁ NHÂN =====\n");
            info.append("Tên: ").append(member.getName() != null ? member.getName() : "N/A").append("\n");
            
            if (member.getGender() != null) {
                info.append("Giới tính: ").append(member.getGender()).append("\n");
            }
            
            if (member.getWeight() != null) {
                info.append("Cân nặng: ").append(String.format("%.1f kg", member.getWeight())).append("\n");
            }
            
            if (member.getHeight() != null) {
                info.append("Chiều cao: ").append(String.format("%.1f cm", member.getHeight())).append("\n");
            }
            
            if (member.getBmi() != null) {
                info.append("BMI: ").append(String.format("%.1f", member.getBmi())).append("\n");
            }
            
            if (member.getGoal() != null && !member.getGoal().trim().isEmpty()) {
                info.append("Mục tiêu: ").append(member.getGoal()).append("\n");
            }

            // Lấy thông tin membership active
            dao.MembershipDAO membershipDAO = new dao.MembershipDAO();
            List<model.Membership> activeMemberships = membershipDAO.findActiveByMemberId(memberId);
            
            if (activeMemberships != null && !activeMemberships.isEmpty()) {
                model.Membership activeMembership = activeMemberships.get(0); // Lấy membership đầu tiên
                info.append("\n📋 GÓI TẬP HIỆN TẠI:\n");
                if (activeMembership.getPackageO() != null) {
                    info.append("Gói: ").append(activeMembership.getPackageO().getName()).append("\n");
                    if (activeMembership.getPackageO().getDescription() != null) {
                        info.append("Mô tả: ").append(activeMembership.getPackageO().getDescription()).append("\n");
                    }
                }
                if (activeMembership.getStartDate() != null) {
                    info.append("Bắt đầu: ").append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(activeMembership.getStartDate())).append("\n");
                }
                if (activeMembership.getEndDate() != null) {
                    info.append("Kết thúc: ").append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(activeMembership.getEndDate())).append("\n");
                    
                    // Tính số ngày còn lại
                    long daysLeft = (activeMembership.getEndDate().getTime() - System.currentTimeMillis()) / (1000 * 60 * 60 * 24);
                    info.append("Còn lại: ").append(daysLeft).append(" ngày\n");
                }
            } else {
                info.append("\n⚠️ Chưa có gói tập nào đang hoạt động\n");
            }

            return info.toString();
            
        } catch (Exception e) {
            System.err.println("[ChatAI] Error getting member info: " + e.getMessage());
            return "";
        }
    }

    private String buildSystemPrompt(String gymInfo, String memberInfo, String userMessage) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Bạn là GymFit AI, trợ lý ảo cá nhân của phòng tập GymFit. ");
        prompt.append("Hãy sử dụng thông tin dưới đây để trả lời câu hỏi.\n\n");
        
        prompt.append("===== THÔNG TIN HỆ THỐNG =====\n");
        prompt.append(gymInfo).append("\n");
        
        if (memberInfo != null && !memberInfo.trim().isEmpty()) {
            prompt.append(memberInfo).append("\n");
            prompt.append("===== HƯỚNG DẪN TRẢ LỜI (CHO MEMBER) =====\n");
            prompt.append("Bạn đang tư vấn cho member đã đăng nhập. ");
            prompt.append("Hãy sử dụng thông tin cá nhân của họ để đưa ra lời khuyên phù hợp. ");
            prompt.append("Nếu họ hỏi về cân nặng, chiều cao, BMI, mục tiêu - hãy tham khảo thông tin đã có. ");
            prompt.append("Đưa ra lời khuyên tập luyện và dinh dưỡng dựa trên thông số cá nhân của họ. ");
        } else {
            prompt.append("===== HƯỚNG DẪN TRẢ LỜI (CHO KHÁCH) =====\n");
            prompt.append("Bạn đang tư vấn cho khách chưa đăng nhập. ");
            prompt.append("Hãy giới thiệu về GymFit và khuyến khích họ đăng ký. ");
        }
        
        prompt.append("Trả lời bằng văn phong thân thiện, genZ, ngắn gọn, xúc tích nhất có thể. ");
        prompt.append("Không dùng font chữ đặc biệt.\n\n");
        prompt.append("Câu hỏi của ").append(memberInfo != null && !memberInfo.trim().isEmpty() ? "member" : "khách").append(": ");
        prompt.append(userMessage);
        
        return prompt.toString();
    }

    private String parseGeminiResponse(String body) {
        try {
            GeminiResponse res = gson.fromJson(body, GeminiResponse.class);
            if (res != null && res.candidates != null && res.candidates.length > 0
                    && res.candidates[0].content != null && res.candidates[0].content.parts != null
                    && res.candidates[0].content.parts.length > 0) {
                return res.candidates[0].content.parts[0].text;
            }
        } catch (Exception e) {
            System.err.println("[ChatAIService] Parse Gemini failed: " + e.getMessage());
        }
        return "Hệ thống đang bị gián đoạn, hãy thử lại sau nhé...";
    }
}