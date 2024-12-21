package com.example.demo.controller;

import java.io.IOException;
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

	private String FILE_DIRECTORY = "D:/Users/hqq/oa/OA-System/src/main/resources/uploads";

	@Autowired
	private UploadedFileService fileService;
	@Autowired
	private UploadFileMapper uploadFileMapper;

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
		boolean success = false; // 初始化 success

		try {
			success = fileService.batchDeleteFiles(ids);
		} catch (IOException e) {
			e.printStackTrace();
			return "error"; // 如果发生异常，直接返回错误信息
		}

		return success ? "redirect:/file" : "error"; // 根据 success 的值返回结果
	}

	// ファイルの公開/非公開ステータスの切り替え
	@PutMapping("/file/{fileId}/toggle-status")
	@ResponseBody
	public String toggleFileStatus(@PathVariable Long fileId) {

		UploadFile file = fileService.getFileById(fileId);
		file.setIsPublic(file.getIsPublic() == 1 ? 0 : 1);
		fileService.updateFile(file);
		return "redirect:/file";

	}

	@GetMapping("/file/download/{fileId}")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
		try {
			String originalFileName = fileService.getFileNameById(fileId);
			UploadFile uploadedFile = uploadFileMapper.getFileById(fileId);

			Path filePath = Paths.get(uploadedFile.getUrl());

			System.out.println("File Path: " + filePath);
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists() && resource.isReadable()) {
				String contentType = Files.probeContentType(filePath);
				if (contentType == null) {
					contentType = "application/octet-stream"; // 如果无法确定 MIME 类型，返回二进制流
				}

				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
						.header(HttpHeaders.CONTENT_TYPE, contentType).body(resource);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 文件不可读取，返回404
			}
		} catch (Exception e) {

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // 返回500错误
		}
	}

}
