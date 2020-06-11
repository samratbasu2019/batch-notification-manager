package com.org.infy.batch.manager.service;

import com.org.infy.batch.manager.exception.FileStorageException;
import com.org.infy.batch.manager.exception.MyFileNotFoundException;
import com.org.infy.batch.manager.property.FileStorageProperties;
import com.org.infy.batch.manager.util.Constants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {
	protected final Log logger = LogFactory.getLog(this.getClass());
	private final Path todoFileStorageLocation;
	private final Path campaignFileStorageLocation;
	
	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) {

		this.todoFileStorageLocation = Paths.get(fileStorageProperties.getTodoNotificationTemplateDir()).toAbsolutePath()
				.normalize();
		this.campaignFileStorageLocation = Paths.get(fileStorageProperties.getCampaignNotificationTemplateDir()).toAbsolutePath()
				.normalize();
		try {
			Files.createDirectories(this.todoFileStorageLocation);
			Files.createDirectories(this.campaignFileStorageLocation);
		} catch (Exception ex) {
			throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
					ex);
		}
	}

	public Path storeFile(MultipartFile file, String uploadType) {
		// Normalize file name
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		Path targetLocation = null; 
		try {
			// Check if the file's name contains invalid characters
			if (fileName.contains("..")) {
				throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
			}
			// Copy file to the target location (Replacing existing file with the same name)
			if (uploadType.equalsIgnoreCase(Constants.TODO_UPLOAD_TYPE))
				targetLocation = this.todoFileStorageLocation.resolve(fileName);
			else 
				targetLocation = this.campaignFileStorageLocation.resolve(fileName);
			
			logger.info("Template saved location is :" + targetLocation);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			return targetLocation;
		} catch (IOException ex) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
		}
	}

	public Resource loadFileAsResource(String fileName) {
		try {
			Path filePath = this.todoFileStorageLocation.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new MyFileNotFoundException("File not found " + fileName);
			}
		} catch (MalformedURLException ex) {
			throw new MyFileNotFoundException("File not found " + fileName, ex);
		}
	}

}
