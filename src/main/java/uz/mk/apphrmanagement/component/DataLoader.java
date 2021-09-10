package uz.mk.apphrmanagement.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.mk.apphrmanagement.entity.*;
import uz.mk.apphrmanagement.entity.enums.RoleName;
import uz.mk.apphrmanagement.entity.enums.TaskStatusName;
import uz.mk.apphrmanagement.entity.enums.TurniketStatusName;
import uz.mk.apphrmanagement.repository.RoleRepository;
import uz.mk.apphrmanagement.repository.TaskStatusRepository;
import uz.mk.apphrmanagement.repository.TurniketStatusRepository;
import uz.mk.apphrmanagement.repository.UserRepository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    TurniketStatusRepository turniketStatusRepository;
    @Value("${spring.sql.init.mode}")
    private String initialMode;
    @Override
    public void run(String... args) throws Exception {
        if (initialMode.equals("always")) {

            RoleName[] roleNames = RoleName.values();
            List<Role> roleList = Arrays.stream(roleNames).map(Role::new).collect(Collectors.toList());
            roleRepository.saveAll(roleList);

            TaskStatusName[] taskStatusNames = TaskStatusName.values();
            List<TaskStatus> taskStatusList = Arrays.stream(taskStatusNames).map(TaskStatus::new).collect(Collectors.toList());
            taskStatusRepository.saveAll(taskStatusList);

            TurniketStatusName[] turniketStatusNames = TurniketStatusName.values();
            List<TurniketStatus> turniketStatusList = Arrays.stream(turniketStatusNames).map(TurniketStatus::new).collect(Collectors.toList());
            turniketStatusRepository.saveAll(turniketStatusList);

            User user = new User(
                    "John",
                    "Doe",
                    "mirzohid.xasanov@mail.ru",
                    passwordEncoder.encode("123")
                    );
            Role director = roleRepository.findByRoleName(RoleName.ROLE_DIRECTOR);
            user.setRoles(Collections.singleton(director));
            user.setEnabled(true);
            userRepository.save(user);
        }
    }
}
