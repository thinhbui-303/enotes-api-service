package com.thinhbqt.enotes_api_service.endpoint;

import static com.thinhbqt.enotes_api_service.util.Constants.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thinhbqt.enotes_api_service.dto.PasswordChangeRequest;
import com.thinhbqt.enotes_api_service.util.ApiCommonResponses;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User", description = "Authentication User Operation APIs")
@ApiCommonResponses
@RequestMapping("/api/v1/user")
public interface UserEndpoint {
    @Operation(summary = "Get User Profile", tags = { "User" }, description = "Get User Profile")
    @PreAuthorize(ADMIN_AND_USER)
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile();

    @Operation(summary = "User Account Password Change", tags = {
            "User" }, description = "User Account Password Change")
    @PreAuthorize(ADMIN_AND_USER)
    @PostMapping("/change-pwd")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request);
}
