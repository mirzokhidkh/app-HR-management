package uz.mk.apphrmanagement.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class PasswordDto {
    private UUID userId;
    private String password;
}
