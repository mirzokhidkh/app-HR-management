package uz.mk.apphrmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.mk.apphrmanagement.entity.WorkTimeHistory;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.payload.TurniketDto;
import uz.mk.apphrmanagement.service.WorkTimeHistoryService;

import java.util.List;
import java.util.UUID;

@RepositoryRestController
@RequiredArgsConstructor
public class WorkTimeHistoryController {

    private final WorkTimeHistoryService workTimeHistoryService;

//    @GetMapping("/{userId}")
//    public HttpEntity<?> getAllByUserId(@PathVariable UUID userId) {
//        List<WorkTimeHistory> workTimeHistories = workTimeHistoryService.getAllByUserId(userId);
//        return ResponseEntity.ok(workTimeHistories);
//    }


    @PostMapping("/workTimeHistory/entry")
    public HttpEntity<?> entry(@RequestBody TurniketDto turniketDto) {
        ApiResponse response = workTimeHistoryService.entry(turniketDto);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    @PostMapping("/workTimeHistory/exit")
    public HttpEntity<?> exit(@RequestBody TurniketDto turniketDto) {
        ApiResponse response = workTimeHistoryService.exit(turniketDto);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }
}

