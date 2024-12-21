package com.example.demo.controller;

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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.UploadFile;
import com.example.demo.service.UploadedFileService;
import com.example.demo.utill.Pager;

@RestController
@RequestMapping("/file")
public class UploadFileController {

	private String FILE_DIRECTORY = "D:/Users/hqq/oa/OA-System/src/main/resources";

	@Autowired
	private UploadedFileService fileService;

	// 显示文件管理页面
	@GetMapping("/page")
	public String showFilePage() {
		return "file"; // `file.html` 需要在 `resources/templates` 文件夹中
	}

	// ファイルのアップロード
	@PostMapping
	public ResponseEntity<UploadFile> uploadFile(@RequestParam("file") MultipartFile file) {
		try {
			UploadFile uploadedFile = fileService.storeFile(file);

			uploadedFile.setIsPublic(1);
			return ResponseEntity.ok(uploadedFile);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	// ファイルの取得（ページング）
	@GetMapping
	public ResponseEntity<Pager<UploadFile>> getFiles(@RequestParam(required = false) String name,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
		try {
			if (page < 1 || size < 1) {
				return ResponseEntity.badRequest().body(null);
			}
			Pager<UploadFile> pager = fileService.getFilesByPage(page, size, name);

			return ResponseEntity.ok(pager);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// 単一ファイルの削除
	@DeleteMapping("/{fileId}")
	public ResponseEntity<Void> deleteFile(@PathVariable Long fileId) {
		boolean deleted = fileService.deleteFile(fileId);
		if (deleted) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// バッチファイル削除
	@PostMapping("/batch-delete")
	public ResponseEntity<Void> batchDelete(@RequestBody Map<String, List<Long>> fileIds) {

		List<Long> ids = fileIds.get("fileIds");

		if (ids == null || ids.isEmpty()) {
			return ResponseEntity.badRequest().build();
		}

		boolean success = fileService.batchDeleteFiles(ids);

		if (success) {
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	// ファイルの公開/非公開ステータスの切り替え
	@PutMapping("/{fileId}/toggle-status")
	public ResponseEntity<UploadFile> toggleFileStatus(@PathVariable Long fileId) {
		try {
			UploadFile file = fileService.getFileById(fileId);

			if (file == null) {
				return ResponseEntity.notFound().build();
			}

			// 公開状態を切り替え
			if (file.getIsPublic() == 1) {
				file.setIsPublic(0);
			} else {
				file.setIsPublic(1);
			}

			UploadFile updatedFile = fileService.updateFile(file);

			return ResponseEntity.ok(updatedFile);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500エラーを返す
		}
	}

	// ファイルのダウンロード
	@GetMapping("/download/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
		try {
			// ファイル名とURLを取得
			String originalFileName = fileService.getFileNameById(fileId);
			String uuidFileName = fileService.getFileUrlById(fileId);

			Path filePath = Paths.get(FILE_DIRECTORY, uuidFileName);
			filePath = filePath.normalize();

			Resource resource = new UrlResource(filePath.toUri());

			// ファイルが存在し、読み取り可能かチェック
			if (resource.exists() && resource.isReadable()) {
				String contentType = Files.probeContentType(filePath);
				if (contentType == null) {
					contentType = "application/octet-stream";
				}

				// ファイルをダウンロードするためにレスポンスを返す
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + originalFileName + "\"")
						.header(HttpHeaders.CONTENT_TYPE, contentType).body(resource);
			} else {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
}
