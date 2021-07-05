package uz.mk.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.Role;
import uz.mk.apphrmanagement.entity.Turniket;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.WorkTimeHistory;
import uz.mk.apphrmanagement.entity.enums.RoleName;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.payload.TurniketDto;
import uz.mk.apphrmanagement.repository.TurniketRepository;
import uz.mk.apphrmanagement.repository.TurniketStatusRepository;
import uz.mk.apphrmanagement.repository.UserRepository;
import uz.mk.apphrmanagement.repository.WorkTimeHistoryRepository;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WorkTimeHistoryService {

    @Autowired
    WorkTimeHistoryRepository workTimeHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TurniketRepository turniketRepository;

    @Autowired
    TurniketStatusRepository turniketStatusRepository;

    public ApiResponse getAllByUserId(UUID userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();

        RoleName userRole = null;
        Set<Role> userRoles = userPrincipal.getRoles();
        for (Role role : userRoles) {
            userRole = role.getRoleName();
        }


        assert userRole != null;
        if (userRole.equals(RoleName.ROLE_STAFF) || userRole.equals(RoleName.ROLE_MANAGER)) {
            return new ApiResponse("You don not have empowerment to see staff's work-time histories", false);
        }

        List<WorkTimeHistory> workTimeHistories = workTimeHistoryRepository.findAllByUserId(userId);
        return new ApiResponse("Work-time histories", true, workTimeHistories);
    }

    public ApiResponse entry(TurniketDto turniketDto) {
        Optional<Turniket> optionalTurniket = turniketRepository.findById(turniketDto.getTurniketId());
        if (optionalTurniket.isEmpty()) {
            return new ApiResponse("Turniket not found", false);
        }
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        String entryTime = timeFormatter.format(date);
        String formatDate = dateFormatter.format(date);

        Turniket turniket = optionalTurniket.get();
        User user = turniket.getUser();
        boolean existsByDateAndUserId = workTimeHistoryRepository.existsByDateAndUserId(java.sql.Date.valueOf(formatDate), user.getId());

        if (existsByDateAndUserId) {
            return new ApiResponse("You have already entered", false);
        }

        turniket.setTurniketStatus(turniketStatusRepository.getById(turniketDto.getTurniketStatusId()));

        java.sql.Date workTimeDate = new java.sql.Date(date.getTime());
        WorkTimeHistory workTimeHistory = new WorkTimeHistory();
        workTimeHistory.setDate(workTimeDate);
        workTimeHistory.setEntryTime(entryTime);
        workTimeHistory.setUser(user);
        WorkTimeHistory savedWorkTime = workTimeHistoryRepository.save(workTimeHistory);


        return new ApiResponse("Staff entered", true, savedWorkTime);
    }

    public ApiResponse exit(TurniketDto turniketDto) {
        Optional<Turniket> optionalTurniket = turniketRepository.findById(turniketDto.getTurniketId());
        if (optionalTurniket.isEmpty()) {
            return new ApiResponse("Turniket not found", false);
        }
        Turniket turniket = optionalTurniket.get();
        turniket.setTurniketStatus(turniketStatusRepository.getById(turniketDto.getTurniketStatusId()));
        Date date = new Date();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
        String formatDate = dateFormatter.format(date);
        String departureTime = timeFormatter.format(date);

        User user = turniket.getUser();

        Optional<WorkTimeHistory> optionalWorkTimeHistory =
                workTimeHistoryRepository.findByDateAndUserId(java.sql.Date.valueOf(formatDate), user.getId());
        WorkTimeHistory workTimeHistory = optionalWorkTimeHistory.get();
        workTimeHistory.setDepartureTime(departureTime);
        WorkTimeHistory savedWorkTime = workTimeHistoryRepository.save(workTimeHistory);

        return new ApiResponse("Staff exited", true, savedWorkTime);
    }


}
