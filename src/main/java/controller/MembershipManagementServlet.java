
package controller;

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
import model.Member;
import model.Membership;
import service.MemberService;
import service.MembershipService;

/*
    Note: 
 */
@WebServlet(urlPatterns = "/admin/member-management")
public class MembershipManagementServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("deleteMembership".equals(req.getParameter("action"))) {
            deleteMembership(req, resp);
        }
        getMembership(req, resp);
        req.getRequestDispatcher("/views/admin/member_management.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addMembership" ->
                addMembership(req, resp);
            default -> {
                System.out.println("Action is null");
                req.getRequestDispatcher("/views/admin/member_management.jsp").forward(req, resp);
            }
        }
    }

    public void deleteMembership(HttpServletRequest req, HttpServletResponse resp) {
        System.out.println("\n\n## DELETE MEMBERSHIP");
        int membershipId = Integer.parseInt(req.getParameter("membershipId"));
        MembershipService membershipService = new MembershipService();
        System.out.println("> Membership: " + membershipService.getMembershipById(membershipId));
        membershipService.deleteById(membershipId);
        return;
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
        int memberId = memberService.add(member);

        if (memberId > -1) {
            Membership membership = new Membership();
            membership.setPackageId(packageId);
            membership.setUserId(member.getUserId());
            membership.setStartDate(startDate);

            Date now = new Date();
            membership.setEndDate(new Date(now.getTime() + 30L * 24 * 60 * 60 * 1000)); // + 30 ngay

            MembershipService membershipService = new MembershipService();
            membershipService.add(membership);
        }
    }

    private void getMembership(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("\n\nGet Membership CALLED");
        MembershipService membershipService = new MembershipService();
        List<Membership> memberships = membershipService.getAll();
        getMemberInfo(memberships);
        System.out.println("> Total memberships: " + memberships.size());
        req.setAttribute("memberships", memberships);
    }

    private void getMemberInfo(List<Membership> memberships) {
        MemberService memberService = new MemberService();
        for (Membership m : memberships) {
            Member mem = memberService.getMemberById(m.getUserId());
            if (mem != null) {
                m.setName(mem.getName());
                m.setEmail(mem.getEmail());
                m.setPhone(mem.getPhone());
                m.setPackageType("Standard");
            }
        }
    }
}
