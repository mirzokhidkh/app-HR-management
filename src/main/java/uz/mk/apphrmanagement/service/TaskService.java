package uz.mk.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.Role;
import uz.mk.apphrmanagement.entity.Task;
import uz.mk.apphrmanagement.entity.TaskStatus;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.enums.RoleName;
import uz.mk.apphrmanagement.entity.enums.TaskStatusName;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.payload.TaskDto;
import uz.mk.apphrmanagement.repository.RoleRepository;
import uz.mk.apphrmanagement.repository.TaskRepository;
import uz.mk.apphrmanagement.repository.TaskStatusRepository;
import uz.mk.apphrmanagement.repository.UserRepository;

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
    public ApiResponse add(Integer roleId,
                           TaskDto taskDto) {
        Optional<User> optionalUser = userRepository.findByIdAndRolesInByNative(taskDto.getUserId(), roleId);
        if (optionalUser.isEmpty()) {
            return new ApiResponse("User not found", false);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();

        RoleName userPrincipalRole = null;
        Set<Role> userPrincipalRoles = userPrincipal.getRoles();
        for (Role role : userPrincipalRoles) {
            userPrincipalRole = role.getRoleName();
        }

        User user = optionalUser.get();
        Set<Role> roles = user.getRoles();
        RoleName userRole = null;
        for (Role role : roles) {
            userRole = role.getRoleName();
        }

        assert userPrincipalRole != null;
        if (userPrincipalRole.equals(RoleName.ROLE_STAFF) ||
                !( (userPrincipalRole.equals(RoleName.ROLE_MANAGER) || userPrincipalRole.equals(RoleName.ROLE_HR_MANAGER)) && Objects.equals(userRole, RoleName.ROLE_STAFF))) {
            return new ApiResponse("You don not have empowerment to add task", false);
        }


        Task task = new Task();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setExpireDate(taskDto.getExpireDate());

        Optional<TaskStatus> optionalTaskStatus = taskStatusRepository.findById(taskDto.getTaskStatusId());
        task.setTaskStatus(optionalTaskStatus.get());
        task.setUser(optionalUser.get());
        Task savedTask = taskRepository.save(task);

        String emailById = userRepository.getEmailById(taskDto.getUserId());

        String subject = "A new task has been added to you";
        String text = "Name: " + savedTask.getName() + "\n" +
                "Description: " + savedTask.getDescription() + "\n" +
                "Expire date: " + savedTask.getExpireDate() + "\n" +
                "Task status: " + savedTask.getTaskStatus() + "\n";

        mailService.sendEmail(emailById, subject, text);

        return new ApiResponse("The message was sent to the user's email to which the task was attached ", true, savedTask);

    }

    public ApiResponse sendReport(UUID taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if (optionalTask.isEmpty()) {
            return new ApiResponse("Task not found", false);
        }

        Task task = optionalTask.get();
        task.setTaskStatus(taskStatusRepository.findByName(TaskStatusName.DONE));
        Task savedTask = taskRepository.save(task);

        String emailById = userRepository.getEmailById(task.getCreatedBy());

        if (emailById.isBlank()) {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userPrincipal = (User) authentication.getPrincipal();

        RoleName userRole = null;
        Set<Role> userRoles = userPrincipal.getRoles();
        for (Role role : userRoles) {
            userRole = role.getRoleName();
        }


        assert userRole != null;
        if (userRole.equals(RoleName.ROLE_STAFF) || userRole.equals(RoleName.ROLE_MANAGER)) {
            return new ApiResponse("You don not have empowerment to see staff's task information", false);
        }

        List<Task> taskList = taskRepository.findAllByUserId(userId);
        return new ApiResponse("Staff's tasks", true,taskList);
    }
}
