package uz.mk.apphrmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.mk.apphrmanagement.entity.Role;
import uz.mk.apphrmanagement.entity.Turniket;
import uz.mk.apphrmanagement.entity.User;
import uz.mk.apphrmanagement.entity.enums.RoleName;
import uz.mk.apphrmanagement.payload.ApiResponse;
import uz.mk.apphrmanagement.payload.LoginDto;
import uz.mk.apphrmanagement.payload.PasswordDto;
import uz.mk.apphrmanagement.payload.RegisterDto;
import uz.mk.apphrmanagement.repository.RoleRepository;
import uz.mk.apphrmanagement.repository.TurniketRepository;
import uz.mk.apphrmanagement.repository.UserRepository;
import uz.mk.apphrmanagement.security.JwtProvider;

import java.util.*;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    TurniketRepository turniketRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;


    public ApiResponse register(RegisterDto registerDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principalUser = (User) authentication.getPrincipal();

        RoleName principalUserRole = null;
        Set<Role> principalUserRoles = principalUser.getRoles();
        for (Role role : principalUserRoles) {
            principalUserRole = role.getRoleName();
        }

        Role role = roleRepository.getById(registerDto.getRoleId());
        RoleName userRole = role.getRoleName();

        assert principalUserRole != null
                ;
        //DIRECTOR CAN ADD MANAGER OR STAFF,
        boolean isDirectorAuthority = principalUserRole.equals(RoleName.ROLE_DIRECTOR) &&
                (userRole.equals(RoleName.ROLE_HR_MANAGER) || userRole.equals(RoleName.ROLE_MANAGER) || userRole.equals(RoleName.ROLE_STAFF));

        //HR MANAGER ONLY CAN ADD STAFF
        boolean isHRManagerAuthority = principalUserRole.equals(RoleName.ROLE_HR_MANAGER) && userRole.equals(RoleName.ROLE_STAFF);

        // IF CONDITION WILL BE TRUE , SO PRINCIPAL USER IS MANAGER OR STAFF. THEREFORE THEY CAN'T ADD ANYONE
        if (!(isDirectorAuthority || isHRManagerAuthority)) {
            return new ApiResponse("You do not have the authority to add a user with such a role", false);
        }


        boolean existsByEmail = userRepository.existsByEmail(registerDto.getEmail());
        if (existsByEmail) {
            return new ApiResponse("Email already exists", false);
        }

        User user = new User();
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());


        Set<Role> roleSet = new HashSet<Role>(Collections.singleton(role));
        user.setRoles(roleSet);

        user.setEmailCode(UUID.randomUUID().toString());

        User savedUser = userRepository.save(user);
        if (role.getRoleName().equals(RoleName.ROLE_STAFF)) {
            Turniket turniket = new Turniket();
            turniket.setUser(savedUser);
            turniketRepository.save(turniket);
        }

        String subject = "Confirm Account";

        String text = "your login: " + user.getEmail() + "\n" +
                "Confirm => http://localhost:8080/api/auth/verifyEmail?emailCode=" + user.getEmailCode() + "&email=" + user.getEmail();
        mailService.sendEmail(user.getEmail(), subject, text);

        return new ApiResponse("Successfully registered. A confirmation message has been sent to email to activate the account", true);
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

    public ApiResponse setPassword(PasswordDto passwordDto) {
        Optional<User> optionalUser = userRepository.findById(passwordDto.getUserId());
        if (!optionalUser.isPresent()) {
            return new ApiResponse("User not found", false);
        }

        User user = optionalUser.get();
        if (user.getPassword() != null) {
            return new ApiResponse("You have already set password", false);

        }
        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        userRepository.save(user);

        return new ApiResponse("Successfully set password. Go to login to enter to system", true);
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
