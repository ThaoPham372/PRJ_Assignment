//package test;
//
//import DAO.IMemberDAO;
//import DAO.MemberDAO;
//import model.Member;
//import org.junit.jupiter.api.*;
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * Unit tests cho MemberDAO
// * Tests against IMemberDAO interface for better abstraction
// */
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//public class MemberDAOTest {
//    
//    private static IMemberDAO memberDAO; // Test against interface
//    private static Member testMember;
//    private static int testMemberId;
//    
//    @BeforeAll
//    public static void setUpClass() {
//        memberDAO = new MemberDAO(); // Instantiate concrete implementation
//        System.out.println("=== Starting MemberDAO Tests ===");
//    }
//    
//    @AfterAll
//    public static void tearDownClass() {
//        System.out.println("=== Finished MemberDAO Tests ===");
//    }
//    
//    @Test
//    @Order(1)
//    @DisplayName("Test 1: Create Member")
//    public void testCreateMember() {
//        System.out.println("\n[TEST 1] Creating new member...");
//        
//        testMember = new Member();
//        testMember.setUserId(1); // Assuming user_id=1 exists
//        testMember.setMemberCode("GYM2024TEST");
//        testMember.setHeight(175.0);
//        testMember.setWeight(70.0);
//        testMember.setFitnessGoal("maintain");
//        testMember.setEmergencyContactName("Test Contact");
//        testMember.setEmergencyContactPhone("0123456789");
//        testMember.setEmergencyContactRelation("parent");
//        testMember.setStatus("active");
//        testMember.setTotalWorkoutSessions(0);
//        testMember.setCurrentStreak(0);
//        testMember.setLongestStreak(0);
//        testMember.setTotalCaloriesBurned(BigDecimal.ZERO);
//        
//        boolean result = memberDAO.createMember(testMember);
//        
//        assertTrue(result, "Member should be created successfully");
//        assertNotEquals(0, testMember.getMemberId(), "Member ID should be generated");
//        testMemberId = testMember.getMemberId();
//        
//        System.out.println("✓ Member created with ID: " + testMemberId);
//    }
//    
//    @Test
//    @Order(2)
//    @DisplayName("Test 2: Get Member by ID")
//    public void testGetMemberById() {
//        System.out.println("\n[TEST 2] Getting member by ID...");
//        
//        Member member = memberDAO.getMemberById(testMemberId);
//        
//        assertNotNull(member, "Member should be found");
//        assertEquals(testMemberId, member.getMemberId(), "Member ID should match");
//        assertEquals("GYM2024TEST", member.getMemberCode(), "Member code should match");
//        assertEquals(175.0, member.getHeight(), "Height should match");
//        assertEquals(70.0, member.getWeight(), "Weight should match");
//        
//        System.out.println("✓ Member retrieved: " + member.getMemberCode());
//    }
//    
//    @Test
//    @Order(3)
//    @DisplayName("Test 3: Get Member by Member Code")
//    public void testGetMemberByCode() {
//        System.out.println("\n[TEST 3] Getting member by code...");
//        
//        Member member = memberDAO.getMemberByCode("GYM2024TEST");
//        
//        assertNotNull(member, "Member should be found by code");
//        assertEquals(testMemberId, member.getMemberId(), "Member ID should match");
//        
//        System.out.println("✓ Member found by code");
//    }
//    
//    @Test
//    @Order(4)
//    @DisplayName("Test 4: Get All Members")
//    public void testGetAllMembers() {
//        System.out.println("\n[TEST 4] Getting all members...");
//        
//        List<Member> members = memberDAO.getAllMembers();
//        
//        assertNotNull(members, "Members list should not be null");
//        assertTrue(members.size() > 0, "Should have at least one member");
//        
//        System.out.println("✓ Found " + members.size() + " members");
//    }
//    
//    @Test
//    @Order(5)
//    @DisplayName("Test 5: Get Active Members")
//    public void testGetActiveMembers() {
//        System.out.println("\n[TEST 5] Getting active members...");
//        
//        List<Member> activeMembers = memberDAO.getActiveMembers();
//        
//        assertNotNull(activeMembers, "Active members list should not be null");
//        
//        // Verify all returned members are active
//        for (Member member : activeMembers) {
//            assertEquals("active", member.getStatus(), "All members should have active status");
//        }
//        
//        System.out.println("✓ Found " + activeMembers.size() + " active members");
//    }
//    
//    @Test
//    @Order(6)
//    @DisplayName("Test 6: Update Member")
//    public void testUpdateMember() {
//        System.out.println("\n[TEST 6] Updating member...");
//        
//        Member member = memberDAO.getMemberById(testMemberId);
//        assertNotNull(member, "Member should exist");
//        
//        // Update some fields
//        member.setHeight(180.0);
//        member.setWeight(75.0);
//        member.setBmi(23.15); // Calculated BMI
//        member.setFitnessGoal("build_muscle");
//        member.setNotes("Updated by test");
//        
//        boolean result = memberDAO.updateMember(member);
//        
//        assertTrue(result, "Member should be updated successfully");
//        
//        // Verify update
//        Member updatedMember = memberDAO.getMemberById(testMemberId);
//        assertEquals(180.0, updatedMember.getHeight(), "Height should be updated");
//        assertEquals(75.0, updatedMember.getWeight(), "Weight should be updated");
//        assertEquals("build_muscle", updatedMember.getFitnessGoal(), "Fitness goal should be updated");
//        
//        System.out.println("✓ Member updated successfully");
//    }
//    
//    @Test
//    @Order(7)
//    @DisplayName("Test 7: Increment Workout Session")
//    public void testIncrementWorkoutSession() {
//        System.out.println("\n[TEST 7] Incrementing workout session...");
//        
//        Member beforeMember = memberDAO.getMemberById(testMemberId);
//        int sessionsBefore = beforeMember.getTotalWorkoutSessions();
//        
//        boolean result = memberDAO.incrementWorkoutSession(testMemberId);
//        
//        assertTrue(result, "Workout session should be incremented");
//        
//        Member afterMember = memberDAO.getMemberById(testMemberId);
//        assertEquals(sessionsBefore + 1, afterMember.getTotalWorkoutSessions(), 
//                    "Total sessions should increase by 1");
//        assertNotNull(afterMember.getLastWorkoutDate(), "Last workout date should be set");
//        
//        System.out.println("✓ Workout session incremented: " + afterMember.getTotalWorkoutSessions());
//    }
//    
//    @Test
//    @Order(8)
//    @DisplayName("Test 8: Update Streak")
//    public void testUpdateStreak() {
//        System.out.println("\n[TEST 8] Updating streak...");
//        
//        boolean result = memberDAO.updateStreak(testMemberId, 5, 10);
//        
//        assertTrue(result, "Streak should be updated");
//        
//        Member member = memberDAO.getMemberById(testMemberId);
//        assertEquals(5, member.getCurrentStreak(), "Current streak should be 5");
//        assertEquals(10, member.getLongestStreak(), "Longest streak should be 10");
//        
//        System.out.println("✓ Streak updated successfully");
//    }
//    
//    @Test
//    @Order(9)
//    @DisplayName("Test 9: Search Members")
//    public void testSearchMembers() {
//        System.out.println("\n[TEST 9] Searching members...");
//        
//        List<Member> results = memberDAO.searchMembers("GYM2024TEST");
//        
//        assertNotNull(results, "Search results should not be null");
//        assertTrue(results.size() > 0, "Should find at least one member");
//        
//        boolean found = false;
//        for (Member member : results) {
//            if (member.getMemberCode().equals("GYM2024TEST")) {
//                found = true;
//                break;
//            }
//        }
//        assertTrue(found, "Should find the test member");
//        
//        System.out.println("✓ Search found " + results.size() + " members");
//    }
//    
//    @Test
//    @Order(10)
//    @DisplayName("Test 10: Get Total Members Count")
//    public void testGetTotalMembersCount() {
//        System.out.println("\n[TEST 10] Getting total members count...");
//        
//        int count = memberDAO.getTotalMembersCount();
//        
//        assertTrue(count > 0, "Should have at least one member");
//        
//        System.out.println("✓ Total members: " + count);
//    }
//    
//    @Test
//    @Order(11)
//    @DisplayName("Test 11: Delete Member (Soft Delete)")
//    public void testDeleteMember() {
//        System.out.println("\n[TEST 11] Deleting member (soft delete)...");
//        
//        boolean result = memberDAO.deleteMember(testMemberId);
//        
//        assertTrue(result, "Member should be deleted (deactivated)");
//        
//        // Verify soft delete
//        Member deletedMember = memberDAO.getMemberById(testMemberId);
//        assertNotNull(deletedMember, "Member record should still exist");
//        assertEquals("inactive", deletedMember.getStatus(), "Status should be inactive");
//        
//        System.out.println("✓ Member deactivated successfully");
//    }
//    
//    @Test
//    @Order(12)
//    @DisplayName("Test 12: BMI Calculation")
//    public void testBMICalculation() {
//        System.out.println("\n[TEST 12] Testing BMI calculation...");
//        
//        Member member = new Member();
//        member.setHeight(175.0); // 1.75m
//        member.setWeight(70.0);  // 70kg
//        
//        // BMI = 70 / (1.75^2) = 22.86
//        assertNotNull(member.getBmi(), "BMI should be calculated");
//        assertTrue(member.getBmi() > 22 && member.getBmi() < 23, "BMI should be around 22.86");
//        assertEquals("Normal", member.getBMICategory(), "BMI category should be Normal");
//        
//        System.out.println("✓ BMI calculated: " + member.getBmi() + " (" + member.getBMICategory() + ")");
//    }
//}
//
