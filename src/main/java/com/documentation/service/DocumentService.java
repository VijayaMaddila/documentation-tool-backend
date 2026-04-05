package com.documentation.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.documentation.dto.DocumentRequest;
import com.documentation.dto.DocumentResponse;
import com.documentation.entity.Document;
import com.documentation.entity.Folder;
import com.documentation.entity.User;
import com.documentation.repository.DocumentRepository;
import com.documentation.repository.FolderRepository;
import com.documentation.repository.UserRepository;

@Service
public class DocumentService {

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FolderRepository folderRepository;

	public DocumentResponse createDocument(DocumentRequest dto) {
		User user = userRepository.findById(dto.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));

		Folder folder = folderRepository.findById(dto.getFolderId())
				.orElseThrow(() -> new RuntimeException("Folder not found"));

		Document request = new Document();
		request.setTitle(dto.getTitle());
		request.setContent(dto.getContent());
		request.setUser(user);
		request.setFolder(folder);

		Document saved = documentRepository.save(request);
		return mapToResponse(saved);
	}

	public DocumentResponse getDocumentById(Long id) {
		Document document = documentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Document not found"));
		return mapToResponse(document);
	}

	public List<DocumentResponse> getDocumentsByFolderId(Long folderId) {
		return documentRepository.findByFolderId(folderId)
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	public List<DocumentResponse> getDocumentsByUserId(Long userId) {
		return documentRepository.findByUserId(userId)
				.stream()
				.map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	public DocumentResponse updateDocument(Long id, DocumentRequest dto) {
		Document existing = documentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Document not found"));

		existing.setTitle(dto.getTitle());
		existing.setContent(dto.getContent());

		Document saved = documentRepository.save(existing);
		return mapToResponse(saved);
	}

	public void deleteDocument(Long id) {
		documentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Document not found"));
		documentRepository.deleteById(id);
	}

	private DocumentResponse mapToResponse(Document document) {
		DocumentResponse response = new DocumentResponse();
		response.setId(document.getId());
		response.setTitle(document.getTitle());
		response.setContent(document.getContent());
		response.setUserId(document.getUser().getId());
		response.setFolderId(document.getFolder().getId());
		return response;
	}
}
