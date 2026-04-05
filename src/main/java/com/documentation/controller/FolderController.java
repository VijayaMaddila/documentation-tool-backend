package com.documentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.documentation.dto.FolderRequest;
import com.documentation.dto.FolderResponse;
import com.documentation.service.FolderService;

@RestController
@RequestMapping("/api/folder")
public class FolderController {
	
	@Autowired
	private FolderService folderService;
	
	@PostMapping
	public FolderResponse createFolder(@RequestBody FolderRequest folder)
	{
		return folderService.createFolder(folder);
	}
	
	@GetMapping("/user/{userId}")
	public List<FolderResponse> getAllFoldersById(@PathVariable Long userId)
	{
		return folderService.getAllFoldersById(userId);
	}

}
