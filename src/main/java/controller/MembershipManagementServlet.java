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
import service.MemberShipService;
import service.PackageService;

/*
    Note: 
 */
@WebServlet(urlPatterns = "/admin/membership-management")
public class MembershipManagementServlet extends HttpServlet {

    private final MemberShipService membershipService = new MemberShipService();
    private final MemberService memberService = new MemberService();
    private final PackageService packageService = new PackageService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String action = req.getParameter("action");
            if (action == null) {
                action = "";
            }

            List<Membership> memberships = getMembership();
            List<Package> packages = packageService.getAll();

            int numberMemberships = countNumberMemberships(memberships);
            int numberMembershipsActive = countNumberMembershipsActive(memberships);
            int numberMembershipsExpired = countNumberMembershipsExpired(memberships);
            int numberMembershipsExpiringSoon = countNumberMembershipsExpiringSoon(memberships);

            if ("deleteMembership".equals(action)) {
                int id = Integer.parseInt(req.getParameter("membershipId"));
                deleteMembership(memberships, id);
            } else if ("filterMemberships".equals(action)) {
                String keyword = req.getParameter("keyword");
                String status = req.getParameter("status");
                String packageType = req.getParameter("packageType");

                memberships = filterMemberships(memberships, keyword, status, packageType);

                req.setAttribute("keyword", keyword);
                req.setAttribute("status", status);
                req.setAttribute("packageType", packageType);
            }

            req.setAttribute("packages", packages);
            req.setAttribute("memberships", memberships);
            req.setAttribute("numberMemberships", numberMemberships);
            req.setAttribute("numberMembershipsActive", numberMembershipsActive);
            req.setAttribute("numberMembershipsExpired", numberMembershipsExpired);
            req.setAttribute("numberMembershipsExpiringSoon", numberMembershipsExpiringSoon);

            req.getRequestDispatcher("/views/admin/membership_management.jsp").forward(req, resp);
        } catch (ServletException | IOException | NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Có lỗi xảy ra khi xử lý yêu cầu.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null)
            action = "";
        switch (action) {
            case "addMembership" -> {
                handleAddMembership(req, resp);
            }
            case "editMembership" -> {
                handleEditMembership(req, resp);
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
            Date endDate = DateUtils.addMonths(startDate, months);

            membership.setStartDate(startDate);
            membership.setEndDate(endDate);

            membershipService.add(membership);
        }
    }

    private List<Membership> getMembership() {
        return membershipService.getAll();
    }

    private List<Membership> filterMemberships(List<Membership> memberships, String keyword, String status,
            String packageType)
            throws ServletException, IOException {

        if (keyword != null) {
            memberships = filterByKeyword(memberships, keyword);
        }
        if (status != null) {
            memberships = filterByStatus(memberships, status);
        }
        if (packageType != null) {
            memberships = filterByPackageType(memberships, packageType);
        }

        return memberships;
    }

    private List<Membership> filterByKeyword(List<Membership> memberships, String keyword) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            memberships.removeIf(m -> {
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

    private List<Membership> filterByPackageType(List<Membership> memberships, String packageType) {

        if (packageType != null && !packageType.trim().isEmpty()) {
            memberships.removeIf(m -> {
                Package packageO = m.getPackageO();
                return packageO == null || !packageO.getName().equalsIgnoreCase(packageType);
            });
        }

        return memberships;
    }

    private int countNumberMemberships(List<Membership> memberships) {
        if (memberships == null) {
            return 0;
        }
        return memberships.size();
    }

    private int countNumberMembershipsActive(List<Membership> memberships) {
        return (int) memberships.stream().filter(m -> "active".equalsIgnoreCase(m.getStatus())).count();
    }

    private int countNumberMembershipsExpired(List<Membership> memberships) {
        return (int) memberships.stream().filter(m -> DateUtils.compareDates(m.getEndDate(), new Date()) < 0).count();
    }

    private int countNumberMembershipsExpiringSoon(List<Membership> memberships) {
        return (int) memberships.stream()
                .filter(m -> (DateUtils.checkDateDifference(m.getEndDate(), new Date()) == 1) &&
                        (DateUtils.compareDates(m.getEndDate(), new Date()) > 1))
                .count();
    }

    private void handleEditMembership(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        int memberId = memberService.getByUsername(username).getId();
        int packageId = Integer.parseInt(req.getParameter("package_id"));
        int membershipId = Integer.parseInt(req.getParameter("id"));
        String status = req.getParameter("status");
        String startDateStr = req.getParameter("startDate");

        Membership tempMembership = membershipService.getByMemberIdAndPackageId(memberId, packageId);
        if (tempMembership == null) {
            Membership membership = membershipService.getMembershipById(membershipId);
            Package packageO = packageService.getById(packageId);

            membership.setStatus(status);
            membership.setStartDate(DateUtils.parseToDate(startDateStr));
            membership.setPackageO(packageO);

            membershipService.update(membership);
        }
    }

    // Cần tối ưu hơn
    private void handleAddMembership(HttpServletRequest req, HttpServletResponse resp) {
        String username = req.getParameter("username");
        int memberId = memberService.getByUsername(username).getId();
        int packageId = Integer.parseInt(req.getParameter("package_id"));
        String startDateStr = req.getParameter("startDate");

        if (!isExistMembership(memberId, packageId))
            addMembership(username, packageId, startDateStr);
    }

    private boolean isExistMembership(int memberId, int packageId) {
        Membership membership = membershipService.getByMemberIdAndPackageId(memberId, packageId);
        if (membership == null)
            return false;

        return true;
    }
}
