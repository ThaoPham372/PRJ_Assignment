# 🧪 Hướng Dẫn Setup JUnit 5 cho Project

## 📋 Tổng Quan

JUnit 5 đã được thêm vào project với đầy đủ dependencies và plugins cần thiết để chạy unit tests.

## 🔧 Dependencies Đã Thêm

### 1. **JUnit 5 Core**
```xml
<!-- JUnit 5 API -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
</dependency>

<!-- JUnit 5 Engine -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
</dependency>

<!-- JUnit 5 Parameters -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <version>5.9.2</version>
    <scope>test</scope>
</dependency>
```

### 2. **Mockito (Mocking Framework)**
```xml
<!-- Mockito Core -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>5.3.1</version>
    <scope>test</scope>
</dependency>

<!-- Mockito JUnit Integration -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>5.3.1</version>
    <scope>test</scope>
</dependency>
```

### 3. **H2 Database (In-Memory Testing)**
```xml
<!-- H2 Database for Testing -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.1.214</version>
    <scope>test</scope>
</dependency>
```

## 🚀 Maven Plugins

### 1. **Maven Surefire Plugin**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0</version>
    <configuration>
        <includes>
            <include>**/*Test.java</include>
            <include>**/*Tests.java</include>
        </includes>
    </configuration>
</plugin>
```

### 2. **Maven Compiler Plugin (Updated)**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <source>11</source>
        <target>11</target>
        <testSource>11</testSource>
        <testTarget>11</testTarget>
    </configuration>
</plugin>
```

## 📁 Cấu Trúc Thư Mục Test

```
src/
├── main/
│   ├── java/
│   │   ├── model/
│   │   ├── DAO/
│   │   ├── service/
│   │   └── controller/
│   └── webapp/
└── test/
    └── java/
        └── test/
            ├── MemberDAOTest.java
            ├── MemberServiceTest.java
            ├── UserDAOTest.java
            ├── UserServiceTest.java
            └── TestRunner.java
```

## 🎯 Cách Sử Dụng

### 1. **Chạy Tất Cả Tests**
```bash
# Chạy tất cả tests
mvn test

# Chạy với verbose output
mvn test -X

# Chạy specific test class
mvn test -Dtest=MemberDAOTest

# Chạy specific test method
mvn test -Dtest=MemberDAOTest#testCreateMember
```

### 2. **Chạy Tests trong IDE**

#### **IntelliJ IDEA:**
1. Right-click vào test class
2. Chọn "Run 'MemberDAOTest'"
3. Hoặc click vào icon ▶️ bên cạnh method

#### **Eclipse:**
1. Right-click vào test class
2. Chọn "Run As" > "JUnit Test"
3. Hoặc sử dụng keyboard shortcut: Alt+Shift+X, T

#### **VS Code:**
1. Install "Extension Pack for Java"
2. Click vào "Run Test" link bên cạnh test method
3. Hoặc sử dụng Command Palette: Ctrl+Shift+P > "Java: Run Tests"

### 3. **Chạy TestRunner.java**
```bash
# Compile và chạy TestRunner
mvn compile test-compile
mvn exec:java -Dexec.mainClass="test.TestRunner"
```

## 📝 Ví Dụ Test Class

### **MemberDAOTest.java**
```java
package test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import static org.junit.jupiter.api.Assertions.*;
import model.Member;
import DAO.MemberDAO;
import DAO.IMemberDAO;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberDAOTest {
    
    private IMemberDAO memberDAO;
    
    @BeforeEach
    void setUp() {
        memberDAO = new MemberDAO();
    }
    
    @Test
    @Order(1)
    @DisplayName("Test tạo member mới")
    void testCreateMember() {
        // Arrange
        Member member = new Member();
        member.setUserId(1);
        member.setMemberCode("MEM001");
        member.setHeight(175.0);
        member.setWeight(70.0);
        
        // Act
        boolean result = memberDAO.createMember(member);
        
        // Assert
        assertTrue(result, "Tạo member phải thành công");
    }
    
    @Test
    @Order(2)
    @DisplayName("Test lấy member theo ID")
    void testGetMemberById() {
        // Arrange
        int memberId = 1;
        
        // Act
        Member member = memberDAO.getMemberById(memberId);
        
        // Assert
        assertNotNull(member, "Member không được null");
        assertEquals(memberId, member.getMemberId(), "ID phải khớp");
    }
}
```

