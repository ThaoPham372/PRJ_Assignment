
package service;

import dao.StudentDAO;
import model.Student;
import java.util.List;

/*
    Note: 
 */
public class StudentService {
    
    private final StudentDAO studentDAO;
    
    public StudentService() {
        studentDAO = new StudentDAO();
    }
    
    //Student
    public List<Student> getAll() {
        return studentDAO.findAll();
    }
    
    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }
    
    public Student getStudentByName(String name) {
        return studentDAO.findByName(name);
    }
    
    public Student getStudentByEmail(String email) {
        return studentDAO.findByEmail(email);
    }
    
    public int add(Student student) {
        return studentDAO.save(student);
    }

    public int update(Student student) {
        return studentDAO.update(student);
    }

    public int delete(Student student) {
        return studentDAO.delete(student);
    }
}
