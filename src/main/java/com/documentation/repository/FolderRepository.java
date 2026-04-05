package com.documentation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.documentation.entity.Folder;

public interface FolderRepository extends JpaRepository<Folder, Long> {

	List<Folder> findByUserId(Long userId);

	List<Folder> findByWorkspaceId(Long workspaceId);
}
