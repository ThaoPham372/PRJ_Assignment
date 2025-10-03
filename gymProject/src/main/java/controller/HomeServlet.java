///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//
//package controller;
//
//import model.Player;
//import java.io.IOException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//import static DAO.DBConnection.getConnection;  // Sử dụng phương thức getConnection từ DBConnection
//import SettingsDAO.SettingsDAO;
//
//@WebServlet("/home")
//public class HomeServlet extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        List<Player> players = getPlayersFromDB();  // Lấy thông tin người chơi từ DB
//        int basePoints = getBasePointsFromSettings();
//        long totalScore = players.stream().mapToLong(Player::getScore).sum() + basePoints;
//        req.setAttribute("players", players);      // Gửi dữ liệu tới JSP
//        req.setAttribute("totalScore", totalScore);
//        req.setAttribute("basePoints", basePoints);
//        req.getRequestDispatcher("views/home.jsp").forward(req, resp);  // Chuyển tiếp đến home.jsp
//    }
//
//    // Phương thức này sẽ kết nối cơ sở dữ liệu và lấy thông tin người chơi
//    private List<Player> getPlayersFromDB() {
//        List<Player> players = new ArrayList<>();
//        String sql = "SELECT id, name, score FROM players ORDER BY score DESC, name ASC";  // Sắp xếp theo điểm giảm dần
//
//        try (Connection conn = getConnection();  // Lấy kết nối từ DBConnection
//             PreparedStatement stmt = conn.prepareStatement(sql); 
//             ResultSet rs = stmt.executeQuery()) {   // Thực thi câu lệnh SQL
//
//            while (rs.next()) { 
//                int id = rs.getInt("id");             // Lấy id
//                String name = rs.getString("name");   // Lấy tên người chơi
//                int score = rs.getInt("score");       // Lấy điểm người chơi
//
//                Player player = new Player(id, name, score);  // Tạo đối tượng Player với id
//                players.add(player);  // Thêm vào danh sách người chơi
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();  // In lỗi nếu có sự cố với kết nối hoặc truy vấn
//        }
//        return players;  // Trả về danh sách người chơi
//    }
//
//    private int getBasePointsFromSettings() {
//        return new SettingsDAO().getTribeBasePoints();
//    }
//}
