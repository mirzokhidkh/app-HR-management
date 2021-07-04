package uz.mk.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mk.apphrmanagement.entity.Task;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.payload.TaskDto;
import uz.mk.apphrmanagement.service.TaskService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    @Autowired
    TaskService taskService;


    @GetMapping
    public HttpEntity<?> getAll() {
        List<Task> tasks = taskService.getAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/byUser/{userId}")
    public HttpEntity<?> getAllByUserId(@PathVariable UUID userId) {
        List<Task> tasks = taskService.getAllByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    //ATTACH TASK TO MANAGER OR STAFF
    @PostMapping("/add")
    public HttpEntity<?> add(@RequestParam Integer roleId,
                             @RequestBody TaskDto taskDto) {
        ApiResponse response = taskService.add(roleId, taskDto);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    //SEND REPORT ABOUT COMPLETED TASK TO MANAGER OR DIRECTOR
    @PostMapping("/sendReport")
    public HttpEntity<?> sendReport(@RequestParam UUID taskId) {
        ApiResponse response = taskService.sendReport(taskId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


}
