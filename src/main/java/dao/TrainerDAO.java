package dao;

import java.util.List;
import model.Trainer;

/*
    Note: 
 */
public class TrainerDAO {

    GenericDAO<Trainer> genericDAO;

    public TrainerDAO() {
        genericDAO = new GenericDAO<>(Trainer.class);
    }

    public int save(Trainer trainer) {
        genericDAO.save(trainer);
        return trainer.getUserId();
    }

    public List<Trainer> findAll() {
        System.out.println(">>Trainer: FIND ALL");
        System.out.println("result: ");
        List<Trainer> trainers = genericDAO.findAll();
        if (trainers != null) {
            System.out.println("");
            for (Trainer a : trainers) {
                System.out.println(a);
            }
        }
        System.out.println("-----------------------------");
        return trainers != null ? trainers : List.of();
    }

    public Trainer findById(int id) {
        System.out.println(">>Trainer: Find by ID");
        Trainer trainer = genericDAO.findById(id);
        System.out.println("result: " + trainer);
        System.out.println("-------------------------------");
        return trainer;
    }

    public Trainer findByName(String name) {
        System.out.println(">>Trainer: Find By Name");
        Trainer trainer = genericDAO.findByField("name", name);
        System.out.println("result: " + trainer);
        System.out.println("---------------------------");
        return trainer;
    }

    public Trainer findByEmail(String email) {
        System.out.println(">>Trainer: Find By Email");
        Trainer trainer = genericDAO.findByField("email", email);
        System.out.println("result: " + trainer);
        System.out.println("---------------------------");
        return trainer;
    }

    public int existsByName(String name) {
        System.out.println(">>Trainer: Exist By Name");
        Trainer trainer = genericDAO.findByField("name", name);
        System.out.println("Result: " + trainer);
        System.out.println("-------------------------------");
        return trainer != null ? trainer.getUserId() : -1;
    }

    public int existsByEmail(String email) {
        System.out.println(">>Trainer: Exist By Email");
        Trainer trainer = genericDAO.findByField("email", email);
        System.out.println("Result: " + trainer);
        System.out.println("-------------------------------");
        return trainer != null ? trainer.getUserId(): -1;
    }

    public Trainer findByNameOrEmail(String nameOrEmail) {
        System.out.println(">>Trainer: Find By Name Or Email");
        Trainer trainer = genericDAO.findByField("name", nameOrEmail);
        if (trainer == null) {
            trainer = genericDAO.findByField("email", nameOrEmail);
        }
        System.out.println("Result: " + trainer);
        System.out.println("---------------------------");
        return trainer;
    }

    public int update(Trainer trainer) {
        System.out.println(">>Trainer: Update");
        int id = genericDAO.update(trainer);
        System.out.println("Result id: " + id);
        System.out.println("---------------------------");
        return id;
    }

    public int incrementFailedLoginAttempts(Trainer trainer) {
        System.out.println(">>Trainer: Increment Failed Login Attempts");
        trainer.setFailedLoginAttempts(trainer.getFailedLoginAttempts() + 1);
        genericDAO.update(trainer);
        System.out.println("New Failed Attempts: " + trainer.getFailedLoginAttempts());
        System.out.println("---------------------------");
        return trainer.getFailedLoginAttempts();
    }

    public int resetFailedLoginAttempts(Trainer trainer) {
        System.out.println(">>Trainer: Reset Failed Login Attempts");
        trainer.setFailedLoginAttempts(0);
        genericDAO.update(trainer);
        System.out.println("Failed Attempts reset to 0");
        System.out.println("---------------------------");
        return 0;
    }

    public int resetLockedUntil(Trainer trainer) {
        System.out.println(">>Trainer: Reset Locked Until");
        trainer.setLockedUntil(null);
        genericDAO.update(trainer);
        System.out.println("LockedUntil reset to null");
        System.out.println("---------------------------");
        return 0;
    }

    public int lockAccount(Trainer trainer, int minutes) {
        System.out.println(">>Trainer: Lock Account for " + minutes + " minutes");
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.MINUTE, minutes);
        trainer.setLockedUntil(cal.getTime());
        genericDAO.update(trainer);
        System.out.println("Account locked until: " + trainer.getLockedUntil());
        System.out.println("---------------------------");
        return minutes;
    }

    public void updateLastLogin(int trainerId) {
        System.out.println(">>Trainer: Update Last Login");
        Trainer trainer = genericDAO.findById(trainerId);
        if (trainer != null) {
            trainer.setLastLogin(new java.util.Date());
            genericDAO.update(trainer);
            System.out.println("Last login updated to: " + trainer.getLastLogin());
        }
        System.out.println("---------------------------");
    }

    public int delete(Trainer trainer) {
        System.out.println(">>Trainer: Delete (update status)");
        trainer.setStatus("INACTIVE");
        int trainerId = genericDAO.update(trainer);
        System.out.println("update trainer id: " + trainerId + ", status: " + trainer.getStatus());
        System.out.println("---------------------------");
        return trainerId;
    }

}
