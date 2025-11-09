package controller;

import Utils.DateUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import model.Member;
import model.Membership;
import model.Package;
import service.MemberService;
import service.MembershipService;
import service.PackageService;

/*
    Note: 
 */
@WebServlet(urlPatterns = "/admin/membership-management")
public class MembershipManagementServlet extends HttpServlet {

    private final MembershipService membershipService = new MembershipService();
    private final MemberService memberService = new MemberService();
    private final PackageService packageService = new PackageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            System.out.println("\nMEMBERSHIP MANAGEMENT");
            String action = req.getParameter("action");
            if (action == null)
                action = "";
            System.out.println("AAction: " + action);
            System.out.println("------------------------");
            List<Membership> memberships = getMembership();
            List<Package> packages = packageService.getAll();

            if ("deleteMembership".equals(action)) {
                int id = Integer.parseInt(req.getParameter("membershipId"));
                deleteMembership(memberships, id);
            } else if ("filterMemberships".equals(action)) {
                String keyword = req.getParameter("keyword");
                String status = req.getParameter("status");
                memberships = filterMemberships(memberships, keyword, status);
            }

            System.out.println("Finish GET");

            req.setAttribute("packages", packages);
            req.setAttribute("memberships", memberships);
            req.getRequestDispatcher("/views/admin/membership_management.jsp").forward(req, resp);
        } catch (ServletException | IOException | NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra khi xử lý yêu cầu.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action) {
            case "addMembership" -> {
                String username = req.getParameter("username");
                int packageId = Integer.parseInt(req.getParameter("package_id"));
                String startDateStr = req.getParameter("startDate");
                addMembership(username, packageId, startDateStr);
            }
            default -> {
                System.out.println("Action is null");
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/membership-management");
    }

    public List<Membership> deleteMembership(List<Membership> memberships, int id) {
        Membership membership = null;
        for (Membership m : memberships) {
            if (m.getId() == id) {
                membership = m;
                break;
            }
        }

        membershipService.delete(membership);

        return memberships;
    }

    private void addMembership(String username, int packageId, String startDateStr) {
        Member member = memberService.getByUsername(username);
        Package packageO = packageService.getById(packageId);
        if (member != null && packageO != null) {
            Membership membership = new Membership(member, packageO);
            int months = packageO.getDurationMonths();
            Date startDate = DateUtils.parseToDate(startDateStr);
            Date endDate = DateUtils.addMonths(months);

            membership.setStartDate(startDate);
            membership.setEndDate(endDate);

            membershipService.add(membership);
        }
    }

    private List<Membership> getMembership() {
        return membershipService.getAll();
    }

    private List<Membership> filterMemberships(List<Membership> memberships, String keyword, String status)
            throws ServletException, IOException {

        memberships = filterByKeyword(memberships, keyword);
        memberships = filterByStatus(memberships, status);

        return memberships;
    }

    private List<Membership> filterByKeyword(List<Membership> memberships, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            memberships.removeIf(m -> {
                MemberService memberService = new MemberService();
                Member mem = m.getMember();
                return mem == null || !(mem.getName().toLowerCase().contains(keyword)
                        || mem.getEmail().toLowerCase().contains(keyword));
            });
        }
        return memberships;
    }

    private List<Membership> filterByStatus(List<Membership> memberships, String status) {
        memberships.removeIf(m -> {
            // Filter by status
            boolean statusMatch = true;
            Date now = new Date();
            if (null != status) {
                switch (status) {
                    case "active" ->
                        statusMatch = m.getEndDate().after(now);
                    case "expiring" -> {
                        long diff = m.getEndDate().getTime() - now.getTime();
                        statusMatch = diff > 0 && diff <= 7L * 24 * 60 * 60 * 1000; // within 7 days
                    }
                    case "expired" ->
                        statusMatch = m.getEndDate().before(now);
                    default -> {
                    }
                }
            }
            return !statusMatch;
        });
        return memberships;
    }
}
