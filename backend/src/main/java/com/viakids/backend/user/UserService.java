package com.viakids.backend.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public User create(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }

        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRol(parseRole(request.getRol()));
        user.setTelefono(request.getTelefono());
        user.setEstado(parseStatus(request.getEstado()));
        return userRepository.save(user);
    }

    public User update(UUID id, UserRequest request) {
        User user = getById(id);
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setRol(parseRole(request.getRol()));
        user.setTelefono(request.getTelefono());
        user.setEstado(parseStatus(request.getEstado()));
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return userRepository.save(user);
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    private User.Role parseRole(String rol) {
        if (rol == null) return User.Role.PARENT;
        return switch (rol.toUpperCase()) {
            case "ADMIN" -> User.Role.ADMIN;
            case "DRIVER" -> User.Role.DRIVER;
            default -> User.Role.PARENT;
        };
    }

    private User.UserStatus parseStatus(String estado) {
        if (estado == null) return User.UserStatus.ACTIVO;
        return "SUSPENDIDO".equalsIgnoreCase(estado) ? User.UserStatus.SUSPENDIDO : User.UserStatus.ACTIVO;
    }
}
