package com.hotel.RuSiri.Controller.User;

import com.hotel.RuSiri.DTO.User.*;
import com.hotel.RuSiri.Service.User.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    //  REGISTER USER
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegisterDTO dto) {

        UserResponseDTO user = userService.registerUser(dto); // ✅ FIX

        return ResponseEntity.ok(user);
    }

    //  LOGIN
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> loginUser(
            @RequestBody UserLoginDTO dto
    ){

        return ResponseEntity.ok(
                userService.loginUser(dto)
        );
    }

    //  UPDATE USER
    @PreAuthorize("hasAnyRole('USER','ADMIN')") // ✅ IMPORTANT
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                      @RequestBody UserRegisterDTO dto) {

        return ResponseEntity.ok(userService.updateUser(id, dto));
    }

    //  GET ALL USERS (ADMIN ONLY)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    //  DELETE USER (ADMIN ONLY)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Deleted");
    }

    // USER VIEW HIS DETAILS
    @GetMapping("/me")
    public ResponseEntity<MyProfileDTO> getMyProfile() {

        return ResponseEntity.ok(userService.getMyProfile());
    }

    // USER CHANGE PASSWORD
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto);
        return ResponseEntity.ok("Password updated successfully");
    }

    // ACTIVATE USERS
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok("User activated");
    }

}