package uz.mk.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.mk.apphrmanagement.entity.SalaryHistory;
import uz.mk.apphrmanagement.entity.Task;
import uz.mk.apphrmanagement.entity.WorkTimeHistory;
import uz.mk.apphrmanagement.repository.UserRepository;
import uz.mk.apphrmanagement.service.SalaryHistoryService;
import uz.mk.apphrmanagement.service.TaskService;
import uz.mk.apphrmanagement.service.WorkTimeHistoryService;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@RestController("/management")
public class ManagementController {

    @Autowired
    UserRepository userRepository;


    @Autowired
    TaskService taskService;

    @Autowired
    WorkTimeHistoryService workTimeHistoryService;

    @Autowired
    SalaryHistoryService salaryHistoryService;

    @GetMapping("/workTimeHistory/{userId}")
    public HttpEntity<?> getWorkTimeHistoriesByUserId(@PathVariable UUID userId) {
        List<WorkTimeHistory> workTimeHistories = workTimeHistoryService.getAllByUserId(userId);
        return ResponseEntity.ok(workTimeHistories);
    }


    @GetMapping("/task/{userId}")
    public HttpEntity<?> getTasksByUserId(@PathVariable UUID userId) {
        List<Task> tasks = taskService.getAllByUserId(userId);
        return ResponseEntity.ok(tasks);
    }


    @GetMapping("/salaryHistory/byMonth")
    public HttpEntity<?> getSalaryHistoriesByMonth(@RequestParam Date minDate, @RequestParam Date maxDate) {
        List<SalaryHistory> salaryHistoryList = salaryHistoryService.getAllByMonth(minDate, maxDate);
        return ResponseEntity.ok(salaryHistoryList);
    }


    @GetMapping("/salaryHistory/byUserId/{userId}")
    public HttpEntity<?> getSalaryHistoriesByUserId(@PathVariable UUID userId) {
        List<SalaryHistory> salaryHistoryList = salaryHistoryService.getAllByUserId(userId);
        return ResponseEntity.ok(salaryHistoryList);
    }
}
