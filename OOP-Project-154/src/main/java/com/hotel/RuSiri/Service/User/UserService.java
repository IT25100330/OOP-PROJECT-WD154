package com.hotel.RuSiri.Service.User;

import com.hotel.RuSiri.DTO.User.*;
import com.hotel.RuSiri.Entity.Notification.NotificationType;
import com.hotel.RuSiri.Entity.User.Role;
import com.hotel.RuSiri.Entity.User.User;
import com.hotel.RuSiri.Repository.User.UserRepository;
import com.hotel.RuSiri.Security.JwtUtil;
import com.hotel.RuSiri.Service.Notification.NotificationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final NotificationService notificationService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       NotificationService notificationService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.notificationService = notificationService;
    }

    // ================= REGISTER =================
    public UserResponseDTO registerUser(UserRegisterDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByNic(dto.getNic())) {
            throw new RuntimeException("NIC already exists");
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phoneNumber(dto.getPhoneNumber())
                .nic(dto.getNic())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);

        user.setActive(true);

        notificationService.createNotification(saved,
                "Welcome to RuSiri Hotel!",
                NotificationType.REGISTER);

        return UserResponseDTO.builder()
                .id(saved.getId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .firstName(saved.getFirstName())
                .lastName(saved.getLastName())
                .nic(saved.getNic())
                .phoneNumber(saved.getPhoneNumber())
                .build();
    }

    // ================= LOGIN =================
    public AuthResponseDTO loginUser(UserLoginDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔥 ACTIVE CHECK
        if (!user.isActive()) {
            throw new RuntimeException("User account is deactivated");
        }

        // 🔐 PASSWORD CHECK
        if (!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException("Invalid password");
        }

        // 🔔 NOTIFICATION
        notificationService.createNotification(
                user,
                "Login successful",
                NotificationType.LOGIN
        );

        // 🔥 TOKEN
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        // 🔥 RETURN TOKEN + ROLE
        return new AuthResponseDTO(
                token,
                user.getRole().name()
        );
    }

    // ================= GET ALL USERS =================
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findByActiveTrue()
                .stream()
                .map(user -> UserResponseDTO.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phoneNumber(user.getPhoneNumber())
                        .nic(user.getNic())
                        .build())
                .toList();
    }

    // ================= UPDATE USER =================
    public UserResponseDTO updateUser(Long id, UserRegisterDTO dto) {

        String loggedEmail = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        String role = SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .iterator().next().getAuthority();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!role.equals("ROLE_ADMIN") && !user.getEmail().equals(loggedEmail)) {
            throw new RuntimeException("You can update only your own profile");
        }

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setNic(dto.getNic());

        User updated = userRepository.save(user);

        return UserResponseDTO.builder()
                .id(updated.getId())
                .username(updated.getUsername())
                .email(updated.getEmail())
                .firstName(updated.getFirstName())
                .lastName(updated.getLastName())
                .nic(updated.getNic())
                .phoneNumber(updated.getPhoneNumber())
                .build();
    }

    // ================= DELETE USER =================
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(false); // 🔥 soft delete

        userRepository.save(user);
    }

    // ================= USER CAN VIEW HIS PROFILE =================
    public MyProfileDTO getMyProfile() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return MyProfileDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber()) // ✅
                .nic(user.getNic())                 // ✅
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public void changePassword(ChangePasswordDTO dto) {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 🔐 check current password
        if (!passwordEncoder.matches(dto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        // 🔐 set new password
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));

        userRepository.save(user);
    }

    // ================= ACTIVATE USRES =================
    public void activateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setActive(true);

        userRepository.save(user);
    }







}

