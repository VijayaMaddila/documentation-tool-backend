package com.documentation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.documentation.dto.WorkspaceDtoRequest;
import com.documentation.dto.WorkspcaeDtoResponse;
import com.documentation.entity.User;
import com.documentation.entity.Workspace;
import com.documentation.repository.UserRepository;
import com.documentation.repository.WorkspaceRepository;

@Service
public class WorkspaceService {
	
	@Autowired
	private WorkspaceRepository workspaceRepository;
	
	@Autowired
	private UserRepository userRepository;

	public WorkspcaeDtoResponse createWorkSpace(WorkspaceDtoRequest dto) {
		User user=userRepository.findById(dto.getOwnerId())
		.orElseThrow(() -> new RuntimeException("User not found"));

		Workspace workspace=new Workspace();
		workspace.setName(dto.getName());
		workspace.setOwner(user);

		Workspace saved=workspaceRepository.save(workspace);

		WorkspcaeDtoResponse response=new WorkspcaeDtoResponse();
		response.setId(saved.getId());
		response.setName(saved.getName());
		response.setOwnerId(user.getId());

		return response;
	}

	public List<WorkspcaeDtoResponse> getUserWorkSpaces(Long userId) {
		
		List<Workspace> workspaces=workspaceRepository.findByOwnerId(userId);
		
		return workspaces.stream().map(workspace->{
			WorkspcaeDtoResponse response=new WorkspcaeDtoResponse();
			response.setId(workspace.getId());
	        response.setName(workspace.getName());
	        response.setOwnerId(workspace.getOwner().getId());
	        return response;
	    }).toList();
	
	}

	public WorkspcaeDtoResponse getWorkspaceById(Long workspaceId) {
		Workspace workspace=workspaceRepository.findById(workspaceId)
				.orElseThrow(()->new  RuntimeException("Workspace not found"));
		WorkspcaeDtoResponse response=new WorkspcaeDtoResponse();
		response.setId(workspace.getId());
		response.setName(workspace.getName());
		response.setOwnerId(workspace.getOwner().getId());
		return response;
	}

	public WorkspcaeDtoResponse updateWorkspace(Long id, WorkspaceDtoRequest dto) {
		Workspace workspace = workspaceRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Workspace not found"));

		if (dto.getName() != null) {
			workspace.setName(dto.getName());
		}
		if (dto.getOwnerId() != null) {
			User user = userRepository.findById(dto.getOwnerId())
					.orElseThrow(() -> new RuntimeException("User not found"));
			workspace.setOwner(user);
		}

		Workspace saved = workspaceRepository.save(workspace);

		WorkspcaeDtoResponse response = new WorkspcaeDtoResponse();
		response.setId(saved.getId());
		response.setName(saved.getName());
		response.setOwnerId(saved.getOwner().getId());
		return response;
	}

	public void deleteWorkspace(Long id) {

	    Workspace workspace = workspaceRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Workspace not found"));

	    workspaceRepository.delete(workspace);
	}

}
