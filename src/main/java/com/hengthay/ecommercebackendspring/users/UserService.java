package com.hengthay.ecommercebackendspring.users;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Get all users from DB
    public List<UserDto> getAllUsers() {
        var users = userRepository
                .findAll()
                .stream()
                .map(userMapper::toDto)
                .toList();

        if(users.isEmpty())
            throw new UserNotFoundException("No user record.");

        return users;
    }

    public List<UserDto> getOnlyActiveUsers() {
        var users = userRepository
                .findByActiveTrue()
                .stream()
                .map(userMapper::toDto)
                .toList();

        if(users.isEmpty())
            throw new UserNotFoundException("No user record.");

        return users;
    }

    public UserDto getUserById(Long id) {

        var user = userRepository.findById(id).orElse(null);

        if(user == null)
            throw new UserNotFoundException("User not found.");

        return userMapper.toDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request) {
        System.out.println("Incoming as DTO: " + request);
        var userEntity = userMapper.toEntity(request);
        System.out.println("Converted to Entity: " + userEntity);

        // Hashing Password
         userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        var user = userRepository.save(userEntity);

        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElse(null);

        if(user == null)
            throw new UserNotFoundException("User not found.");

        userMapper.update(request, user);

        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto updateUserRole(Long id, AdminUpdateUserRoleRequest request) {
        var user = userRepository.findById(id).orElse(null);

        if(user == null)
            throw new UserNotFoundException("User not found.");

        userMapper.updateRole(request, user);

        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public void deleteUser(Long id) {
        var user = userRepository.findById(id).orElse(null);

        if(user == null)
            throw new UserNotFoundException("User not found.");

        user.setActive(false);
        userRepository.save(user);
    }

    public void changePassword(Long id, ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElseThrow();

        if(!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new UserAccessDeniedException("Password does not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    public void activateUser(Long id) {
        var user = userRepository.findById(id).orElseThrow();
        user.setActive(true);
        userRepository.save(user);
    }
}
