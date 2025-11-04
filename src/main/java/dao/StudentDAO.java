package dao;

import java.util.List;
import model.Student;

/*
    Note: 
 */
public class StudentDAO {

    GenericDAO<Student> genericDAO;

    public StudentDAO() {
        genericDAO = new GenericDAO<>(Student.class);
    }

    public int save(Student student) {
        genericDAO.save(student);
        return student.getUserId();
    }

    public List<Student> findAll() {
        System.out.println(">>Student: FIND ALL");
        System.out.println("result: ");
        List<Student> students = genericDAO.findAll();
        if (students != null) {
            System.out.println("");
            for (Student a : students) {
                System.out.println(a);
            }
        }
        System.out.println("-----------------------------");
        return students != null ? students : List.of();
    }

    public Student findById(int id) {
        System.out.println(">>Student: Find by ID");
        Student student = genericDAO.findById(id);
        System.out.println("result: " + student);
        System.out.println("-------------------------------");
        return student;
    }

    public Student findByName(String name) {
        System.out.println(">>Student: Find By Name");
        Student student = genericDAO.findByField("name", name);
        System.out.println("result: " + student);
        System.out.println("---------------------------");
        return student;
    }

    public Student findByEmail(String email) {
        System.out.println(">>Student: Find By Email");
        Student student = genericDAO.findByField("email", email);
        System.out.println("result: " + student);
        System.out.println("---------------------------");
        return student;
    }

    public int existsByName(String name) {
        System.out.println(">>Student: Exist By Name");
        Student student = genericDAO.findByField("name", name);
        System.out.println("Result: " + student);
        System.out.println("-------------------------------");
        return student != null ? student.getUserId() : -1;
    }

    public int existsByEmail(String email) {
        System.out.println(">>Student: Exist By Email");
        Student student = genericDAO.findByField("email", email);
        System.out.println("Result: " + student);
        System.out.println("-------------------------------");
        return student != null ? student.getUserId() : -1;
    }

    public Student findByNameOrEmail(String nameOrEmail) {
        System.out.println(">>Student: Find By Name Or Email");
        Student student = genericDAO.findByField("name", nameOrEmail);
        if (student == null) {
            student = genericDAO.findByField("email", nameOrEmail);
        }
        System.out.println("Result: " + student);
        System.out.println("---------------------------");
        return student;
    }

    public int update(Student student) {
        System.out.println(">>Student: Update");
        int id = genericDAO.update(student);
        System.out.println("Result id: " + id);
        System.out.println("---------------------------");
        return id;
    }

    public int incrementFailedLoginAttempts(Student student) {
        System.out.println(">>Student: Increment Failed Login Attempts");
        student.setFailedLoginAttempts(student.getFailedLoginAttempts() + 1);
        genericDAO.update(student);
        System.out.println("New Failed Attempts: " + student.getFailedLoginAttempts());
        System.out.println("---------------------------");
        return student.getFailedLoginAttempts();
    }

    public int resetFailedLoginAttempts(Student student) {
        System.out.println(">>Student: Reset Failed Login Attempts");
        student.setFailedLoginAttempts(0);
        genericDAO.update(student);
        System.out.println("Failed Attempts reset to 0");
        System.out.println("---------------------------");
        return 0;
    }

    public int resetLockedUntil(Student student) {
        System.out.println(">>Student: Reset Locked Until");
        student.setLockedUntil(null);
        genericDAO.update(student);
        System.out.println("LockedUntil reset to null");
        System.out.println("---------------------------");
        return 0;
    }

    public int lockAccount(Student student, int minutes) {
        System.out.println(">>Student: Lock Account for " + minutes + " minutes");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.MINUTE, minutes);
        student.setLockedUntil(cal.getTime());
        genericDAO.update(student);
        System.out.println("Account locked until: " + student.getLockedUntil());
        System.out.println("---------------------------");
        return minutes;
    }

    public void updateLastLogin(int studentId) {
        System.out.println(">>Student: Update Last Login");
        Student student = genericDAO.findById(studentId);
        if (student != null) {
            student.setLastLogin(new java.util.Date());
            genericDAO.update(student);
            System.out.println("Last login updated to: " + student.getLastLogin());
        }
        System.out.println("---------------------------");
    }

    public int delete(Student student) {
        System.out.println(">>Student: Delete (update status)");
        student.setStatus("INACTIVE");
        int studentId = genericDAO.update(student);
        System.out.println("update student id: " + studentId + ", status: " + student.getStatus());
        System.out.println("---------------------------");
        return studentId;
    }

}
