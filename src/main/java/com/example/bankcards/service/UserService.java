package com.example.bankcards.service;

import com.example.bankcards.dto.*;
import com.example.bankcards.entity.User;
import com.example.bankcards.exception.*;
import com.example.bankcards.repository.UserRepository;
import com.example.bankcards.security.JwtUtils;
import com.example.bankcards.security.UserDetailsImpl;
import com.example.bankcards.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;


    public UserResponse addUser(UserRequest userRequest) {
        if(userRepository.existByUsername(userRequest.username()))
            throw new AlreadyExistException("User with username '" + userRequest.username() + "' already exist.", this.getClass().getName() + ".addUser()");
        else {
            if (userRepository.existByEmail(userRequest.email()))
                throw new AlreadyExistException("User with email '" + userRequest.email() + "' already exist.", this.getClass().getName() + ".addUser()");
            else {
                User user = new User();
                user.setUsername(userRequest.username());
                user.setPassword(passwordEncoder.encode(userRequest.password()));
                user.setEmail(userRequest.email());
                user.setFirstName(userRequest.firstname());
                user.setLastName(userRequest.lastname());
                Set<Role> authorities = new HashSet<>();
                authorities.add(Role.USER);
                user.setRoles(authorities);
                user.setAddedOn(LocalDateTime.now());
                return UserMapper.userToDto(userRepository.save(user));
            }
        }
    }




    public AuthResponse authenticateUser(AuthRequest authRequest) {
        if(!userRepository.existByUsername(authRequest.getUsername()))
            throw new SomethingNotFoundException("User with username '" + authRequest.getUsername() + "' not found.", this.getClass().getName() + ".authenticateUser()");
        else {
            try {
                Authentication authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                authRequest.getUsername(),
                                authRequest.getPassword()));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                String token = jwtUtils.generateJwtToken(authentication);

                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());

                return new AuthResponse(
                        token,
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles);
            } catch (Exception e) {
                throw new AuthException(e.getMessage() + " Password does not match.", this.getClass().getName() + ".signin()");
            }
        }
    }




    public UserResponse getUserByUsername(String username) {
        return UserMapper.userToDto(userRepository.findByUsername(username).orElseThrow(() -> new SomethingNotFoundException("User with username \"" + username + "\" not found. ", this.getClass().getName() + ".getUserByUsername()")));
    }




    public UserResponse getUserByEmail(String email) {
        return UserMapper.userToDto(userRepository.findByEmail(email).orElseThrow(() -> new SomethingNotFoundException("User with email \"" + email + "\" not found. ", this.getClass().getName() + ".getUserByEmail()")));
    }




    public UserResponse getUserById(Long id) {
        return UserMapper.userToDto(userRepository.findById(id).orElseThrow(() -> new SomethingNotFoundException("User with ID \"" + id + "\" not found. ", this.getClass().getName() + ".getUserById()")));
    }




    public List<UserResponse> getAllUsers() {
        List<UserResponse> users = new ArrayList<>();
            users = userRepository.getAllUsers().stream().map(user -> UserMapper.userToDto(user)).collect(Collectors.toList());
            if (users.isEmpty()) throw new SomethingNotFoundException("User database is empty. ", this.getClass().getName() + ".getAllUsers()");
        return users;
    }



    @Transactional
    public UserResponse addRoleById(Long id, String role) {
        if(!Role.isValidRole(role)) throw new RoleValidationException("Role \"" + role + "\" not found. Use roles: " + Arrays.toString(Role.values()), this.getClass().getName() + ".addRoleById()");
        User user = userRepository.findById(id).orElseThrow(() -> new SomethingNotFoundException("User with ID \"" + id + "\" not found. ", this.getClass().getName() + ".addRoleById()"));
        Set<Role> roles = user.getRoles();
        roles.add(Role.valueOf(role));
        user.setRoles(roles);
        return UserMapper.userToDto(userRepository.save(user));
    }


    @Transactional
    public UserResponse removeRoleById(Long id, String role) {
        if(!Role.isValidRole(role)) throw new RoleValidationException("Role \"" + role + "\" not found. Use roles: " + Arrays.toString(Role.values()), this.getClass().getName() + ".removeRoleById()");
        User user = userRepository.findById(id).orElseThrow(() -> new SomethingNotFoundException("User with ID \"" + id + "\" not found. ", this.getClass().getName() + ".addRoleById()"));

        Set<Role> roles = user.getRoles();
        if(roles.contains(Role.valueOf(role))) roles.remove(Role.valueOf(role));
        else throw new SomethingNotFoundException("User with ID \"" + id + "\" not have a role \"" + role + "\".", this.getClass().getName() + ".addRoleById()");

        user.setRoles(roles);
        return UserMapper.userToDto(userRepository.save(user));
    }



    public UserResponse deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new SomethingNotFoundException("User with id \"" + id + "\" not found. ", this.getClass().getName() + ".deleteUserById()"));
        if (!user.getCards().isEmpty()) throw new DeleteUserException("Before deleting a user, must be deleted all his cards", this.getClass().getName() + ".addRoleById()");
        else userRepository.deleteById(id);
        return UserMapper.userToDto(user);
    }
}
