package com.gym.controller;

import com.gym.model.Member;
import com.gym.model.membership.Membership;
import com.gym.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.gym.service.membership.MembershipService;
import com.gym.util.DateUtils;
import java.time.LocalDate;

/*
    Note: 
 */
@WebServlet(urlPatterns = "/admin/member-management")
public class MembershipManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        List<Membership> memberships = getMembership();

        if ("deleteMembership".equals(action)) {
            int id = Integer.parseInt(req.getParameter("membershipId"));
            deleteMembership(memberships, id);
        } else if ("filterMemberships".equals(action)) {
            System.out.println("FILTER CALLED\n\n\n");
            String keyword = req.getParameter("keyword");
            String status = req.getParameter("status");
            String packageType = req.getParameter("packageType");

            memberships = filterMemberships(memberships, keyword, status, packageType);
        }

        req.setAttribute("memberships", memberships);
        req.getRequestDispatcher("/views/admin/member_management.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addMembership":
                addMembership(req, resp);
                break;
            default: {
                System.out.println("Action is null");
                req.getRequestDispatcher("/views/admin/member_management.jsp").forward(req, resp);
            }
        }
    }

    public List<Membership> deleteMembership(List<Membership> memberships, int id) {
        Membership membership = null;
        for (Membership m : memberships) {
            if (m.getMembershipId() == id) {
                membership = m;
                break;
            }
        }

        MembershipService membershipService = new MembershipService();
        membershipService.delete(membership);

        return memberships;
    }

    private void addMembership(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        boolean success = false;
        try {
            String username = req.getParameter("username");
            String phone = req.getParameter("phone");
            String packageIdStr = req.getParameter("package_id");
            String startDateStr = req.getParameter("startDate");

            if (username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên người dùng không được để trống.");
            }
            if (packageIdStr == null || packageIdStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Gói tập không được để trống.");
            }
            if (startDateStr == null || startDateStr.trim().isEmpty()) {
                throw new IllegalArgumentException("Ngày bắt đầu không được để trống.");
            }
            if (phone == null || phone.trim().isEmpty()) {
                throw new IllegalArgumentException("Phone bắt đầu không được để trống.");
            }

            int packageId;
            try {
                packageId = Integer.parseInt(packageIdStr);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Gói tập không hợp lệ (phải là số nguyên).", e);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false); // không cho ngày sai kiểu 2025-13-50

            Date startDate;
            try {
                startDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
            } catch (ParseException e) {
                throw new IllegalArgumentException("Định dạng ngày không hợp lệ. Phải là yyyy-MM-dd.", e);
            }

            createMembership(username, phone, packageId, startDate);
            success = true;
        } catch (IllegalArgumentException e) {
            System.err.println("⚠️ Lỗi dữ liệu đầu vào: " + e.getMessage());
            req.setAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            System.err.println("🔥 Lỗi không xác định khi thêm membership:");
            req.setAttribute("errorMessage", "Đã xảy ra lỗi khi thêm membership.");
        }

        if (success) {
            resp.sendRedirect(req.getContextPath() + "/admin/member-management");
        }

    }

    private void createMembership(String username, String phone, int packageId, Date startDate) {
        MemberService memberService = new MemberService();
        Member member = new Member();
        member.setUsername(username);
        member.setName(username);
        member.setEmail(username + "@gmail.com");
        member.setPhone(phone);
        memberService.saveMember(member);
        Long memberId = member.getId();

        if (memberId > -1) {
            Membership membership = new Membership();
            membership.setPackageId((long) packageId);
            membership.setUserId(member.getUserId());
            membership.setStartDate(DateUtils.parseToLocalDate(startDate.toString()));

            Date now = new Date();
            membership.setEndDate(DateUtils.parseToLocalDate(new Date(now.getTime() + 30L * 24 * 60 * 60 * 1000).toString())); // + 30 ngay

            MembershipService membershipService = new MembershipService();
            membershipService.add(membership);
        }
    }

    private List<Membership> getMembership() {
        MembershipService membershipService = new MembershipService();
        List<Membership> memberships = membershipService.getAll();
        generateMembershipInfo(memberships);
        return memberships;
    }

    private void generateMembershipInfo(List<Membership> memberships) {
        MemberService memberService = new MemberService();
        for (Membership m : memberships) {
            Member mem = memberService.getMemberByUserId(m.getUserId());
            if (mem != null) {
                m.setName(mem.getName());
                m.setEmail(mem.getEmail());
                m.setPhone(mem.getPhone());
                m.setPackageType("Standard");
            }
        }
    }

    private List<Membership> filterMemberships(List<Membership> memberships, String keyword, String status,
            String packageType) throws ServletException, IOException {

        memberships = filterByKeyword(memberships, keyword);
        memberships = filterByStatusAndPackageType(memberships, status, packageType);

        generateMembershipInfo(memberships);

        return memberships;
    }

    private List<Membership> filterByKeyword(List<Membership> memberships, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            memberships.removeIf(m -> {
                MemberService memberService = new MemberService();
                Member mem = memberService.getMemberByUserId(m.getUserId());
                return mem == null || !(mem.getName().toLowerCase().contains(keyword)
                        || mem.getEmail().toLowerCase().contains(keyword));
            });
        }
        return memberships;
    }

    private List<Membership> filterByStatusAndPackageType(List<Membership> memberships, String status,
            String packageType) {
        memberships.removeIf(m -> {
            // 🔹 Lấy ngày hiện tại
            LocalDate now = LocalDate.now();

            // 🔹 Filter theo status
            boolean statusMatch = true;

            if (status != null) {
                switch (status) {
                    case "active": {
                        statusMatch = m.getEndDate().isAfter(now);
                        break;
                    }
                    case "expiring": {
                        long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(now, m.getEndDate());
                        statusMatch = daysBetween > 0 && daysBetween <= 7; // trong vòng 7 ngày
                        break;
                    }
                    case "expired": {
                        statusMatch = m.getEndDate().isBefore(now);
                        break;
                    }
                    default: {
                        statusMatch = true;
                    }
                }
            }

            // 🔹 Filter theo packageType
            boolean packageMatch = true;
            if (packageType != null && !"all".equalsIgnoreCase(packageType)) {
                packageMatch = packageType.equalsIgnoreCase(m.getPackageType());
            }

            // 🔹 Nếu KHÔNG khớp cả 2 điều kiện thì remove
            return !(statusMatch && packageMatch);
        });

        return memberships;
    }

}
