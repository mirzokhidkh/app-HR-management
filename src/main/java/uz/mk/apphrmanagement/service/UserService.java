package uz.mk.apphrmanagement.service;

import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.Role;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.enums.RoleName;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.repository.UserRepository;

import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public ApiResponse getAllStaff() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();

        RoleName userRole = null;
        Set<Role> userRoles = userPrincipal.getRoles();
        for (Role role : userRoles) {
            userRole = role.getRoleName();
        }


        assert userRole != null;
        if (userRole.equals(RoleName.ROLE_STAFF) || userRole.equals(RoleName.ROLE_MANAGER)) {
            return new ApiResponse("You don not have empowerment to see staff information", false);
        }
        List<User> staffList = userRepository.findAllByRolesId(4);
        return new ApiResponse("Staff", true, staffList);
    }
}
