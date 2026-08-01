package com.thinhbqt.enotes_api_service.dto;
import lombok.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserRequest {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String mobileNumber;
    private String password;
    private List<RoleDto> roles;

    @Getter
    @Setter
    public static class RoleDto {
        private Integer id;
        private String name;
    }
}