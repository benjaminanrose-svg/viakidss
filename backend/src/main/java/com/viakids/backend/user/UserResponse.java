package com.viakids.backend.user;

import lombok.Data;
import java.util.UUID;

@Data
public class UserResponse {
    private UUID id;
    private String nombre;
    private String email;
    private String rol;
    private String telefono;
    private String estado;

    public static UserResponse from(User user) {
        UserResponse r = new UserResponse();
        r.setId(user.getId());
        r.setNombre(user.getNombre());
        r.setEmail(user.getEmail());
        r.setRol(user.getRol().name());
        r.setTelefono(user.getTelefono());
        r.setEstado(user.getEstado().name());
        return r;
    }
}
