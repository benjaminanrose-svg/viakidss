import { apiService } from './api';

const ROLE_TO_BACKEND = { 'Administrador': 'ADMIN', 'Conductor': 'DRIVER', 'Apoderado': 'PARENT' };
const ROLE_FROM_BACKEND = { 'ADMIN': 'Administrador', 'DRIVER': 'Conductor', 'PARENT': 'Apoderado' };
const STATUS_TO_BACKEND = { 'Activo': 'ACTIVO', 'Suspendido': 'SUSPENDIDO' };
const STATUS_FROM_BACKEND = { 'ACTIVO': 'Activo', 'SUSPENDIDO': 'Suspendido' };

const normalizeUser = (u) => ({
    id: u.id,
    nombre: u.nombre,
    email: u.email,
    rol: ROLE_FROM_BACKEND[u.rol] ?? u.rol,
    telefono: u.telefono || '',
    estado: STATUS_FROM_BACKEND[u.estado] ?? u.estado,
    extra: u.extra || '',
});

export const userService = {
    getAll: async () => {
        const data = await apiService.getUsers();
        return (data || []).map(normalizeUser);
    },

    create: async (user) => {
        const payload = {
            nombre: user.nombre,
            email: user.email,
            password: user.contraseña,
            rol: ROLE_TO_BACKEND[user.rol] ?? user.rol,
            telefono: user.telefono || '',
            estado: STATUS_TO_BACKEND[user.estado] ?? user.estado,
        };
        try {
            const data = await apiService.createUser(payload);
            return { success: true, data: normalizeUser(data) };
        } catch (error) {
            const msg = error.response?.data?.message || 'Error al crear usuario';
            return { success: false, error: msg };
        }
    },

    update: async (user) => {
        const payload = {
            nombre: user.nombre,
            email: user.email,
            rol: ROLE_TO_BACKEND[user.rol] ?? user.rol,
            telefono: user.telefono || '',
            estado: STATUS_TO_BACKEND[user.estado] ?? user.estado,
        };
        if (user.contraseña) {
            payload.password = user.contraseña;
        }
        try {
            const data = await apiService.updateUser(user.id, payload);
            return { success: true, data: normalizeUser(data) };
        } catch (error) {
            const msg = error.response?.data?.message || 'Error al actualizar usuario';
            return { success: false, error: msg };
        }
    },

    delete: async (id) => {
        await apiService.deleteUser(id);
        return { success: true };
    },
};