package net.atos.zepe.auth.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordDto {

    @NotBlank
    @Email
    private String email;
}
