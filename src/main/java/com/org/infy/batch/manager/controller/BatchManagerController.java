package com.org.infy.batch.manager.controller;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.org.infy.batch.manager.service.FileStorageService;
import com.org.infy.batch.manager.util.Constants;
import com.org.infy.batch.manager.util.ResponseHelper;

@RestController
public class BatchManagerController {
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private FileStorageService fileStorageService;

	@PostMapping("/batch-manager/upload/todo-template")
	public ResponseEntity<?> uploadAppreciation(
			@RequestParam(value = "files", required = false) MultipartFile[] files) {

		List<Path> listPath = Arrays.asList(files).stream()
				.map(file -> fileStorageService.storeFile(file, Constants.TODO_UPLOAD_TYPE))
				.collect(Collectors.toList());

		return (null != listPath)
				? new ResponseEntity<>(ResponseHelper.populateRresponse("TODO-Template uploaded sucessfully.", "Success"),
						HttpStatus.OK)
				: new ResponseEntity<>(ResponseHelper.populateRresponse("TODO-Template upload failed.", "failure"),
						HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/batch-manager/upload/campaign-template")
	public ResponseEntity<?> uploadCourse(@RequestParam(value = "files", required = false) MultipartFile[] files,
			String icountStore) {

		List<Path> listPath = Arrays.asList(files).stream()
				.map(file -> fileStorageService.storeFile(file, Constants.CAMPAIGN_UPLOAD_TYPE))
				.collect(Collectors.toList());

		return (null != listPath)
				? new ResponseEntity<>(ResponseHelper.populateRresponse("Campaign Template uploaded sucessfully.", "Success"),
						HttpStatus.OK)
				: new ResponseEntity<>(ResponseHelper.populateRresponse("Campaign Template upload failed.", "failure"),
						HttpStatus.BAD_REQUEST);
	}

}
