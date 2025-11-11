package service;

import dao.TrainerStudentDAO;
import java.util.List;

/**
 * Service for Trainer Student Management
 * Handles business logic for managing trainer's students
 */
public class TrainerStudentService {
    private TrainerStudentDAO dao;

    public TrainerStudentService() {
        this.dao = new TrainerStudentDAO();
    }

    /**
     * Get list of students assigned to a trainer
     * 
     * @param trainerId The trainer ID
     * @return List of Object arrays containing student information
     *         Array indices: [0]=memberId, [1]=name, [2]=phone, [3]=email, [4]=gender, 
     *                        [5]=dob, [6]=weight, [7]=height, [8]=bmi, [9]=goal, 
     *                        [10]=ptNote, [11]=totalBookings, [12]=completedSessions,
     *                        [13]=confirmedSessions, [14]=pendingSessions
     */
    public List<Object[]> getTrainerStudents(Integer trainerId) {
        return dao.getStudentsByTrainer(trainerId);
    }

    /**
     * Get students with search and filter
     * 
     * @param trainerId The trainer ID
     * @param keyword Search keyword (name, phone, email)
     * @param packageName Filter by package name
     * @return List of Object arrays containing student information
     *         Array indices: [0]=memberId, [1]=name, [2]=phone, [3]=email, [4]=gender,
     *                        [5]=dob, [6]=weight, [7]=height, [8]=bmi, [9]=goal,
     *                        [10]=ptNote, [11]=packageName, [12]=totalBookings,
     *                        [13]=completedSessions, [14]=confirmedSessions, [15]=pendingSessions
     */
    public List<Object[]> getTrainerStudentsWithFilter(Integer trainerId, String keyword, String packageName) {
        return dao.getStudentsByTrainerWithFilter(trainerId, keyword, packageName);
    }

    /**
     * Get student statistics
     * 
     * @param trainerId The trainer ID
     * @return Object array: [0]=totalStudents, [1]=activeStudents, [2]=achievedGoalCount
     */
    public Object[] getStudentStatistics(Integer trainerId) {
        return dao.getStudentStatistics(trainerId);
    }

    /**
     * Get student detail
     * 
     * @param memberId The member ID
     * @param trainerId The trainer ID
     * @return Object array containing detailed student information
     */
    public Object[] getStudentDetail(Integer memberId, Integer trainerId) {
        return dao.getStudentDetail(memberId, trainerId);
    }
}

