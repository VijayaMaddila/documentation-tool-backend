package com.documentation.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.documentation.dto.FolderRequest;
import com.documentation.dto.FolderResponse;
import com.documentation.entity.Folder;
import com.documentation.entity.User;
import com.documentation.entity.Workspace;
import com.documentation.repository.FolderRepository;
import com.documentation.repository.UserRepository;
import com.documentation.repository.WorkspaceRepository;

@Service
public class FolderService {
	
	@Autowired
	private FolderRepository folderRepository;
	
	@Autowired
	private WorkspaceRepository workspaceRepository;
	
	@Autowired
	private UserRepository userRepository;
	

	public FolderResponse createFolder(FolderRequest folder) {
		User user=userRepository.findById(folder.getUserId())
		.orElseThrow(()->new RuntimeException("User not found"));

		Workspace workspace=workspaceRepository.findById(folder.getWorkspaceId())
		.orElseThrow(()->new RuntimeException("Workspace not found"));

		Folder request=new Folder();
		request.setName(folder.getName());
		request.setUser(user);
		request.setWorkspace(workspace);

		Folder saved=folderRepository.save(request);

		FolderResponse response=new FolderResponse();
		response.setId(saved.getId());
		response.setName(saved.getName());
		response.setUserId(saved.getUser().getId());
		response.setWorkspaceId(saved.getWorkspace().getId());
		
		return response;
	}


	public List<FolderResponse> getAllFoldersById(Long userId) {
		return folderRepository.findByUserId(userId).stream().map(folder -> {
			FolderResponse response = new FolderResponse();
			response.setId(folder.getId());
			response.setName(folder.getName());
			response.setUserId(folder.getUser().getId());
			response.setWorkspaceId(folder.getWorkspace().getId());
			return response;
		}).toList();
	}

}
