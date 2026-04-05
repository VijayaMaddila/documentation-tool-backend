package com.documentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.documentation.dto.DocumentRequest;
import com.documentation.dto.DocumentResponse;
import com.documentation.service.DocumentService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

	@Autowired
	private DocumentService documentService;

	@PostMapping
	public DocumentResponse createDocument(@RequestBody DocumentRequest dto) {
		return documentService.createDocument(dto);
	}

	@GetMapping("/{id}")
	public DocumentResponse getDocumentById(@PathVariable Long id) {
		return documentService.getDocumentById(id);
	}

	@GetMapping("/folder/{folderId}")
	public List<DocumentResponse> getDocumentsByFolderId(@PathVariable Long folderId) {
		return documentService.getDocumentsByFolderId(folderId);
	}

	@GetMapping("/user/{userId}")
	public List<DocumentResponse> getDocumentsByUserId(@PathVariable Long userId) {
		return documentService.getDocumentsByUserId(userId);
	}

	@PutMapping("/{id}")
	public DocumentResponse updateDocument(@PathVariable Long id, @RequestBody DocumentRequest dto) {
		return documentService.updateDocument(id, dto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
		documentService.deleteDocument(id);
		return ResponseEntity.noContent().build();
	}
}
