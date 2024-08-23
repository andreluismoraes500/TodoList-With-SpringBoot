package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.MessageErros.TodoNotFoundException;
import com.example.demo.dto.TodoDTO;
import com.example.demo.model.Todo;
import com.example.demo.repository.TodoRepository;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public List<TodoDTO> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TodoDTO getTodoById(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException("Todo com ID " + id + " não foi encontrado."));
        return convertToDTO(todo);
    }

    public TodoDTO createTodo(TodoDTO todoDTO) {
        Todo todo = convertToEntity(todoDTO);
        Todo savedTodo = todoRepository.save(todo);
        return convertToDTO(savedTodo);
    }

    public TodoDTO updateTodo(Long id, TodoDTO todoDTO) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(
                        () -> new TodoNotFoundException(
                                "Todo com ID " + id + " não pôde ser encontrado para ser atualizado."));
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCompleted(todoDTO.isCompleted());
        Todo updatedTodo = todoRepository.save(todo);
        return convertToDTO(updatedTodo);
    }

    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(
                        "Todo com este ID " + id + " não pôde ser encontrado para ser deletado."));
        todoRepository.delete(todo);
    }

    private TodoDTO convertToDTO(Todo todo) {
        TodoDTO todoDTO = new TodoDTO();
        todoDTO.setId(todo.getId());
        todoDTO.setTitle(todo.getTitle());
        todoDTO.setDescription(todo.getDescription());
        todoDTO.setCompleted(todo.isCompleted());
        return todoDTO;
    }

    private Todo convertToEntity(TodoDTO todoDTO) {
        Todo todo = new Todo();
        todo.setId(todoDTO.getId());
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCompleted(todoDTO.isCompleted());
        return todo;
    }
}
