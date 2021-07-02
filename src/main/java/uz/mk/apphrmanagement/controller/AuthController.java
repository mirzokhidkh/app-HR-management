package uz.mk.apphrmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.payload.LoginDto;
import uz.mk.apphrmanagement.payload.RegisterDto;
import uz.mk.apphrmanagement.service.AuthService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody RegisterDto registerDto) {
        ApiResponse response = authService.register(registerDto);
        return ResponseEntity.status(response.isSuccess() ? 201 : 409).body(response);
    }

    @GetMapping("/verifyEmail")
    public HttpEntity<?> verifyEmail(@RequestParam String emailCode, @RequestParam String email) {
        ApiResponse response = authService.verifyEmail(emailCode, email);
        return ResponseEntity.status(response.isSuccess() ? 200 : 409).body(response);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto loginDto) {
        ApiResponse response = authService.login(loginDto);
        return ResponseEntity.status(response.isSuccess() ? 200 : 401).body(response);
    }

}
