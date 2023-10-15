package br.com.andrelopes.todolist.task;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface iTaskRepository extends JpaRepository<TaskModel, UUID> {
    /**
     * @param idUser
     * @return
     */
    List<TaskModel> findByUsuario(UUID idUser);
    /**
     * @param id
     * @param idUser
     * @return
     */
    TaskModel findByIdAndUsuario(UUID id, UUID idUser);
}