package uz.mk.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.Role;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.enums.RoleName;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.repository.UserRepository;
import uz.mk.apphrmanagement.utils.CommonUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;


    public ApiResponse getAllStaff() {
        Map<String, Object> contextHolder = CommonUtils.getPrincipalAndRoleFromSecurityContextHolder();
//        User principalUser = (User) contextHolder.get("principalUser");
        RoleName principalUserRole = (RoleName) contextHolder.get("principalUserRole");

        assert principalUserRole != null;
        if (principalUserRole.equals(RoleName.ROLE_STAFF) || principalUserRole.equals(RoleName.ROLE_MANAGER)) {
            return new ApiResponse("You don not have empowerment to see staff information", false);
        }
        List<User> staffList = userRepository.findAllByRolesId(4);
        return new ApiResponse("Staff", true, staffList);
    }
}
