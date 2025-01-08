package com.example.demo.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.UploadFile;
import com.example.demo.mapper.UploadFileMapper;
import com.example.demo.service.UploadedFileService;
import com.example.demo.utill.Pager;

@Controller
public class UploadFileController {

	@Autowired
	private UploadedFileService fileService;
	@Autowired
	private UploadFileMapper uploadFileMapper;

	@GetMapping("/userfile")
	public String home() {
		return "userfile";
	}

	@PostMapping("/file")
	@ResponseBody
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			UploadFile uploadedFile = fileService.storeFile(file);
			uploadedFile.setIsPublic(1);
			return ResponseEntity.ok("ファイルのアップロードが成功しました！");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("ファイルのアップロードに失敗しました");
		}
	}

	// ファイルの取得（ページング） - 返回 HTML 页面
	@GetMapping("/file")
	public String getFiles(Model model, @RequestParam(required = false) String name,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
		Pager<UploadFile> pager = fileService.getFilesByPage(page, size, name);
		model.addAttribute("pager", pager);
		return "file";

	}

	// ファイルの取得（ページング） - 返回 JSON 数据
	@GetMapping("/file/list")
	@ResponseBody
	public ResponseEntity<?> getFileList(@RequestParam(required = false) String name,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {

		Pager<UploadFile> pager = fileService.getFilesByPage(page, size, name);
		return ResponseEntity.ok(pager);

	}

	// 单一ファイルの削除

	@DeleteMapping("/file/{fileId}")
	@ResponseBody
	public String deleteFile(@PathVariable Long fileId) {
		boolean deleted = fileService.deleteFile(fileId);
		if (deleted) {
			return "redirect:/file";
		} else {
			return "error";
		}
	}

	// バッチファイル削除
	@PostMapping("/file/batch-delete")
	@ResponseBody
	public String batchDelete(@RequestBody Map<String, List<Long>> fileIds) {
		List<Long> ids = fileIds.get("fileIds");
		boolean success = false;

		try {
			success = fileService.batchDeleteFiles(ids);
		} catch (IOException e) {
			e.printStackTrace();
			return "error";
		}

		return success ? "redirect:/file" : "error";
	}

	@PutMapping("/file/{fileId}/toggle-status")
	public ResponseEntity<UploadFile> toggleFileStatus(@PathVariable Long fileId) {
		try {

			UploadFile file = fileService.getFileById(fileId);

			if (file == null) {
				return ResponseEntity.notFound().build();
			}
			if (file.getIsPublic() == 1) {
				file.setIsPublic(0);
			} else {
				file.setIsPublic(1);
			}
			UploadFile updatedFile = fileService.updateFile(file);
			return ResponseEntity.ok(updatedFile);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/file/download/{fileId}")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
		try {
			String originalFileName = fileService.getFileNameById(fileId);

			UploadFile uploadedFile = uploadFileMapper.getFileById(fileId);
			Path filePath = Paths.get(uploadedFile.getUrl());

			Resource resource = new UrlResource(filePath.toUri());

			if (Files.exists(filePath) && Files.isReadable(filePath)) {

				String contentType = Files.probeContentType(filePath);
				if (contentType == null) {
					contentType = "application/octet-stream";
				}

				String encodedFileName = URLEncoder.encode(originalFileName, "UTF-8").replace("+", "%20");
				String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

				return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
						.header(HttpHeaders.CONTENT_TYPE, contentType)
						.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(Files.size(filePath))).body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
