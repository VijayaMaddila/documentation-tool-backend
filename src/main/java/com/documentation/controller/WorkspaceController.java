package com.documentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.documentation.dto.WorkspaceDtoRequest;
import com.documentation.dto.WorkspcaeDtoResponse;
import com.documentation.service.WorkspaceService;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {
	
	@Autowired
	private WorkspaceService workspaceService;
	
	@PostMapping
	public WorkspcaeDtoResponse createWorkSpace(@RequestBody WorkspaceDtoRequest dto)
	{
		return workspaceService.createWorkSpace(dto);
	}
	@GetMapping("/user/{userId}")
	public  List<WorkspcaeDtoResponse> getUserWorkSpaces(@PathVariable Long userId)
	{
		return workspaceService.getUserWorkSpaces(userId);
	}
	@GetMapping("/{workspaceId}")
	public WorkspcaeDtoResponse getWorkspaceById(@PathVariable Long workspaceId)
	{
		return workspaceService.getWorkspaceById(workspaceId);
	}
	@PutMapping("/{id}")
	public WorkspcaeDtoResponse updateWorkspace(@PathVariable Long id, @RequestBody WorkspaceDtoRequest dto) {
		return workspaceService.updateWorkspace(id, dto);
	}

	@DeleteMapping("/{id}")
	public String deleteWorkspace(@PathVariable Long id) {
	    workspaceService.deleteWorkspace(id);
	    return "Workspace deleted successfully";
	}

}
