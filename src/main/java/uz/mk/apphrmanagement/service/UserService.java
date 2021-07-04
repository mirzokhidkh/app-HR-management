package uz.mk.apphrmanagement.service;

import jdk.dynalink.linker.LinkerServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> getAllByRoleId(Integer roleId) {
        List<User> users = userRepository.findAllByRolesId(roleId);
        return users;
    }
}
