package com.example.qlybanhang.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {
    private int id;

    @NotEmpty(message = "{not.empty}")
    private String name;

    @NotEmpty(message = "{not.empty}")
    private String username;

    @NotEmpty(message = "{not.empty}")
    private String password;

    @JsonIgnore
    private MultipartFile file;

    private String avatar;

    private String email;

    private String birthdate;
}
