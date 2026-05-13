import { apiService } from './api';

export const userService = {
    getAll: async () => {
        const data = await apiService.getUsers();
        return data || [];
    },

    create: async (user) => {
        const payload = {
            nombre: user.nombre,
            email: user.email,
            rol: user.rol,
            telefono: user.telefono || '',
            estado: user.estado || 'Activo',
            extra: user.extra || '',
            password: user.contraseña,
        };
        const data = await apiService.createUser(payload);
        return { success: true, data };
    },

    update: async (user) => {
        const payload = {
            nombre: user.nombre,
            email: user.email,
            rol: user.rol,
            telefono: user.telefono || '',
            estado: user.estado || 'Activo',
            extra: user.extra || '',
        };
        if (user.contraseña) {
            payload.password = user.contraseña;
        }
        const data = await apiService.updateUser(user.id, payload);
        return { success: true, data };
    },

    delete: async (id) => {
        await apiService.deleteUser(id);
        return { success: true };
    },
};