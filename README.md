# 🏋️ Gym Management System

Hệ thống quản lý phòng gym toàn diện được xây dựng bằng Java Web Technologies (JSP, JSTL, Servlet).

## 🚀 Tính Năng Chính

- ✅ **Quản lý khách hàng** - Thêm, sửa, xóa thông tin thành viên
- ✅ **Quản lý dịch vụ** - Personal training, group fitness, swimming pool
- ✅ **Quản lý sản phẩm** - Protein, supplements, gym equipment
- ✅ **Hệ thống hợp đồng** - Tạo và quản lý membership packages
- ✅ **Báo cáo thống kê** - Dashboard với charts và analytics
- ✅ **Thanh toán online** - Tích hợp MoMo, ZaloPay, QR codes
- ✅ **Đăng nhập OAuth** - Google, Facebook authentication
- ✅ **AI Integration** - Chatbot hỗ trợ và smart recommendations

## 🛠️ Tech Stack

### Backend

- **Java 11** - Core programming language
- **Servlet 4.0** - HTTP request handling
- **JSP 2.2** - Dynamic web pages
- **JSTL 1.2** - Tag libraries for JSP
- **JPA/Hibernate** - Object-relational mapping
- **SQL Server** - Primary database
- **JDBC** - Database connectivity

### Frontend

- **Bootstrap 5.3** - Responsive UI framework
- **Font Awesome 6.4** - Icons and fonts
- **Chart.js** - Data visualization
- **Custom CSS** - Modern design system

### Tools & Libraries

- **Maven** - Dependency management
- **JUnit 5** - Unit testing
- **Jackson** - JSON processing
- **Apache Commons** - Utility libraries

## 📁 Cấu Trúc Project

```
gym-management/
├── src/main/
│   ├── java/
│   │   └── com/gym/
│   │       ├── controller/     # Servlet controllers
│   │       ├── model/         # Entity classes (JPA)
│   │       ├── dao/           # Data Access Objects
│   │       ├── service/       # Business logic
│   │       ├── filter/        # Authentication filters
│   │       └── utils/         # Utility classes
│   ├── resources/
│   │   ├── META-INF/
│   │   └── validation/        # Validation configurations
│   └── webapp/
│       ├── WEB-INF/
│       │   └── web.xml       # Web application config
│       ├── css/              # Stylesheets
│       ├── js/               # JavaScript files
│       └── views/           gy # JSP pages
│           ├── common/       # Header, footer, navigation
│           ├── customers/    # Customer management
│           ├── services/     # Service management
│           ├── products/     # Product management
│           ├── contracts/    # Contract management
│           └── error/        # Error pages
├── pom.xml                   # Maven dependencies
└── README.md
```

## 🚀 Cách Chạy Dự Án

### Prerequisites

- Java 11 or higher
- Maven 3.6+
- SQL Server 2019+
- Apache Tomcat 9+

### Setup Database

1. Tạo database mới: `gym_management`
2. Import schema từ `/sql/schema.sql`
3. Import sample data từ `/sql/data.sql`

### Run Application

```bash
# Clone repository
git clone <repository-url>
cd gym-management

# Compile project
mvn clean compile

# Run with embedded Tomcat
mvn tomcat7:run

# Or deploy to external Tomcat
mvn clean package
# Copy target/gym-management.war to Tomcat webapps/
```

### Access Application

- **URL**: http://localhost:8080/gym-management
- **Demo Accounts**:
  - Manager: `admin/admin123`
  - Employee: `employee/emp123`
  - Customer: `customer/cust123`

## 📊 Database Schema

### Core Tables

- `customers` - Thông tin khách hàng
- `services` - Dịch vụ gym (PT, group class, pool)
- `products` - Sản phẩm bán tại gym
- `contracts` - Hợp đồng thành viên
- `contract_details` - Chi tiết hợp đồng
- `orders` - Đơn hàng mua sản phẩm
- `order_details` - Chi tiết đơn hàng
- `employees` - Nhân viên và tài khoản
- `payments` - Lịch sử thanh toán

## 🔒 Validation Rules

- **Customer Code**: `KH-XXXX` (X = 0-9)
- **Service Code**: `DV-XXXX` (X = 0-9)
- **Phone**: `090xxxxxxx`, `091xxxxxxxx`, `(84)+90xxxxxxxx`, `(84)+91xxxxxxxx`
- **ID Number**: 9 hoặc 13 chữ số
- **Email**: Định dạng email chuẩn
- **Dates**: DD/MM/YYYY format
- **Numbers**: Quantity, price phải là số dương

## 🎨 UI/UX Features

- **Responsive Design** - Mobile-first approach
- **Modern Interface** - Gradient backgrounds, card layouts
- **Interactive Charts** - Real-time data visualization
- **Smooth Animations** - CSS transitions và hover effects
- **Loading States** - User feedback cho async operations
- **Toast Notifications** - Success/error message system

## 🔮 Tính Năng Sắp Tới

- [ ] **Mobile App** - React Native companion
- [ ] **Real-time Notifications** - WebSocket integration
- [ ] **Advanced Analytics** - Machine learning insights
- [ ] **Inventory Management** - Stock tracking
- [ ] **Appointment Booking** - Calendar integration
- [ ] **Multi-language Support** - i18n implementation

## 🤝 Contributing

1. Fork the project
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👥 Team

- **Backend Developer** - Java/Servlet expert
- **Frontend Developer** - JSP/JSTL specialist
- **Database Designer** - SQL Server architect
- **UI/UX Designer** - Modern web design

## 📞 Support

- 📧 Email: support@gymmanager.com
- 🌐 Website: https://gymmanager.com
- 📱 Hotline: 1900-1234

---

Made with ❤️ for the gym community
