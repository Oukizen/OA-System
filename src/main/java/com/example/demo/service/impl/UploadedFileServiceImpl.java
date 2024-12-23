package com.example.demo.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.UploadFile;
import com.example.demo.mapper.UploadFileMapper;
import com.example.demo.service.UploadedFileService;
import com.example.demo.utill.Pager;

@Service
public class UploadedFileServiceImpl implements UploadedFileService {

	private String FILE_DIRECTORY = "D:/Users/hqq/oa/OA-System/src/main/resources/uploads";

	@Autowired
	private UploadFileMapper uploadFileMapper;

	// ファイルアップロード
	public UploadFile storeFile(MultipartFile file) throws IOException {

		Path path = Paths.get(FILE_DIRECTORY);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
		}

		String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
		Path filePath = path.resolve(fileName);

		file.transferTo(filePath.toFile());

		UploadFile uploadedFile = new UploadFile();
		uploadedFile.setName(file.getOriginalFilename());
		uploadedFile.setUrl(filePath.toString());
		uploadedFile.setIsPublic(1);

		uploadFileMapper.insertFile(uploadedFile);

		return uploadedFile;
	}

	// ファイル削除
	public boolean deleteFile(Long fileId) {
		UploadFile uploadedFile = uploadFileMapper.getFileById(fileId);
		if (uploadedFile != null) {

			Path filePath = Paths.get(uploadedFile.getUrl());
			try {
				Files.delete(filePath);
				uploadFileMapper.deleteFile(fileId);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

	// すべてのファイルを取得
	public List<UploadFile> getAllFiles() {
		return uploadFileMapper.getAllFiles();
	}

	// ファイルダウンロード
	public Resource downloadFile(Long fileId) {
		String fileName = getFileNameById(fileId);
		if (fileName == null) {
			throw new RuntimeException("ファイルが見つかりません");
		}

		Path filePath = Paths.get(FILE_DIRECTORY, fileName);
		try {
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists() && resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("ファイルが読み取れません");
			}
		} catch (IOException e) {
			throw new RuntimeException("ファイルのダウンロードに失敗しました", e); // IOException をキャッチ
		}
	}

	// バッチ削除
	public boolean batchDeleteFiles(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			throw new RuntimeException("ファイル ID のリストが空です");
		}

		for (Long id : ids) {

			UploadFile uploadedFile = uploadFileMapper.getFileById(id);
			if (uploadedFile != null) {

				Path filePath = Paths.get(uploadedFile.getUrl());
				try {

					Files.delete(filePath);
					uploadFileMapper.deleteFile(id);
				} catch (IOException e) {

					e.printStackTrace();
					return false;
				}
			}
		}
		return false;
	}

	// ファイル URL を取得
	public String getFileUrlById(Long fileId) {
		String fileUrl = uploadFileMapper.getFileUrlById(fileId);
		if (fileUrl == null) {
			throw new RuntimeException("ファイル URL が見つかりません");
		}
		return fileUrl;
	}

	// ファイル名を取得
	public String getFileNameById(Long fileId) {
		return uploadFileMapper.getFileNameById(fileId); // 元のファイル名を取得
	}

	// ファイル情報を更新
	public UploadFile updateFile(UploadFile file) {
		UploadFile existingFile = uploadFileMapper.getFileById(file.getId());
		if (existingFile == null) {
			throw new RuntimeException("ファイルが見つかりません");
		}

		int updatedRows = uploadFileMapper.updateFileStatus(file.getId(), file.getIsPublic(), file.getName(),
				file.getUrl());

		if (updatedRows > 0) {
			return file;
		} else {
			throw new RuntimeException("ファイルの更新に失敗しました");
		}
	}

	// ID によってファイル情報を取得
	public UploadFile getFileById(Long fileId) {
		UploadFile file = uploadFileMapper.getFileById(fileId);

		if (file == null) {
			throw new RuntimeException("ファイルが見つかりません");
		}
		return file;
	}

	// ページネーション付きでファイルを取得
	public Pager<UploadFile> getFilesByPage(int page, int size, String name) {

		int offset = (page - 1) * size;

		List<UploadFile> files = uploadFileMapper.getFiles(offset, size, name);

		long total = uploadFileMapper.getTotalCount(name);

		return new Pager<>(page, size, files, total);
	}

	@Override
	public long getFileCount() {
	    // 调用 Mapper 的方法获取文档总数
	    return uploadFileMapper.getTotalCount(null); // 参数为 null 表示不限制条件
	}

}
