package uz.mk.apphrmanagement.utils;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import uz.mk.apphrmanagement.entity.Role;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.enums.RoleName;

import javax.swing.plaf.PanelUI;
import java.util.*;

public class CommonUtils {

    public static Map<String, Object> getPrincipalAndRoleFromSecurityContextHolder() {
        Map<String, Object> map = new HashMap<String, Object>();

        User principalUser = null;
        RoleName principalUserRole = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        principalUser = (User) authentication.getPrincipal();

        Set<Role> userPrincipalRoles = principalUser.getRoles();
        for (Role role : userPrincipalRoles) {
            principalUserRole = role.getRoleName();
        }
        map.put("principalUser", principalUser);
        map.put("principalUserRole", principalUserRole);
        return map;
    }


    public static Integer generateCode() {
        return new Random().nextInt((999999 - 100000) + 1) + 100000;
    }
}
