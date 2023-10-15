package br.com.andrelopes.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.andrelopes.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private iTaskRepository taskRepository;
    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel , HttpServletRequest request){
        System.out.println("chegou no controlller" );
        var idUser = request.getAttribute("userName");
        taskModel.setUsuario((UUID) idUser);
        var dataAtual = LocalDateTime.now();
        if(dataAtual.isAfter(taskModel.getDataInicio()) || dataAtual.isAfter(taskModel.getDataTermino()) ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A data de inicio / data de término não pode ser menor que a data atual");
        }
        if(taskModel.getDataInicio().isAfter(taskModel.getDataTermino()) ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("A data de inicio deve ser menor que a data de término");
        }
        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    /**
     * @param request
     * @return 
     */
    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request){
        var idUser = request.getAttribute("userName");
        var tasks = this.taskRepository.findByUsuario((UUID)idUser);
        return tasks;
    }
    
    //http:localhost:8080/tasks/15632-4221362-4213413
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id, HttpServletRequest request ){
        // buscar task no banco por id ou retornar null caso não exista

        var task = this.taskRepository.findById(id).orElse(null);

        if(task == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body("Tarefa não encontrada");
        }
        
        var idUser = request.getAttribute("userName");
        if(!task.getUsuario().equals(idUser)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Você não tem permissão para alterar essa tarefa");
        }

        Utils.copyNonNullProperties(taskModel, task);
        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }

}
