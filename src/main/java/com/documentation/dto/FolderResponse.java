package com.documentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FolderResponse {

	private Long Id;
	@NotBlank
	private String name;
	private Long workspaceId;
	private Long userId;
}
