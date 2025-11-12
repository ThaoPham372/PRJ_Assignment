package controller;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

import Utils.FormUtils;
import dao.GymInfoDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Trainer;
import model.User;
import model.schedule.DayOfWeek;
import model.schedule.TrainerSchedule;
import service.TrainerService;
import service.UserService;
import service.schedule.TrainerScheduleService;

/**
 * TrainerManagementServlet - Quản lý PT và lịch của PT
 * Follows MVC pattern - Controller layer
 */
@WebServlet(urlPatterns = "/admin/trainer-management")
public class TrainerManagementServlet extends HttpServlet {

    private final TrainerService trainerService;
    private final UserService userService;
    private final TrainerScheduleService trainerScheduleService;
    private final GymInfoDAO gymInfoDAO;

    public TrainerManagementServlet() {
        this.trainerService = new TrainerService();
        this.userService = new UserService();
        this.trainerScheduleService = new TrainerScheduleService();
        this.gymInfoDAO = new GymInfoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action != null) {
            switch (action) {
                case "edit" -> handleEditTrainer(req, resp);
                case "schedule" -> handleViewSchedule(req, resp);
                default -> loadTrainersList(req, resp);
            }
        } else {
            loadTrainersList(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");

        if (action == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is required");
            return;
        }

        switch (action) {
            case "add" -> handleAddTrainer(req, resp);
            case "update" -> handleUpdateTrainer(req, resp);
            case "delete" -> handleDeleteTrainer(req, resp);
            case "activate" -> handleActivateTrainer(req, resp);
            case "addSchedule" -> handleAddSchedule(req, resp);
            case "updateSchedule" -> handleUpdateSchedule(req, resp);
            case "deleteSchedule" -> handleDeleteSchedule(req, resp);
            default -> {
                req.setAttribute("error", "Invalid action: " + action);
                loadTrainersList(req, resp);
            }
        }
    }

    /**
     * Load danh sách trainers (tất cả, kể cả inactive)
     */
    private void loadTrainersList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Load tất cả trainers, không filter
            List<Trainer> allTrainers = trainerService.getAll();
            
            // Sắp xếp: active trước, inactive sau
            List<Trainer> sortedTrainers = allTrainers.stream()
                    .sorted((t1, t2) -> {
                        boolean t1Active = t1.getStatus() != null && "active".equalsIgnoreCase(t1.getStatus());
                        boolean t2Active = t2.getStatus() != null && "active".equalsIgnoreCase(t2.getStatus());
                        if (t1Active == t2Active) return 0;
                        return t1Active ? -1 : 1; // active trước
                    })
                    .collect(Collectors.toList());

            req.setAttribute("trainers", sortedTrainers);
            req.setAttribute("gyms", gymInfoDAO.findAll());
            req.setAttribute("timeSlots", trainerScheduleService.getActiveTimeSlots());

            req.getRequestDispatcher("/views/admin/trainer_management.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi khi tải danh sách PT: " + e.getMessage());
            req.getRequestDispatcher("/views/admin/trainer_management.jsp").forward(req, resp);
        }
    }

    /**
     * Hiển thị form chỉnh sửa trainer
     */
    private void handleEditTrainer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String trainerIdStr = req.getParameter("id");
            if (trainerIdStr == null || trainerIdStr.isEmpty()) {
                req.setAttribute("error", "ID PT không hợp lệ");
                loadTrainersList(req, resp);
                return;
            }

            int trainerId = Integer.parseInt(trainerIdStr);
            Trainer trainer = trainerService.getTrainerById(trainerId);

            if (trainer == null) {
                req.setAttribute("error", "Không tìm thấy PT với ID: " + trainerId);
                loadTrainersList(req, resp);
                return;
            }

            req.setAttribute("editTrainer", trainer);
            req.setAttribute("gyms", gymInfoDAO.findAll());
            req.getRequestDispatcher("/views/admin/edit_trainer.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID PT không hợp lệ");
            loadTrainersList(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi khi tải thông tin PT: " + e.getMessage());
            loadTrainersList(req, resp);
        }
    }

    /**
     * Xem lịch của trainer
     */
    private void handleViewSchedule(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String trainerIdStr = req.getParameter("id");
            if (trainerIdStr == null || trainerIdStr.isEmpty()) {
                req.setAttribute("error", "ID PT không hợp lệ");
                loadTrainersList(req, resp);
                return;
            }

            int trainerId = Integer.parseInt(trainerIdStr);
            Trainer trainer = trainerService.getTrainerById(trainerId);

            if (trainer == null) {
                req.setAttribute("error", "Không tìm thấy PT với ID: " + trainerId);
                loadTrainersList(req, resp);
                return;
            }

            List<TrainerSchedule> schedules = trainerScheduleService.getAvailableSchedulesByTrainer(trainerId);

            // Load trainers list
            List<Trainer> allTrainers = trainerService.getAll();
            List<Trainer> activeTrainers = allTrainers.stream()
                    .filter(t -> t.getStatus() != null &&
                            "active".equalsIgnoreCase(t.getStatus()))
                    .collect(Collectors.toList());

            req.setAttribute("trainers", activeTrainers);
            req.setAttribute("viewScheduleTrainer", trainer);
            req.setAttribute("viewSchedules", schedules);
            req.setAttribute("gyms", gymInfoDAO.findAll());
            req.setAttribute("timeSlots", trainerScheduleService.getActiveTimeSlots());
            req.getRequestDispatcher("/views/admin/trainer_management.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID PT không hợp lệ");
            loadTrainersList(req, resp);
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi khi tải lịch PT: " + e.getMessage());
            loadTrainersList(req, resp);
        }
    }

    /**
     * Thêm trainer mới
     */
    private void handleAddTrainer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Validate required fields
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String username = req.getParameter("username");
            String phone = req.getParameter("phone");
            String password = req.getParameter("password");
            String workAt = req.getParameter("workAt");

            if (name == null || name.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập họ và tên");
                loadTrainersList(req, resp);
                return;
            }

            if (email == null || email.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập email");
                loadTrainersList(req, resp);
                return;
            }

            // Validate email format
            if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                req.setAttribute("error", "Email không hợp lệ");
                loadTrainersList(req, resp);
                return;
            }

            // Check if email already exists in entire user table (Admin, Member, Trainer)
            if (userService.getUserByEmail(email) != null) {
                req.setAttribute("error", "Email đã tồn tại trong hệ thống. Vui lòng sử dụng email khác.");
                loadTrainersList(req, resp);
                return;
            }

            if (username == null || username.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập username");
                loadTrainersList(req, resp);
                return;
            }

            // Validate username format
            if (!username.matches("^[a-zA-Z0-9_]{3,20}$")) {
                req.setAttribute("error", "Username phải có 3-20 ký tự, chỉ chứa chữ, số và dấu gạch dưới");
                loadTrainersList(req, resp);
                return;
            }

            // Check if username already exists in entire user table
            if (userService.getUserByUsername(username) != null) {
                req.setAttribute("error", "Username đã tồn tại trong hệ thống. Vui lòng chọn username khác.");
                loadTrainersList(req, resp);
                return;
            }

            if (phone == null || phone.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập số điện thoại");
                loadTrainersList(req, resp);
                return;
            }

            // Validate phone format
            if (!phone.matches("^[0-9]{10,11}$")) {
                req.setAttribute("error", "Số điện thoại phải có 10-11 chữ số");
                loadTrainersList(req, resp);
                return;
            }

            if (password == null || password.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập mật khẩu");
                loadTrainersList(req, resp);
                return;
            }

            if (password.length() < 6) {
                req.setAttribute("error", "Mật khẩu phải có ít nhất 6 ký tự");
                loadTrainersList(req, resp);
                return;
            }

            if (workAt == null || workAt.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng chọn cơ sở làm việc");
                loadTrainersList(req, resp);
                return;
            }

            Trainer trainer = new Trainer();
            FormUtils.getFormValue(req, trainer);

            // Set các field riêng của Trainer
            if (req.getParameter("specialization") != null) {
                trainer.setSpecialization(req.getParameter("specialization").trim());
            }
            if (req.getParameter("yearsOfExperience") != null && !req.getParameter("yearsOfExperience").isEmpty()) {
                try {
                    trainer.setYearsOfExperience(Integer.parseInt(req.getParameter("yearsOfExperience").trim()));
                } catch (NumberFormatException e) {
                    // Ignore invalid number
                }
            }
            if (req.getParameter("certificationLevel") != null) {
                trainer.setCertificationLevel(req.getParameter("certificationLevel").trim());
            }
            if (req.getParameter("salary") != null && !req.getParameter("salary").isEmpty()) {
                try {
                    trainer.setSalary(Float.parseFloat(req.getParameter("salary").trim()));
                } catch (NumberFormatException e) {
                    // Ignore invalid number
                }
            }
            // workAt now stores gymId
            if (workAt != null) {
                trainer.setWorkAt(workAt.trim());
            }

            // Set status mặc định là active
            trainer.setStatus("active");

            // Thêm trainer vào database
            try {
                trainerService.add(trainer);
                req.setAttribute("success", "Thêm PT thành công!");
            } catch (jakarta.persistence.PersistenceException e) {
                // Handle database constraint violations
                Throwable cause = e.getCause();
                String errorMessage = e.getMessage();

                // Check for SQLIntegrityConstraintViolationException
                if (cause instanceof SQLIntegrityConstraintViolationException) {
                    SQLIntegrityConstraintViolationException sqlEx = (SQLIntegrityConstraintViolationException) cause;
                    String sqlMessage = sqlEx.getMessage();

                    if (sqlMessage != null) {
                        if (sqlMessage.contains("unique_email")
                                || (sqlMessage.contains("Duplicate entry") && sqlMessage.contains("email"))) {
                            req.setAttribute("error", "Email đã tồn tại trong hệ thống. Vui lòng sử dụng email khác.");
                        } else if (sqlMessage.contains("unique_username")
                                || (sqlMessage.contains("Duplicate entry") && sqlMessage.contains("username"))) {
                            req.setAttribute("error",
                                    "Username đã tồn tại trong hệ thống. Vui lòng chọn username khác.");
                        } else {
                            req.setAttribute("error", "Dữ liệu không hợp lệ: " + sqlMessage);
                        }
                    } else {
                        req.setAttribute("error", "Email hoặc Username đã tồn tại trong hệ thống.");
                    }
                } else if (errorMessage != null) {
                    if (errorMessage.contains("unique_email") || errorMessage.contains("Duplicate entry")) {
                        req.setAttribute("error", "Email đã tồn tại trong hệ thống. Vui lòng sử dụng email khác.");
                    } else {
                        req.setAttribute("error", "Lỗi database: " + errorMessage);
                    }
                } else {
                    req.setAttribute("error", "Lỗi khi thêm PT vào database. Vui lòng thử lại.");
                }
                e.printStackTrace();
            } catch (Exception e) {
                req.setAttribute("error", "Lỗi khi thêm PT: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            req.setAttribute("error", "Lỗi khi xử lý form: " + e.getMessage());
            e.printStackTrace();
        }

        loadTrainersList(req, resp);
    }

    /**
     * Cập nhật thông tin trainer
     */
    private void handleUpdateTrainer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String trainerIdStr = req.getParameter("id");
            if (trainerIdStr == null || trainerIdStr.isEmpty()) {
                req.setAttribute("error", "ID PT không hợp lệ");
                loadTrainersList(req, resp);
                return;
            }

            int trainerId = Integer.parseInt(trainerIdStr);
            Trainer trainer = trainerService.getTrainerById(trainerId);

            if (trainer == null) {
                req.setAttribute("error", "Không tìm thấy PT với ID: " + trainerId);
                loadTrainersList(req, resp);
                return;
            }

            // Validate required fields
            String name = req.getParameter("name");
            String email = req.getParameter("email");
            String phone = req.getParameter("phone");
            String workAt = req.getParameter("workAt");

            if (name == null || name.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập họ và tên");
                req.setAttribute("editTrainer", trainer);
                req.setAttribute("gyms", gymInfoDAO.findAll());
                req.getRequestDispatcher("/views/admin/edit_trainer.jsp").forward(req, resp);
                return;
            }

            if (email == null || email.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập email");
                req.setAttribute("editTrainer", trainer);
                req.setAttribute("gyms", gymInfoDAO.findAll());
                req.getRequestDispatcher("/views/admin/edit_trainer.jsp").forward(req, resp);
                return;
            }

            // Validate email format
            if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                req.setAttribute("error", "Email không hợp lệ");
                req.setAttribute("editTrainer", trainer);
                req.setAttribute("gyms", gymInfoDAO.findAll());
                req.getRequestDispatcher("/views/admin/edit_trainer.jsp").forward(req, resp);
                return;
            }

            // Check if email is already used by another trainer
            Trainer existingTrainer = trainerService.getTrainerByEmail(email);
            if (existingTrainer != null && !existingTrainer.getId().equals(trainer.getId())) {
                req.setAttribute("error", "Email đã được sử dụng bởi PT khác");
                req.setAttribute("editTrainer", trainer);
                req.setAttribute("gyms", gymInfoDAO.findAll());
                req.getRequestDispatcher("/views/admin/edit_trainer.jsp").forward(req, resp);
                return;
            }

            // Also check if email is used by any other user (Member, Admin)
            if (userService.getUserByEmail(email) != null) {
                User existingUser = userService.getUserByEmail(email);
                if (!existingUser.getId().equals(trainer.getId())) {
                    req.setAttribute("error", "Email đã được sử dụng bởi người dùng khác trong hệ thống");
                    req.setAttribute("editTrainer", trainer);
                    req.setAttribute("gyms", gymInfoDAO.findAll());
                    req.getRequestDispatcher("/views/admin/edit_trainer.jsp").forward(req, resp);
                    return;
                }
            }

            if (phone == null || phone.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng nhập số điện thoại");
                req.setAttribute("editTrainer", trainer);
                req.setAttribute("gyms", gymInfoDAO.findAll());
                req.getRequestDispatcher("/views/admin/edit_trainer.jsp").forward(req, resp);
                return;
            }

            // Validate phone format
            if (!phone.matches("^[0-9]{10,11}$")) {
                req.setAttribute("error", "Số điện thoại phải có 10-11 chữ số");
                req.setAttribute("editTrainer", trainer);
                req.setAttribute("gyms", gymInfoDAO.findAll());
                req.getRequestDispatcher("/views/admin/edit_trainer.jsp").forward(req, resp);
                return;
            }

            if (workAt == null || workAt.trim().isEmpty()) {
                req.setAttribute("error", "Vui lòng chọn cơ sở làm việc");
                req.setAttribute("editTrainer", trainer);
                req.setAttribute("gyms", gymInfoDAO.findAll());
                req.getRequestDispatcher("/views/admin/edit_trainer.jsp").forward(req, resp);
                return;
            }

            // Cập nhật thông tin từ form (KHÔNG cập nhật username và password)
            // Lưu lại username và password hiện tại trước khi gọi FormUtils
            String originalUsername = trainer.getUsername();
            String originalPassword = trainer.getPassword();

            FormUtils.getFormValue(req, trainer);

            // Khôi phục username và password về giá trị ban đầu
            trainer.setUsername(originalUsername);
            trainer.setPassword(originalPassword);

            // Cập nhật các field riêng của Trainer
            if (req.getParameter("specialization") != null) {
                trainer.setSpecialization(req.getParameter("specialization").trim());
            }
            if (req.getParameter("yearsOfExperience") != null && !req.getParameter("yearsOfExperience").isEmpty()) {
                try {
                    trainer.setYearsOfExperience(Integer.parseInt(req.getParameter("yearsOfExperience").trim()));
                } catch (NumberFormatException e) {
                    // Ignore invalid number
                }
            }
            if (req.getParameter("certificationLevel") != null) {
                trainer.setCertificationLevel(req.getParameter("certificationLevel").trim());
            }
            if (req.getParameter("salary") != null && !req.getParameter("salary").isEmpty()) {
                try {
                    trainer.setSalary(Float.parseFloat(req.getParameter("salary").trim()));
                } catch (NumberFormatException e) {
                    // Ignore invalid number
                }
            }
            // workAt now stores gymId
            if (workAt != null) {
                trainer.setWorkAt(workAt.trim());
            }

            trainerService.update(trainer);
            req.setAttribute("success", "Cập nhật thông tin PT thành công!");
            req.setAttribute("editTrainer", trainer);
            req.setAttribute("gyms", gymInfoDAO.findAll());
            req.getRequestDispatcher("/views/admin/edit_trainer.jsp").forward(req, resp);
            return;

        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID PT không hợp lệ");
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi khi cập nhật PT: " + e.getMessage());
            e.printStackTrace();
        }

        loadTrainersList(req, resp);
    }

    /**
     * Xóa trainer (set status = inactive)
     * Chỉ xóa trainer đang active
     */
    private void handleDeleteTrainer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String trainerIdStr = req.getParameter("id");
            if (trainerIdStr == null || trainerIdStr.isEmpty()) {
                req.setAttribute("error", "ID PT không hợp lệ");
                loadTrainersList(req, resp);
                return;
            }

            int trainerId;
            try {
                trainerId = Integer.parseInt(trainerIdStr);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID PT không hợp lệ: " + trainerIdStr);
                loadTrainersList(req, resp);
                return;
            }

            Trainer trainer = trainerService.getTrainerById(trainerId);

            if (trainer == null) {
                req.setAttribute("error", "Không tìm thấy PT với ID: " + trainerId);
                loadTrainersList(req, resp);
                return;
            }

            // Kiểm tra nếu trainer đã inactive
            if (trainer.getStatus() != null && "inactive".equalsIgnoreCase(trainer.getStatus())) {
                req.setAttribute("error", "PT này đã bị xóa (INACTIVE) rồi");
                loadTrainersList(req, resp);
                return;
            }

            // Xóa trainer (set status = inactive)
            trainerService.delete(trainer);
            req.setAttribute("success", "Xóa PT thành công! PT đã được đánh dấu là INACTIVE.");
            
        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID PT không hợp lệ");
        } catch (Exception e) {
            System.err.println("[TrainerManagementServlet] Error deleting trainer: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("error", "Lỗi khi xóa PT: " + e.getMessage());
        }

        loadTrainersList(req, resp);
    }

    /**
     * Kích hoạt lại trainer (set status = active)
     */
    private void handleActivateTrainer(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String trainerIdStr = req.getParameter("id");
            if (trainerIdStr == null || trainerIdStr.isEmpty()) {
                req.setAttribute("error", "ID PT không hợp lệ");
                loadTrainersList(req, resp);
                return;
            }

            int trainerId;
            try {
                trainerId = Integer.parseInt(trainerIdStr);
            } catch (NumberFormatException e) {
                req.setAttribute("error", "ID PT không hợp lệ: " + trainerIdStr);
                loadTrainersList(req, resp);
                return;
            }

            Trainer trainer = trainerService.getTrainerById(trainerId);
            
            if (trainer == null) {
                req.setAttribute("error", "Không tìm thấy PT với ID: " + trainerId);
                loadTrainersList(req, resp);
                return;
            }

            // Kiểm tra nếu trainer đã active
            if (trainer.getStatus() != null && "active".equalsIgnoreCase(trainer.getStatus())) {
                req.setAttribute("error", "PT này đã được kích hoạt (ACTIVE) rồi");
                loadTrainersList(req, resp);
                return;
            }

            // Kích hoạt trainer (set status = active)
            trainer.setStatus("active");
            trainerService.update(trainer);
            req.setAttribute("success", "Kích hoạt PT thành công! PT đã được đánh dấu là ACTIVE.");
            
        } catch (Exception e) {
            System.err.println("[TrainerManagementServlet] Error activating trainer: " + e.getMessage());
            e.printStackTrace();
            req.setAttribute("error", "Lỗi khi kích hoạt PT: " + e.getMessage());
        }
        
        loadTrainersList(req, resp);
    }

    /**
     * Thêm lịch cho trainer
     */
    private void handleAddSchedule(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String trainerIdStr = req.getParameter("trainerId");
            String gymIdStr = req.getParameter("gymId");
            String dayOfWeekStr = req.getParameter("dayOfWeek");
            String slotIdStr = req.getParameter("slotId");

            if (trainerIdStr == null || gymIdStr == null || dayOfWeekStr == null || slotIdStr == null) {
                req.setAttribute("error", "Thiếu thông tin cần thiết để thêm lịch");
                loadTrainersList(req, resp);
                return;
            }

            TrainerSchedule schedule = new TrainerSchedule();
            schedule.setTrainerId(Integer.parseInt(trainerIdStr));
            // GymInfo.gymId is Long, but TrainerSchedule.gymId is Integer
            schedule.setGymId((int) Long.parseLong(gymIdStr));
            schedule.setDayOfWeek(DayOfWeek.fromCode(dayOfWeekStr));
            schedule.setSlotId(Integer.parseInt(slotIdStr));

            if (req.getParameter("maxBookings") != null && !req.getParameter("maxBookings").isEmpty()) {
                try {
                    schedule.setMaxBookings(Integer.parseInt(req.getParameter("maxBookings").trim()));
                } catch (NumberFormatException e) {
                    schedule.setMaxBookings(1);
                }
            }

            if (req.getParameter("notes") != null) {
                schedule.setNotes(req.getParameter("notes").trim());
            }

            if (req.getParameter("isAvailable") != null) {
                schedule.setIsAvailable("true".equalsIgnoreCase(req.getParameter("isAvailable")));
            } else {
                schedule.setIsAvailable(true);
            }

            trainerScheduleService.createSchedule(schedule);
            req.setAttribute("success", "Thêm lịch cho PT thành công!");

        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Lỗi: " + e.getMessage());
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi khi thêm lịch: " + e.getMessage());
        }

        loadTrainersList(req, resp);
    }

    /**
     * Cập nhật lịch của trainer
     */
    private void handleUpdateSchedule(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String scheduleIdStr = req.getParameter("scheduleId");
            if (scheduleIdStr == null || scheduleIdStr.isEmpty()) {
                req.setAttribute("error", "ID lịch không hợp lệ");
                loadTrainersList(req, resp);
                return;
            }

            TrainerSchedule schedule = trainerScheduleService.getScheduleById(Integer.parseInt(scheduleIdStr));
            if (schedule == null) {
                req.setAttribute("error", "Không tìm thấy lịch với ID: " + scheduleIdStr);
                loadTrainersList(req, resp);
                return;
            }

            // Cập nhật các field
            if (req.getParameter("gymId") != null) {
                schedule.setGymId((int) Long.parseLong(req.getParameter("gymId")));
            }
            if (req.getParameter("dayOfWeek") != null) {
                schedule.setDayOfWeek(DayOfWeek.fromCode(req.getParameter("dayOfWeek")));
            }
            if (req.getParameter("slotId") != null) {
                schedule.setSlotId(Integer.parseInt(req.getParameter("slotId")));
            }
            if (req.getParameter("maxBookings") != null && !req.getParameter("maxBookings").isEmpty()) {
                try {
                    schedule.setMaxBookings(Integer.parseInt(req.getParameter("maxBookings").trim()));
                } catch (NumberFormatException e) {
                    // Ignore
                }
            }
            if (req.getParameter("notes") != null) {
                schedule.setNotes(req.getParameter("notes").trim());
            }
            if (req.getParameter("isAvailable") != null) {
                schedule.setIsAvailable("true".equalsIgnoreCase(req.getParameter("isAvailable")));
            }

            trainerScheduleService.updateSchedule(schedule);
            req.setAttribute("success", "Cập nhật lịch thành công!");

        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID lịch không hợp lệ");
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi khi cập nhật lịch: " + e.getMessage());
        }

        loadTrainersList(req, resp);
    }

    /**
     * Xóa lịch của trainer
     */
    private void handleDeleteSchedule(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String scheduleIdStr = req.getParameter("scheduleId");
            if (scheduleIdStr == null || scheduleIdStr.isEmpty()) {
                req.setAttribute("error", "ID lịch không hợp lệ");
                loadTrainersList(req, resp);
                return;
            }

            TrainerSchedule schedule = trainerScheduleService.getScheduleById(Integer.parseInt(scheduleIdStr));
            if (schedule == null) {
                req.setAttribute("error", "Không tìm thấy lịch với ID: " + scheduleIdStr);
                loadTrainersList(req, resp);
                return;
            }

            trainerScheduleService.deleteSchedule(schedule);
            req.setAttribute("success", "Xóa lịch thành công!");

        } catch (NumberFormatException e) {
            req.setAttribute("error", "ID lịch không hợp lệ");
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi khi xóa lịch: " + e.getMessage());
        }

        loadTrainersList(req, resp);
    }
}
