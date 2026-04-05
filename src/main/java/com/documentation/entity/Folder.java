package com.documentation.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name="folders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Folder {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private  Long id;
	@NotBlank
	private String name;
	private LocalDateTime createAt;
	
	@ManyToOne
	@JoinColumn(name="workspaceId")
	private Workspace workspace;

	@ManyToOne
	@JoinColumn(name="userId")
	private User user;

	@ManyToOne
	@JoinColumn(name="parentFolderId")
	private Folder folder;

	@OneToMany(mappedBy="folder",cascade=CascadeType.ALL)
	private List<Folder> folders;

	@OneToMany(mappedBy="folder",cascade=CascadeType.ALL)
	private List<Document> documents;
	
	

}