### **MemberServiceTest.java (với Mockito)**
```java
package test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import model.Member;
import model.User;
import service.MemberService;
import service.IMemberService;
import DAO.IMemberDAO;
import DAO.IUserDAO;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    
    @Mock
    private IMemberDAO memberDAO;
    
    @Mock
    private IUserDAO userDAO;
    
    private IMemberService memberService;
    
    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberDAO, userDAO);
    }
    
    @Test
    @DisplayName("Test đăng ký member mới")
    void testRegisterMember() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        
        Member member = new Member();
        member.setHeight(175.0);
        member.setWeight(70.0);
        
        when(userDAO.createUser(any(User.class))).thenReturn(true);
        when(memberDAO.createMember(any(Member.class))).thenReturn(true);
        
        // Act
        var result = memberService.registerMember(user, member);
        
        // Assert
        assertTrue(result.isSuccess(), "Đăng ký phải thành công");
        verify(userDAO).createUser(any(User.class));
        verify(memberDAO).createMember(any(Member.class));
    }
}
```

## 🔧 Cấu Hình Database Test

### **H2 Database Configuration**
```java
// Trong test class
@BeforeAll
static void setUpDatabase() {
    // Cấu hình H2 database cho testing
    System.setProperty("db.url", "jdbc:h2:mem:testdb");
    System.setProperty("db.username", "sa");
    System.setProperty("db.password", "");
}
```

### **Test Database Schema**
```sql
-- Tạo tables trong H2 database
CREATE TABLE Users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'member'
);

CREATE TABLE Members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    member_code VARCHAR(20) UNIQUE NOT NULL,
    height DECIMAL(5,2),
    weight DECIMAL(5,2),
    FOREIGN KEY (user_id) REFERENCES Users(id)
);
```

## 📊 Test Reports

### **Maven Surefire Reports**
```bash
# Tạo test report
mvn surefire-report:report

# Xem report
open target/site/surefire-report.html
```

### **JUnit 5 Test Reports**
- **Console Output**: Hiển thị kết quả test trong console
- **IDE Integration**: Kết quả hiển thị trong IDE test runner
- **HTML Reports**: Maven Surefire tạo HTML reports

## 🐛 Troubleshooting

### **Lỗi Thường Gặp**

#### 1. **"No tests found"**
```bash
# Kiểm tra naming convention
# Test class phải kết thúc bằng "Test" hoặc "Tests"
# Ví dụ: MemberDAOTest.java, UserServiceTests.java
```

#### 2. **"Class not found"**
```bash
# Compile test sources
mvn test-compile

# Hoặc compile tất cả
mvn compile test-compile
```

#### 3. **"Database connection error"**
```java
// Sử dụng H2 database cho testing
// Hoặc mock database connections
@Mock
private Connection mockConnection;
```

#### 4. **"Mockito not working"**
```java
// Đảm bảo sử dụng @ExtendWith(MockitoExtension.class)
@ExtendWith(MockitoExtension.class)
public class TestClass {
    @Mock
    private SomeClass mockObject;
}
```

## 🎯 Best Practices

### 1. **Test Naming**
```java
// Tên test method phải mô tả rõ ràng
@Test
@DisplayName("Should create member when valid data provided")
void shouldCreateMemberWhenValidDataProvided() {
    // test implementation
}
```

### 2. **Test Structure (AAA Pattern)**
```java
@Test
void testMethod() {
    // Arrange - Chuẩn bị dữ liệu
    Member member = new Member();
    member.setHeight(175.0);
    
    // Act - Thực hiện hành động
    boolean result = memberDAO.createMember(member);
    
    // Assert - Kiểm tra kết quả
    assertTrue(result);
}
```

### 3. **Test Isolation**
```java
@BeforeEach
void setUp() {
    // Mỗi test chạy độc lập
    // Reset state nếu cần
}

@AfterEach
void tearDown() {
    // Cleanup sau mỗi test
}
```

## 🚀 Next Steps

1. **Chạy tests hiện tại**:
   ```bash
   mvn test
   ```

2. **Tạo thêm test cases** cho các class khác

3. **Setup CI/CD** để chạy tests tự động

4. **Code coverage** với JaCoCo plugin

5. **Integration tests** với TestContainers

## 📚 Tài Liệu Tham Khảo

- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)
- [H2 Database](https://www.h2database.com/html/main.html)

---

**🎉 JUnit 5 đã sẵn sàng sử dụng!** Bạn có thể bắt đầu viết và chạy tests ngay bây giờ.
