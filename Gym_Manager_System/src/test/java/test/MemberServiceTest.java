//package test;
//
//import service.IMemberService;
//import service.MemberService;
//import service.MemberService.ServiceResponse;
//import service.MemberService.MemberStatistics;
//import model.Member;
//import model.User;
//import org.junit.jupiter.api.*;
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests cho MemberService
// * Tests against IMemberService interface for better abstraction
// */
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class MemberServiceTest {
//    
//    private static IMemberService memberService; // Test against interface
//    private static int testMemberId;
//    private static int testUserId;
//    
//    @BeforeAll
//    public static void setUpClass() {
//        memberService = new MemberService(); // Instantiate concrete implementation
//        System.out.println("=== Starting MemberService Tests ===");
//    }
//    
//    @AfterAll
//    public static void tearDownClass() {
//        System.out.println("=== Finished MemberService Tests ===");
//    }
//    
//    @Test
//    @Order(1)
//    @DisplayName("Test 1: Register Member - Success")
//    public void testRegisterMemberSuccess() {
//        System.out.println("\n[TEST 1] Registering new member...");
//        
//        // Create User
//        User user = new User();
//        user.setUsername("testmember" + System.currentTimeMillis());
//        user.setPassword("password123");
//        user.setFullName("Test Member");
//        user.setEmail("test@example.com");
//        user.setPhone("0123456789");
//        user.setRole("member");
//        
//        // Create Member
//        Member member = new Member();
//        member.setHeight(175.0);
//        member.setWeight(70.0);
//        member.setFitnessGoal("maintain");
//        member.setEmergencyContactName("Emergency Contact");
//        member.setEmergencyContactPhone("0987654321");
//        member.setEmergencyContactRelation("parent");
//        
//        ServiceResponse<Member> response = memberService.registerMember(user, member);
//        
//        assertTrue(response.isSuccess(), "Registration should succeed");
//        assertNotNull(response.getData(), "Member data should be returned");
//        assertNotNull(response.getData().getMemberCode(), "Member code should be generated");
//        assertNotNull(response.getData().getBmi(), "BMI should be calculated");
//        
//        testMemberId = response.getData().getMemberId();
//        testUserId = response.getData().getUserId();
//        
//        System.out.println("✓ Member registered: " + response.getData().getMemberCode());
//        System.out.println("  BMI: " + response.getData().getBmi());
//    }
//    
//    @Test
//    @Order(2)
//    @DisplayName("Test 2: Register Member - Missing Required Fields")
//    public void testRegisterMemberMissingFields() {
//        System.out.println("\n[TEST 2] Testing registration with missing fields...");
//        
//        User user = new User();
//        user.setUsername("incomplete");
//        // Missing password, email, fullName
//        
//        Member member = new Member();
//        // Missing emergency contact
//        
//        ServiceResponse<Member> response = memberService.registerMember(user, member);
//        
//        assertFalse(response.isSuccess(), "Registration should fail");
//        assertNotNull(response.getMessage(), "Error message should be provided");
//        
//        System.out.println("✓ Validation working: " + response.getMessage());
//    }
//    
//    @Test
//    @Order(3)
//    @DisplayName("Test 3: Get Member Full Info")
//    public void testGetMemberFullInfo() {
//        System.out.println("\n[TEST 3] Getting member full info...");
//        
//        MemberService.MemberWithUserInfo info = memberService.getMemberFullInfo(testMemberId);
//        
//        assertNotNull(info, "Member info should be found");
//        assertNotNull(info.getMember(), "Member should not be null");
//        assertNotNull(info.getUser(), "User should not be null");
//        assertEquals(testMemberId, info.getMember().getMemberId(), "Member ID should match");
//        assertEquals(testUserId, info.getUser().getId(), "User ID should match");
//        
//        System.out.println("✓ Found member: " + info.getUser().getFullName());
//        System.out.println("  Member code: " + info.getMember().getMemberCode());
//    }
//    
//    @Test
//    @Order(4)
//    @DisplayName("Test 4: Update Member Profile")
//    public void testUpdateMemberProfile() {
//        System.out.println("\n[TEST 4] Updating member profile...");
//        
//        Member member = memberService.getMemberById(testMemberId);
//        assertNotNull(member, "Member should exist");
//        
//        User user = new User();
//        user.setId(testUserId);
//        user.setFullName("Updated Test Member");
//        user.setEmail("updated@example.com");
//        user.setPhone("0111222333");
//        
//        member.setHeight(180.0);
//        member.setWeight(75.0);
//        member.setFitnessGoal("build_muscle");
//        member.setNotes("Updated profile");
//        
//        ServiceResponse<Member> response = memberService.updateMemberProfile(member, user);
//        
//        assertTrue(response.isSuccess(), "Update should succeed");
//        
//        // Verify BMI recalculation
//        Member updatedMember = memberService.getMemberById(testMemberId);
//        assertNotEquals(member.getBmi(), updatedMember.getBmi(), "BMI should be recalculated");
//        
//        System.out.println("✓ Profile updated");
//        System.out.println("  New BMI: " + updatedMember.getBmi());
//    }
//    
//    @Test
//    @Order(5)
//    @DisplayName("Test 5: Get All Members")
//    public void testGetAllMembers() {
//        System.out.println("\n[TEST 5] Getting all members...");
//        
//        List<Member> members = memberService.getAllMembers();
//        
//        assertNotNull(members, "Members list should not be null");
//        assertTrue(members.size() > 0, "Should have at least one member");
//        
//        System.out.println("✓ Found " + members.size() + " members");
//    }
//    
//    @Test
//    @Order(6)
//    @DisplayName("Test 6: Get Active Members")
//    public void testGetActiveMembers() {
//        System.out.println("\n[TEST 6] Getting active members...");
//        
//        List<Member> activeMembers = memberService.getActiveMembers();
//        
//        assertNotNull(activeMembers, "Active members list should not be null");
//        
//        System.out.println("✓ Found " + activeMembers.size() + " active members");
//    }
//    
//    @Test
//    @Order(7)
//    @DisplayName("Test 7: Search Members")
//    public void testSearchMembers() {
//        System.out.println("\n[TEST 7] Searching members...");
//        
//        // Search by name
//        List<Member> results = memberService.searchMembers("Test");
//        
//        assertNotNull(results, "Search results should not be null");
//        assertTrue(results.size() > 0, "Should find at least one member");
//        
//        System.out.println("✓ Search found " + results.size() + " members");
//    }
//    
//    @Test
//    @Order(8)
//    @DisplayName("Test 8: Assign Coach")
//    public void testAssignCoach() {
//        System.out.println("\n[TEST 8] Assigning coach...");
//        
//        ServiceResponse<Member> response = memberService.assignCoach(testMemberId, 1);
//        
//        assertTrue(response.isSuccess(), "Coach assignment should succeed");
//        
//        Member member = memberService.getMemberById(testMemberId);
//        assertEquals(1, member.getAssignedCoachId(), "Coach ID should be set");
//        
//        System.out.println("✓ Coach assigned successfully");
//    }
//    
//    @Test
//    @Order(9)
//    @DisplayName("Test 9: Record Workout Session")
//    public void testRecordWorkoutSession() {
//        System.out.println("\n[TEST 9] Recording workout session...");
//        
//        Member beforeMember = memberService.getMemberById(testMemberId);
//        int sessionsBefore = beforeMember.getTotalWorkoutSessions();
//        
//        ServiceResponse<Void> response = memberService.recordWorkoutSession(testMemberId);
//        
//        assertTrue(response.isSuccess(), "Workout recording should succeed");
//        
//        Member afterMember = memberService.getMemberById(testMemberId);
//        assertEquals(sessionsBefore + 1, afterMember.getTotalWorkoutSessions(), 
//                    "Sessions should increase by 1");
//        assertNotNull(afterMember.getLastWorkoutDate(), "Last workout date should be set");
//        
//        System.out.println("✓ Workout recorded: " + afterMember.getTotalWorkoutSessions() + " sessions");
//    }
//    
//    @Test
//    @Order(10)
//    @DisplayName("Test 10: Get Member Statistics")
//    public void testGetMemberStatistics() {
//        System.out.println("\n[TEST 10] Getting member statistics...");
//        
//        MemberStatistics stats = memberService.getMemberStatistics(testMemberId);
//        
//        assertNotNull(stats, "Statistics should not be null");
//        assertNotNull(stats.getTotalSessions(), "Total sessions should be set");
//        assertNotNull(stats.getCurrentStreak(), "Current streak should be set");
//        assertNotNull(stats.getBmi(), "BMI should be set");
//        assertNotNull(stats.getBmiCategory(), "BMI category should be set");
//        
//        System.out.println("✓ Statistics retrieved:");
//        System.out.println("  Total sessions: " + stats.getTotalSessions());
//        System.out.println("  Current streak: " + stats.getCurrentStreak());
//        System.out.println("  BMI: " + stats.getBmi() + " (" + stats.getBmiCategory() + ")");
//    }
//    
//    @Test
//    @Order(11)
//    @DisplayName("Test 11: Check Membership Active")
//    public void testIsMembershipActive() {
//        System.out.println("\n[TEST 11] Checking membership status...");
//        
//        boolean isActive = memberService.isMembershipActive(testMemberId);
//        
//        // Should be false since we didn't set package dates
//        assertFalse(isActive, "Membership should not be active without package");
//        
//        System.out.println("✓ Membership status checked: " + (isActive ? "Active" : "Inactive"));
//    }
//    
//    @Test
//    @Order(12)
//    @DisplayName("Test 12: Get Total Members Count")
//    public void testGetTotalMembersCount() {
//        System.out.println("\n[TEST 12] Getting total members count...");
//        
//        int count = memberService.getTotalMembersCount();
//        
//        assertTrue(count > 0, "Should have at least one member");
//        
//        System.out.println("✓ Total members: " + count);
//    }
//    
//    @Test
//    @Order(13)
//    @DisplayName("Test 13: Deactivate Member")
//    public void testDeactivateMember() {
//        System.out.println("\n[TEST 13] Deactivating member...");
//        
//        ServiceResponse<Void> response = memberService.deactivateMember(testMemberId);
//        
//        assertTrue(response.isSuccess(), "Deactivation should succeed");
//        
//        Member member = memberService.getMemberById(testMemberId);
//        assertEquals("inactive", member.getStatus(), "Status should be inactive");
//        
//        System.out.println("✓ Member deactivated successfully");
//    }
//    
//    @Test
//    @Order(14)
//    @DisplayName("Test 14: BMI Calculation Logic")
//    public void testBMICalculationLogic() {
//        System.out.println("\n[TEST 14] Testing BMI calculation logic...");
//        
//        Member member = new Member();
//        
//        // Test Underweight
//        member.setHeight(175.0);
//        member.setWeight(50.0);
//        assertTrue(member.getBmi() < 18.5, "Should be underweight");
//        assertEquals("Underweight", member.getBMICategory());
//        
//        // Test Normal
//        member.setWeight(70.0);
//        assertTrue(member.getBmi() >= 18.5 && member.getBmi() < 25, "Should be normal");
//        assertEquals("Normal", member.getBMICategory());
//        
//        // Test Overweight
//        member.setWeight(85.0);
//        assertTrue(member.getBmi() >= 25 && member.getBmi() < 30, "Should be overweight");
//        assertEquals("Overweight", member.getBMICategory());
//        
//        // Test Obese
//        member.setWeight(100.0);
//        assertTrue(member.getBmi() >= 30, "Should be obese");
//        assertEquals("Obese", member.getBMICategory());
//        
//        System.out.println("✓ BMI calculation logic verified");
//    }
//}
//
