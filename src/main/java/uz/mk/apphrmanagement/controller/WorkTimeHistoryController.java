package uz.mk.apphrmanagement.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.payload.TurniketDto;
import uz.mk.apphrmanagement.service.WorkTimeHistoryService;

@RepositoryRestController
@RequiredArgsConstructor
public class WorkTimeHistoryController {

    private final WorkTimeHistoryService workTimeHistoryService;

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

