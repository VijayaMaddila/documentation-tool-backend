package com.documentation.dto;

import lombok.Data;

@Data
public class DocumentResponse {
	
	private Long id;
	private String title;
	private String content;
	private Long folderId;
	private Long userId;

}
