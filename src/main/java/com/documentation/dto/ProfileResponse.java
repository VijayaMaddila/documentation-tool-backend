package com.documentation.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProfileResponse {
    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
