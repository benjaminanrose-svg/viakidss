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

    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            throw new RuntimeException("La contraseña es obligatoria");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(UUID id, User updates) {
        User user = getById(id);
        user.setNombre(updates.getNombre());
        user.setEmail(updates.getEmail());
        user.setRol(updates.getRol());
        user.setTelefono(updates.getTelefono());
        user.setEstado(updates.getEstado());
        if (updates.getPassword() != null && !updates.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updates.getPassword()));
        }
        return userRepository.save(user);
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }
}
