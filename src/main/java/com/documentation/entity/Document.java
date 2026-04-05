package com.documentation.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String title;

	@NotBlank
	@Column(columnDefinition = "TEXT")
	private String content;
	
	@ManyToOne
	@JoinColumn(name="folderId")
	private Folder folder;

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;
	
	
	private LocalDateTime createdAt;
	

}
