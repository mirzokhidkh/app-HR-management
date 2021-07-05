package uz.mk.apphrmanagement.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.service.UserService;

import java.util.List;

@RepositoryRestController
@RequiredArgsConstructor
public class UserController {

    @Autowired
    UserService userService;

}
