package uz.mk.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.Role;
import uz.mk.apphrmanagement.entity.Task;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.enums.RoleName;
import uz.mk.apphrmanagement.entity.enums.TaskStatusName;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.payload.TaskDto;
import uz.mk.apphrmanagement.repository.RoleRepository;
import uz.mk.apphrmanagement.repository.TaskRepository;
import uz.mk.apphrmanagement.repository.TaskStatusRepository;
import uz.mk.apphrmanagement.repository.UserRepository;
import uz.mk.apphrmanagement.utils.CommonUtils;

import java.util.*;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    MailService mailService;

    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    //ATTACH TASK TO MANAGER OR STAFF
    public ApiResponse add(TaskDto taskDto) {
        Optional<User> optionalUser = userRepository.findById(taskDto.getUserId());
        if (!optionalUser.isPresent()) {
            return new ApiResponse("User not found", false);
        }

        Map<String, Object> contextHolder = CommonUtils.getPrincipalAndRoleFromSecurityContextHolder();
//        User principalUser = (User) contextHolder.get("principalUser");
        RoleName principalUserRole = (RoleName) contextHolder.get("principalUserRole");


        RoleName userRole = null;
        User user = optionalUser.get();
        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            userRole = role.getRoleName();
        }

        assert principalUserRole != null;
        //DIRECTOR CAN ATTACH TASK TO  MANAGER OR STAFF,
        boolean isDirectorAuthority = principalUserRole.equals(RoleName.ROLE_DIRECTOR) &&
                (Objects.equals(userRole, RoleName.ROLE_HR_MANAGER) || Objects.equals(userRole, RoleName.ROLE_MANAGER) || Objects.equals(userRole, RoleName.ROLE_STAFF));

        //MANAGER ONLY CAN ATTACH TASK TO STAFF
        boolean isManagerAuthority = (principalUserRole.equals(RoleName.ROLE_HR_MANAGER) || principalUserRole.equals(RoleName.ROLE_MANAGER)) &&
                Objects.equals(userRole, RoleName.ROLE_STAFF);

        // IF CONDITION WILL BE TRUE , SO PRINCIPAL USER IS STAFF. THEREFORE HE/SHE CAN'T ATTACH TASK ANYONE
        if (!(isDirectorAuthority || isManagerAuthority)) {
            return new ApiResponse("Your position is " + principalUserRole + ". You do not have the authority to attach a user with such a role", false);
        }


        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setExpireDate(taskDto.getExpireDate());
        task.setTaskStatus(taskStatusRepository.findByName(TaskStatusName.NEW));
        task.setUser(optionalUser.get());
        Task savedTask = taskRepository.save(task);

        String emailById = user.getEmail();

        String subject = "A new task has been added to you";
        String text = "Name: " + savedTask.getName() + "\n" +
                "Description: " + savedTask.getDescription() + "\n" +
                "Expire date: " + savedTask.getExpireDate() + "\n" +
                "Task status: " + savedTask.getTaskStatus().getName() + "\n";

        mailService.sendEmail(emailById, subject, text);

        return new ApiResponse("The message was sent to the user's email to which the task was attached ", true, savedTask);

    }

    public ApiResponse sendReport(UUID taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Task not found", false);
        }

        Task task = optionalTask.get();
        task.setTaskStatus(taskStatusRepository.findByName(TaskStatusName.DONE));
        Task savedTask = taskRepository.save(task);

        String emailById = userRepository.getEmailById(task.getCreatedBy());

        if (emailById.isEmpty()) {
            return new ApiResponse("Director or manager not found", false);
        }
        String fullNameById = userRepository.getFullNameById(savedTask.getUser().getId());

        String subject = "Task completed";
        String text = "Name: " + savedTask.getName() + "\n" +
                "Description: " + savedTask.getDescription() + "\n" +
                "Expire date: " + savedTask.getExpireDate() + "\n" +
                "Task status: " + savedTask.getTaskStatus() + "\n" +
                "Full name : " + fullNameById;

        mailService.sendEmail(emailById, subject, text);

        return new ApiResponse("Sent report", true);
    }

    public ApiResponse getAllByUserId(UUID userId) {
        Map<String, Object> contextHolder = CommonUtils.getPrincipalAndRoleFromSecurityContextHolder();
//        User principalUser = (User) contextHolder.get("principalUser");
        RoleName principalUserRole = (RoleName) contextHolder.get("principalUserRole");


        assert principalUserRole != null;
        if (principalUserRole.equals(RoleName.ROLE_STAFF) || principalUserRole.equals(RoleName.ROLE_MANAGER)) {
            return new ApiResponse("You don not have empowerment to see staff's task information", false);
        }

        List<Task> taskList = taskRepository.findAllByUserId(userId);
        return new ApiResponse("Staff's tasks", true, taskList);
    }
}
