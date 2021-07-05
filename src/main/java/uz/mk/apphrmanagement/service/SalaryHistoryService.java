package uz.mk.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.Role;
import uz.mk.apphrmanagement.entity.SalaryHistory;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.enums.RoleName;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.repository.SalaryHistoryRepository;

import java.sql.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class SalaryHistoryService {

    @Autowired
    SalaryHistoryRepository salaryHistoryRepository;


    public ApiResponse getAllByMonth(Date minDate, Date maxDate) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();

        RoleName userRole = null;
        Set<Role> userRoles = userPrincipal.getRoles();
        for (Role role : userRoles) {
            userRole = role.getRoleName();
        }

        assert userRole != null;
        if (userRole.equals(RoleName.ROLE_STAFF) || userRole.equals(RoleName.ROLE_MANAGER)) {
            return new ApiResponse("You don not have empowerment to see staff's salary history ", false);
        }

        List<SalaryHistory> salaryHistoryList = salaryHistoryRepository.findAllByDateBetween(minDate, maxDate);
        return new ApiResponse("Salary histories", true, salaryHistoryList);
    }

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
            return new ApiResponse("You don not have empowerment to see staff's salary history ", false);
        }

        List<SalaryHistory> salaryHistoryList = salaryHistoryRepository.findAllByUserId(userId);
        return new ApiResponse("Salary histories", true, salaryHistoryList);
    }

}
