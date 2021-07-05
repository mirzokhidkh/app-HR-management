package uz.mk.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import uz.mk.apphrmanagement.entity.SalaryHistory;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.service.SalaryHistoryService;
import uz.mk.apphrmanagement.service.TaskService;
import uz.mk.apphrmanagement.service.UserService;
import uz.mk.apphrmanagement.service.WorkTimeHistoryService;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@RestController("/management")
public class ManagementController {
    @Autowired
    UserService userService;

    @Autowired
    TaskService taskService;

    @Autowired
    WorkTimeHistoryService workTimeHistoryService;

    @Autowired
    SalaryHistoryService salaryHistoryService;

    @GetMapping("/staff")
    public HttpEntity<?> getAllStaff() {
        ApiResponse response = userService.getAllStaff();
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @GetMapping("/workTimeHistory/{userId}")
    public HttpEntity<?> getWorkTimeHistoriesByUserId(@PathVariable UUID userId) {
        ApiResponse response = workTimeHistoryService.getAllByUserId(userId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


    @GetMapping("/task/{userId}")
    public HttpEntity<?> getTasksByUserId(@PathVariable UUID userId) {
        ApiResponse response = taskService.getAllByUserId(userId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


    @GetMapping("/salaryHistory/byMonth")
    public HttpEntity<?> getSalaryHistoriesByMonth(@RequestParam Date minDate, @RequestParam Date maxDate) {
        ApiResponse response = salaryHistoryService.getAllByMonth(minDate, maxDate);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }


    @GetMapping("/salaryHistory/byUserId/{userId}")
    public HttpEntity<?> getSalaryHistoriesByUserId(@PathVariable UUID userId) {
        ApiResponse response = salaryHistoryService.getAllByUserId(userId);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }
}
