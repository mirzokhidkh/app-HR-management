package uz.mk.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.enums.RoleName;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.payload.LoginDto;
import uz.mk.apphrmanagement.payload.RegisterDto;
import uz.mk.apphrmanagement.repository.RoleRepository;
import uz.mk.apphrmanagement.repository.UserRepository;
import uz.mk.apphrmanagement.security.JwtProvider;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;


    public ApiResponse register(RegisterDto registerDto) {
        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) {
            return new ApiResponse("Email already exists", false);
        }
        User user = new User();
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByRoleName(RoleName.ROLE_STAFF)));
        user.setEmailCode(UUID.randomUUID().toString());
        userRepository.save(user);

        sendEmail(user.getEmail(), user.getEmailCode());

        return new ApiResponse("Successfully registered. Confirm your email account to active your account", true);
    }


    public ApiResponse verifyEmail(String emailCode, String email) {
        Optional<User> optionalUser = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new ApiResponse("Account confirmed", true);
        }

        return new ApiResponse("Account already confirmed", false);
    }

    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("example@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Confirm Acount");
            mailMessage.setText("<a href='http://localhost:8080/api/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'>Confirm</a>");
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getEmail(), user.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("Email or password incorrect", true);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }
        throw new UsernameNotFoundException(email + " not found");
    }
}
