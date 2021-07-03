package uz.mk.apphrmanagement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.mk.apphrmanagement.entity.Role;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.enums.RoleName;
import uz.mk.apphrmanagement.repository.RoleRepository;
import uz.mk.apphrmanagement.repository.UserRepository;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Value("${spring.sql.init.mode}")
    private String initialMode;
    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {
            User user = new User(
                    "John",
                    "Doe",
                    "mirzohid.xasanov@mail.ru",
                    passwordEncoder.encode("113")
                    );
            Role director = roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR);
            user.setRoles(Collections.singleton(director));
            user.setEnabled(true);
            userRepository.save(user);
        }
    }
}
