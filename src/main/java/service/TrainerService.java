
package service;

import java.util.List;

import dao.TrainerDAO;
import model.Trainer;

/*
    Note: 
 */
public class TrainerService {

    private final TrainerDAO trainerDAO;

    public TrainerService() {
        trainerDAO = new TrainerDAO();
    }

    // Trainer
    public List<Trainer> getAll() {
        return trainerDAO.findAll();
    }

    public Trainer getTrainerById(int id) {
        return trainerDAO.findById(id);
    }

    public Trainer getTrainerByName(String name) {
        return trainerDAO.findByName(name);
    }

    public Trainer getTrainerByEmail(String email) {
        return trainerDAO.findByEmail(email);
    }

    public int add(Trainer trainer) {
        return trainerDAO.save(trainer);
    }

    public int update(Trainer trainer) {
        return trainerDAO.update(trainer);
    }

    public int delete(Trainer trainer) {
        return trainerDAO.delete(trainer);
    }
}
