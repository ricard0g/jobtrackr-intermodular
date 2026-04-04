package org.ricardo.jobtrackr.dto;

public record UserResponse(
        int usuarioId,
        String primerNombreUsuario,
        String segundoNombreUsuario,
        String primerApellidoUsuario,
        String segundoApellidoUsuario,
        String correoElectronicoUsuario
) {
}
