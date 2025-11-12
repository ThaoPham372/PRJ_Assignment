package listener;

import dao.MemberDAO;
import dao.MembershipDAO;
import dao.PackageDAO;
import dao.PaymentDAO;
import dao.ProductDAO;
import dao.shop.OrderDao;
import dao.shop.OrderItemDao;
import model.Member;
import model.Membership;
import model.Package;
import model.Payment;
import model.Product;
import model.shop.DeliveryMethod;
import model.shop.Order;
import model.shop.OrderItem;
import model.shop.OrderStatus;
import model.shop.PaymentMethod;
import model.shop.PaymentStatus;
import service.PasswordService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * SalesDataInitializer - Khởi tạo dữ liệu mua hàng, thanh toán và membership
 * 
 * Tạo:
 * - Thêm member accounts để tạo orders và memberships
 * - Orders với order_details (sản phẩm) với thời gian trải đều nhiều tháng
 * - Payments cho orders (PRODUCT type) - trên 10 payments
 * - Memberships với packages (3-4 memberships) với thời gian active khác nhau
 * - Payments cho memberships (PACKAGE type)
 */
public class SalesDataInitializer {

    private static final Random random = new Random();
    private static final DateTimeFormatter orderNumberFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * Khởi tạo dữ liệu mua hàng
     */
    public static void initialize() {
        System.out.println("========================================");
        System.out.println("[SalesDataInitializer] Bắt đầu khởi tạo dữ liệu mua hàng...");
        System.out.println("========================================");

        try {
            MemberDAO memberDAO = new MemberDAO();
            PackageDAO packageDAO = new PackageDAO();
            ProductDAO productDAO = new ProductDAO();
            OrderDao orderDAO = new OrderDao();
            OrderItemDao orderItemDAO = new OrderItemDao();
            PaymentDAO paymentDAO = new PaymentDAO();
            MembershipDAO membershipDAO = new MembershipDAO();
            PasswordService passwordService = new PasswordService();

            // 1. Tạo thêm member accounts
            List<Member> additionalMembers = createAdditionalMembers(memberDAO, passwordService);
            System.out.println("[SalesDataInitializer] Đã tạo " + additionalMembers.size() + " member accounts mới");

            // 2. Lấy tất cả members
            List<Member> allMembers = memberDAO.findAll();
            if (allMembers.isEmpty()) {
                System.out.println("[SalesDataInitializer] Cảnh báo: Không có member nào, không thể tạo orders");
                return;
            }

            // 3. Lấy packages (chỉ active packages)
            List<Package> allPackages = packageDAO.findAll();
            List<Package> packages = new ArrayList<>();
            for (Package p : allPackages) {
                if (p.getIsActive() != null && p.getIsActive()) {
                    packages.add(p);
                }
            }
            if (packages.isEmpty()) {
                System.out.println("[SalesDataInitializer] Cảnh báo: Không có package active nào, không thể tạo memberships");
            }

            // 4. Lấy products (chỉ active products)
            List<Product> allProducts = productDAO.findAll();
            List<Product> products = new ArrayList<>();
            for (Product p : allProducts) {
                if (p.getActive() != null && p.getActive()) {
                    products.add(p);
                }
            }
            if (products.isEmpty()) {
                System.out.println("[SalesDataInitializer] Cảnh báo: Không có product active nào, không thể tạo orders");
            }

            // 5. Tạo orders và payments (PRODUCT type) - trên 10 payments
            if (!products.isEmpty() && !allMembers.isEmpty()) {
                createOrdersAndPayments(orderDAO, orderItemDAO, paymentDAO, allMembers, products);
            } else {
                System.out.println("[SalesDataInitializer] Bỏ qua tạo orders: thiếu products hoặc members");
            }

            // 6. Tạo memberships và payments (PACKAGE type) - 3-4 memberships
            if (!packages.isEmpty() && !allMembers.isEmpty()) {
                createMembershipsAndPayments(membershipDAO, paymentDAO, allMembers, packages);
            } else {
                System.out.println("[SalesDataInitializer] Bỏ qua tạo memberships: thiếu packages hoặc members");
            }

            System.out.println("[SalesDataInitializer] Hoàn tất khởi tạo dữ liệu mua hàng.");
        } catch (Exception e) {
            System.err.println("[SalesDataInitializer] Lỗi khi khởi tạo dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("========================================");
    }

    /**
     * Tạo thêm member accounts
     */
    private static List<Member> createAdditionalMembers(MemberDAO memberDAO, PasswordService passwordService) {
        List<Member> newMembers = new ArrayList<>();
        String[] memberNames = {"Nguyễn Văn A", "Trần Thị B", "Lê Văn C", "Phạm Thị D", "Hoàng Văn E"};
        String[] memberEmails = {"member1@gymfit.vn", "member2@gymfit.vn", "member3@gymfit.vn", 
                                 "member4@gymfit.vn", "member5@gymfit.vn"};
        String[] memberUsernames = {"member1", "member2", "member3", "member4", "member5"};

        for (int i = 0; i < memberNames.length; i++) {
            try {
                MemberDAO checkDAO = new MemberDAO();
                if (checkDAO.existsByEmail(memberEmails[i]) > -1) {
                    System.out.println("[SalesDataInitializer] Member " + memberUsernames[i] + " đã tồn tại, bỏ qua.");
                    continue;
                }

                String passwordHash = passwordService.hashPassword("member" + (i + 1) + "1");

                Member member = new Member();
                member.setUsername(memberUsernames[i]);
                member.setPassword(passwordHash);
                member.setEmail(memberEmails[i]);
                member.setName(memberNames[i]);
                member.setRole("Member");
                member.setStatus("ACTIVE");
                member.setCreatedDate(new Date());
                member.setPhone("090" + String.format("%08d", 10000000 + i));
                member.setAddress("123 Đường " + memberNames[i] + ", Quận " + (i + 1) + ", TP.HCM");
                member.setWeight(65.0f + i * 5);
                member.setHeight(170.0f + i * 2);
                member.setGoal("Giảm cân và tăng cơ");

                MemberDAO saveDAO = new MemberDAO();
                int memberId = saveDAO.save(member);
                if (memberId > 0) {
                    newMembers.add(member);
                    System.out.println("[SalesDataInitializer] ✓ Đã tạo member: " + memberUsernames[i] + " (ID: " + memberId + ")");
                }
            } catch (Exception e) {
                System.err.println("[SalesDataInitializer] Lỗi khi tạo member " + memberUsernames[i] + ": " + e.getMessage());
            }
        }

        return newMembers;
    }

    /**
     * Tạo orders và payments (PRODUCT type)
     */
    private static void createOrdersAndPayments(OrderDao orderDAO, OrderItemDao orderItemDAO, 
                                                PaymentDAO paymentDAO, List<Member> members, 
                                                List<Product> products) {
        System.out.println("[SalesDataInitializer] Bắt đầu tạo orders và payments...");

        // Tạo 12 orders với thời gian trải đều từ 6 tháng trước đến hiện tại
        int orderCount = 12;
        LocalDateTime now = LocalDateTime.now();
        
        for (int i = 0; i < orderCount; i++) {
            try {
                // Tính toán thời gian: từ 6 tháng trước đến hiện tại, trải đều
                int monthsAgo = 5 - (i / 2); // 5, 5, 4, 4, 3, 3, 2, 2, 1, 1, 0, 0
                int daysOffset = (i % 2) * 15; // 0, 15, 0, 15, ...
                LocalDateTime orderDate = now.minusMonths(monthsAgo).minusDays(daysOffset)
                                             .withHour(10 + random.nextInt(10))
                                             .withMinute(random.nextInt(60))
                                             .withSecond(random.nextInt(60));

                // Chọn ngẫu nhiên member và products
                Member member = members.get(random.nextInt(members.size()));
                int productCount = 1 + random.nextInt(3); // 1-3 products per order
                List<Product> selectedProducts = new ArrayList<>();
                for (int j = 0; j < productCount && j < products.size(); j++) {
                    Product product = products.get(random.nextInt(products.size()));
                    if (!selectedProducts.contains(product)) {
                        selectedProducts.add(product);
                    }
                }

                if (selectedProducts.isEmpty()) {
                    continue;
                }

                // Tạo order
                Order order = new Order();
                order.setMemberId(member.getId());
                order.setOrderNumber(generateOrderNumber(orderDate));
                order.setOrderDate(orderDate);
                order.setCreatedAt(orderDate);
                
                // Tính total amount
                BigDecimal totalAmount = BigDecimal.ZERO;
                BigDecimal discountAmount = BigDecimal.ZERO;
                
                List<OrderItem> items = new ArrayList<>();
                for (Product product : selectedProducts) {
                    int quantity = 1 + random.nextInt(3); // 1-3 quantity
                    BigDecimal unitPrice = product.getPrice();
                    BigDecimal itemDiscount = BigDecimal.ZERO;
                    
                    // Có thể có discount (20% chance)
                    if (random.nextDouble() < 0.2) {
                        itemDiscount = unitPrice.multiply(BigDecimal.valueOf(quantity))
                                                .multiply(BigDecimal.valueOf(0.1)); // 10% discount
                    }
                    
                    BigDecimal itemSubtotal = unitPrice.multiply(BigDecimal.valueOf(quantity))
                                                       .subtract(itemDiscount);
                    
                    OrderItem item = new OrderItem();
                    item.setOrderId(null); // Will be set after order is saved
                    item.setProductId(product.getId());
                    item.setProductName(product.getProductName());
                    item.setQuantity(quantity);
                    item.setUnitPrice(unitPrice);
                    item.setDiscountAmount(itemDiscount);
                    item.setDiscountPercent(itemDiscount.compareTo(BigDecimal.ZERO) > 0 ? 
                                           BigDecimal.valueOf(10) : BigDecimal.ZERO);
                    
                    items.add(item);
                    totalAmount = totalAmount.add(itemSubtotal);
                    discountAmount = discountAmount.add(itemDiscount);
                }
                
                order.setTotalAmount(totalAmount);
                order.setDiscountAmount(discountAmount);
                order.setOrderStatus(OrderStatus.COMPLETED); // Most orders are completed
                order.setDeliveryMethod(random.nextDouble() < 0.7 ? DeliveryMethod.PICKUP : DeliveryMethod.DELIVERY);
                order.setDeliveryAddress(member.getAddress());
                order.setDeliveryPhone(member.getPhone());
                order.setNotes("Đơn hàng demo - Tự động tạo");

                // Lưu order
                OrderDao saveOrderDAO = new OrderDao();
                Integer orderId = saveOrderDAO.create(order);
                
                if (orderId != null && orderId > 0) {
                    // Lưu order items sử dụng insertBatch (đã được thiết kế để lưu trong một transaction)
                    try {
                        // Set orderId và tính discount percent cho tất cả items
                        for (OrderItem item : items) {
                            item.setOrderId(orderId);
                            // Tính discount percent nếu cần
                            if (item.getDiscountPercent() == null) {
                                if (item.getDiscountAmount() != null && 
                                    item.getDiscountAmount().compareTo(BigDecimal.ZERO) > 0 &&
                                    item.getUnitPrice() != null && 
                                    item.getUnitPrice().compareTo(BigDecimal.ZERO) > 0 &&
                                    item.getQuantity() != null && item.getQuantity() > 0) {
                                    BigDecimal totalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                                    BigDecimal discountPercent = item.getDiscountAmount()
                                        .divide(totalPrice, 4, java.math.RoundingMode.HALF_UP)
                                        .multiply(BigDecimal.valueOf(100));
                                    item.setDiscountPercent(discountPercent);
                                } else {
                                    item.setDiscountPercent(BigDecimal.ZERO);
                                }
                            }
                        }
                        
                        // Sử dụng insertBatch để lưu tất cả items
                        OrderItemDao saveItemDAO = new OrderItemDao();
                        saveItemDAO.insertBatch(orderId, items);
                        System.out.println("[SalesDataInitializer] Đã lưu " + items.size() + " order items cho order " + orderId);
                    } catch (Exception e) {
                        System.err.println("[SalesDataInitializer] Lỗi khi lưu order items cho order " + orderId + ": " + e.getMessage());
                        e.printStackTrace();
                        // Tiếp tục tạo payment ngay cả khi có lỗi với order items
                    }

                    // Tạo payment cho order
                    try {
                        Payment payment = new Payment();
                        payment.setMemberId(member.getId());
                        payment.setAmount(totalAmount);
                        payment.setPaymentDate(orderDate);
                        payment.setMethod(getRandomPaymentMethod());
                        payment.setStatus(PaymentStatus.PAID);
                        payment.setTransactionType(Payment.TransactionType.PRODUCT);
                        payment.setOrderId(orderId);
                        payment.setReferenceId("REF" + orderId + System.currentTimeMillis());
                        payment.setPaidAt(Date.from(orderDate.atZone(java.time.ZoneId.systemDefault()).toInstant()));
                        payment.setNotes("Thanh toán cho đơn hàng " + order.getOrderNumber());

                        PaymentDAO savePaymentDAO = new PaymentDAO();
                        savePaymentDAO.save(payment);

                        System.out.println("[SalesDataInitializer] ✓ Đã tạo order: " + order.getOrderNumber() + 
                                         " (ID: " + orderId + ", Amount: " + totalAmount + ", Items: " + items.size() + ")");
                    } catch (Exception e) {
                        System.err.println("[SalesDataInitializer] Lỗi khi tạo payment cho order " + orderId + ": " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("[SalesDataInitializer] Lỗi khi tạo order " + (i + 1) + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("[SalesDataInitializer] Hoàn tất tạo orders và payments.");
    }

    /**
     * Tạo memberships và payments (PACKAGE type)
     */
    private static void createMembershipsAndPayments(MembershipDAO membershipDAO, PaymentDAO paymentDAO,
                                                     List<Member> members, List<Package> packages) {
        System.out.println("[SalesDataInitializer] Bắt đầu tạo memberships và payments...");

        // Tạo 4 memberships với thời gian active khác nhau
        int membershipCount = 4;
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);

        for (int i = 0; i < membershipCount && i < members.size(); i++) {
            try {
                // Lấy member và package từ database để đảm bảo chúng còn trong persistence context
                MemberDAO memberDAOForMembership = new MemberDAO();
                PackageDAO packageDAOForMembership = new PackageDAO();
                
                Member member = members.get(i);
                // Reload member từ database để đảm bảo nó còn trong persistence context
                Member memberEntity = memberDAOForMembership.findById(member.getId());
                if (memberEntity == null) {
                    System.err.println("[SalesDataInitializer] Không tìm thấy member với ID: " + member.getId());
                    continue;
                }
                
                Package packageO = packages.get(random.nextInt(packages.size()));
                // Reload package từ database
                Package packageEntity = packageDAOForMembership.findById(packageO.getId());
                if (packageEntity == null) {
                    System.err.println("[SalesDataInitializer] Không tìm thấy package với ID: " + packageO.getId());
                    continue;
                }

                // Tính toán thời gian: từ 4 tháng trước đến hiện tại
                int monthsAgo = 3 - i; // 3, 2, 1, 0 months ago
                cal.setTime(now);
                cal.add(Calendar.MONTH, -monthsAgo);
                // Set time to beginning of day for consistency
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date startDate = cal.getTime();

                // End date = start date + package duration
                cal.add(Calendar.MONTH, packageEntity.getDurationMonths());
                Date endDate = cal.getTime();

                // Status: ACTIVE nếu endDate >= now, EXPIRED nếu endDate < now
                String status = endDate.after(now) || endDate.equals(now) ? "ACTIVE" : "EXPIRED";
                if (i == 0) {
                    // Đảm bảo membership đầu tiên là ACTIVE
                    status = "ACTIVE";
                    // Đảm bảo endDate > now
                    cal.setTime(now);
                    cal.add(Calendar.MONTH, packageEntity.getDurationMonths());
                    endDate = cal.getTime();
                }

                // Tạo membership
                Membership membership = new Membership();
                membership.setMember(memberEntity);
                membership.setPackageO(packageEntity);
                membership.setStartDate(startDate);
                membership.setEndDate(endDate);
                membership.setStatus(status);
                membership.setCreatedDate(startDate);
                membership.setUpdatedDate(startDate);
                if ("ACTIVE".equals(status)) {
                    membership.setActivatedAt(startDate);
                }
                membership.setNotes("Membership demo - Tự động tạo");

                // Lưu membership
                MembershipDAO saveMembershipDAO = new MembershipDAO();
                int membershipId = saveMembershipDAO.save(membership);
                
                if (membershipId > 0) {
                    // Tạo payment cho membership
                    Payment payment = new Payment();
                    payment.setMemberId(member.getId());
                    payment.setAmount(packageO.getPrice());
                    payment.setPaymentDate(startDate.toInstant()
                                                   .atZone(java.time.ZoneId.systemDefault())
                                                   .toLocalDateTime());
                    payment.setMethod(getRandomPaymentMethod());
                    payment.setStatus(PaymentStatus.PAID);
                    payment.setTransactionType(Payment.TransactionType.PACKAGE);
                    payment.setMembershipId(membershipId);
                    payment.setReferenceId("MEM" + membershipId + System.currentTimeMillis());
                    payment.setPaidAt(startDate);
                    payment.setNotes("Thanh toán cho gói tập " + packageO.getName());

                    PaymentDAO savePaymentDAO = new PaymentDAO();
                    savePaymentDAO.save(payment);

                    System.out.println("[SalesDataInitializer] ✓ Đã tạo membership: " + membershipId + 
                                     " (Member: " + member.getUsername() + ", Package: " + packageO.getName() + 
                                     ", Status: " + status + ", Start: " + startDate + ", End: " + endDate + ")");
                }
            } catch (Exception e) {
                System.err.println("[SalesDataInitializer] Lỗi khi tạo membership " + (i + 1) + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        System.out.println("[SalesDataInitializer] Hoàn tất tạo memberships và payments.");
    }

    /**
     * Generate order number
     */
    private static String generateOrderNumber(LocalDateTime dateTime) {
        String timestamp = dateTime.format(orderNumberFormatter);
        int randomNum = random.nextInt(1000);
        return "ORD" + timestamp + String.format("%03d", randomNum);
    }

    /**
     * Get random payment method
     */
    private static PaymentMethod getRandomPaymentMethod() {
        PaymentMethod[] methods = PaymentMethod.values();
        return methods[random.nextInt(methods.length)];
    }
}

