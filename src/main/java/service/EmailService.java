package service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Email Service for sending emails via Gmail SMTP
 */
public class EmailService {
    
    private final Properties emailProps;
    private final String username;
    private final String password;
    private final String fromEmail;
    private final String fromName;
    
    public EmailService() {
        this.emailProps = new Properties();
        loadEmailProperties();
        
        // ✅ Get properties with validation
        this.username = emailProps.getProperty("mail.username");
        this.password = emailProps.getProperty("mail.password");
        this.fromEmail = emailProps.getProperty("mail.from");
        this.fromName = emailProps.getProperty("mail.from.name", "Gym Manager System");
        
        // ✅ Validate required properties
        if (username == null || username.trim().isEmpty()) {
            System.err.println("[EmailService] ❌ ERROR: mail.username is missing or empty in email.properties");
        }
        if (password == null || password.trim().isEmpty()) {
            System.err.println("[EmailService] ❌ ERROR: mail.password is missing or empty in email.properties");
        }
        if (fromEmail == null || fromEmail.trim().isEmpty()) {
            System.err.println("[EmailService] ❌ ERROR: mail.from is missing or empty in email.properties");
        }
        
        System.out.println("[EmailService] Initialized - username: " + 
                          (username != null ? username : "NULL") + 
                          ", fromEmail: " + (fromEmail != null ? fromEmail : "NULL"));
    }
    
    /**
     * Load email configuration from email.properties
     */
    private void loadEmailProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("email.properties")) {
            if (input == null) {
                System.err.println("[EmailService] ❌ ERROR: email.properties not found in classpath!");
                System.err.println("[EmailService] Expected location: src/main/resources/email.properties");
                System.err.println("[EmailService] Please:");
                System.err.println("[EmailService]   1. Copy email.properties.example to email.properties");
                System.err.println("[EmailService]   2. Configure your Gmail credentials");
                System.err.println("[EmailService]   3. Rebuild project: mvn clean package");
                System.err.println("[EmailService] See GMAIL_SETUP.md for detailed instructions");
                
                // ✅ Set default SMTP properties even if file not found
                emailProps.setProperty("mail.smtp.host", "smtp.gmail.com");
                emailProps.setProperty("mail.smtp.port", "587");
                emailProps.setProperty("mail.smtp.auth", "true");
                emailProps.setProperty("mail.smtp.starttls.enable", "true");
                emailProps.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
                return;
            }
            
            emailProps.load(input);
            System.out.println("[EmailService] ✅ Loaded email.properties successfully");
            
            // ✅ Log loaded properties (hide password)
            System.out.println("[EmailService] mail.username: " + 
                              (emailProps.getProperty("mail.username") != null ? "SET" : "NULL"));
            System.out.println("[EmailService] mail.password: " + 
                              (emailProps.getProperty("mail.password") != null ? "SET" : "NULL"));
            System.out.println("[EmailService] mail.from: " + 
                              (emailProps.getProperty("mail.from") != null ? emailProps.getProperty("mail.from") : "NULL"));
        } catch (IOException e) {
            System.err.println("[EmailService] ❌ Error loading email.properties: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Send password reset email with verification code
     */
    public boolean sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            System.out.println("[EmailService] Sending password reset email to: " + toEmail);
            
            String subject = "[GymFit] Mã xác nhận đổi mật khẩu";
            String body = buildPasswordResetEmailBody(resetToken);
            
            return sendEmail(toEmail, subject, body);
        } catch (Exception e) {
            System.err.println("[EmailService] Error sending password reset email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Build password reset email HTML body
     */
    private String buildPasswordResetEmailBody(String resetToken) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<style>" +
               "body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; }" +
               ".container { max-width: 600px; margin: 0 auto; padding: 20px; }" +
               ".header { background: linear-gradient(135deg, #141a46 0%, #1e2a5c 100%); color: white; padding: 30px; text-align: center; border-radius: 10px 10px 0 0; }" +
               ".content { background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px; }" +
               ".token { background: #ec8b5e; color: white; font-size: 32px; font-weight: bold; padding: 20px; text-align: center; border-radius: 10px; letter-spacing: 5px; margin: 20px 0; }" +
               ".footer { text-align: center; margin-top: 20px; font-size: 12px; color: #999; }" +
               ".warning { background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'>" +
               "<h1>🏋️ GymFit</h1>" +
               "<p>Đặt lại mật khẩu</p>" +
               "</div>" +
               "<div class='content'>" +
               "<h2>Xin chào,</h2>" +
               "<p>Bạn đã yêu cầu đặt lại mật khẩu cho tài khoản <strong>GymFit</strong>.</p>" +
               "<p>Mã xác nhận của bạn là:</p>" +
               "<div class='token'>" + resetToken + "</div>" +
               "<p>Mã này sẽ <strong>hết hạn sau 15 phút</strong>.</p>" +
               "<div class='warning'>" +
               "<strong>⚠️ Lưu ý:</strong> Nếu bạn không yêu cầu đổi mật khẩu, vui lòng bỏ qua email này. " +
               "Mật khẩu của bạn sẽ không thay đổi cho đến khi bạn nhập mã xác nhận và tạo mật khẩu mới." +
               "</div>" +
               "<p>Trân trọng,<br><strong>GymFit Team</strong></p>" +
               "</div>" +
               "<div class='footer'>" +
               "<p>&copy; 2024 GymFit - Hệ Thống Quản Lý Phòng Gym</p>" +
               "<p>Email này được gửi tự động, vui lòng không trả lời.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
    
    /**
     * Generic method to send email
     */
    private boolean sendEmail(String toEmail, String subject, String htmlBody) {
        try {
            // ✅ Validate required fields before sending
            if (username == null || password == null || fromEmail == null) {
                System.err.println("[EmailService] ❌ Cannot send email: Missing required configuration");
                System.err.println("[EmailService] username: " + (username != null ? "SET" : "NULL"));
                System.err.println("[EmailService] password: " + (password != null ? "SET" : "NULL"));
                System.err.println("[EmailService] fromEmail: " + (fromEmail != null ? "SET" : "NULL"));
                System.err.println("[EmailService] Please check email.properties file exists and is configured correctly");
                return false;
            }
            
            // Setup mail server properties with defaults
            Properties props = new Properties();
            String smtpHost = emailProps.getProperty("mail.smtp.host", "smtp.gmail.com");
            String smtpPort = emailProps.getProperty("mail.smtp.port", "587");
            String smtpAuth = emailProps.getProperty("mail.smtp.auth", "true");
            String startTls = emailProps.getProperty("mail.smtp.starttls.enable", "true");
            String sslTrust = emailProps.getProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
            
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);
            props.put("mail.smtp.auth", smtpAuth);
            props.put("mail.smtp.starttls.enable", startTls);
            props.put("mail.smtp.ssl.trust", sslTrust);
            
            System.out.println("[EmailService] SMTP Config: host=" + smtpHost + ", port=" + smtpPort);
            
            // Create session with authenticator
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });
            
            // Create message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, fromName));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setContent(htmlBody, "text/html; charset=UTF-8");
            
            // Send message
            Transport.send(message);
            
            System.out.println("[EmailService] ✅ Email sent successfully to: " + toEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("[EmailService] ❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Get token expiry minutes from config
     */
    public int getTokenExpiryMinutes() {
        String minutes = emailProps.getProperty("password.reset.token.expiry.minutes", "15");
        try {
            return Integer.parseInt(minutes);
        } catch (NumberFormatException e) {
            return 15; // default 15 minutes
        }
    }
}

