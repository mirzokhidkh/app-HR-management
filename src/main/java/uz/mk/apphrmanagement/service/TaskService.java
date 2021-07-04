package uz.mk.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.Task;
import uz.mk.apphrmanagement.entity.TaskStatus;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.enums.TaskStatusName;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.payload.TaskDto;
import uz.mk.apphrmanagement.repository.RoleRepository;
import uz.mk.apphrmanagement.repository.TaskRepository;
import uz.mk.apphrmanagement.repository.TaskStatusRepository;
import uz.mk.apphrmanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public List<Task> getAllByUserId(UUID userId) {
        List<Task> taskList = taskRepository.findAllByUserId(userId);
        return taskList;
    }
}
