package com.viakids.backend.user;

import lombok.Data;

@Data
public class UserRequest {
    private String nombre;
    private String email;
    private String password;
    private String rol;
    private String telefono;
    private String estado;
}
