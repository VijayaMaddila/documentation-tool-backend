package com.documentation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.documentation.entity.Workspace;

public interface WorkspaceRepository extends JpaRepository<Workspace,Long> {

	List<Workspace> findByOwnerId(Long userId);

}
