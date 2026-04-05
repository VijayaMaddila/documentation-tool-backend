package com.documentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DocumentRequest {
	
	@NotBlank
	private String title;
	@NotBlank
	private String content;
	private Long folderId;
	private Long userId;
	

}
