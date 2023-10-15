package br.com.andrelopes.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

/*
 * ID
 * Usuário
 * Descricao
 * Titulo
 * Data de inicio
 * Data de Termino
 * Prioridade
 */
@Data
@Entity(name = "tb_task")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private UUID usuario;
    private String descricao;
    @Column(length = 50)
    private String titulo;
    private LocalDateTime dataInicio;
    private LocalDateTime dataTermino;
    private String prioridade;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    public void setTitulo (String titulo) throws IllegalArgumentException {
        if(titulo.length() > 50){
            throw new IllegalArgumentException("O titulo não pode ter mais que 50 caracteres");
        }
        this.titulo = titulo;
    }
}
