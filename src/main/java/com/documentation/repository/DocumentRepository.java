package com.documentation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.documentation.entity.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

	List<Document> findByFolderId(Long folderId);

	List<Document> findByUserId(Long userId);
}
