package com.example.todoapp.controllers;

import com.example.todoapp.models.Todo;
import com.example.todoapp.repositories.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class TodoController {

    @Autowired
    TodoRepository todoRepository;

    @GetMapping("/todos")
    public List<Todo> getAllTodos(){
//        Sort sortByCreatedAtDesc = new Sort(Sort.Direction.DESC, "createdAt");
        return todoRepository.findAll();
    }

    @PostMapping("/todos")
    public Todo createTodo(@Valid @RequestBody Todo todo){
        todo.setCompleted(true);
        return todoRepository.save(todo);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable("id") String id){
        return todoRepository.findById(id)
                .map((todo -> ResponseEntity.ok().body(todo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<Todo> updateTodo(@PathVariable("id") String id, @Valid @RequestBody Todo todo) {
        return todoRepository.findById(id)
                .map(todoData -> {
                    todoData.setTitle(todo.getTitle());
                    todoData.setCompleted(todo.getCompleted());
                    Todo updatedTodo = todoRepository.save(todoData);
                    return ResponseEntity.ok().body(updatedTodo);
                }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable("id") String id){
        return todoRepository.findById(id)
                .map(todo-> {
                    todoRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}
