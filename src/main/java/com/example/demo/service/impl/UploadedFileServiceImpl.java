package com.example.demo.service.impl;

import java.io.File;
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

	private String FILE_DIRECTORY = "src/main/resources/uploads";

	@Autowired
	private UploadFileMapper uploadFileMapper;

	public UploadFile storeFile(MultipartFile file) throws IOException {

		String projectPath = System.getProperty("user.dir");
		String filesPath = projectPath + "/src/main/resources/uploads";

		File parentFile = new File(filesPath);
		if (!parentFile.exists()) {
			parentFile.mkdirs(); // 创建上传目录
		}

		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || originalFilename.isEmpty()) {
			throw new RuntimeException("文件名无效");
		}

		// 提取文件的扩展名
		int dotIndex = originalFilename.lastIndexOf(".");
		String extName = (dotIndex == -1) ? "" : originalFilename.substring(dotIndex); // 包括 "."

		// 生成唯一文件名用于存储
		String uniqueName = UUID.randomUUID().toString() + extName;
		File saveFile = new File(filesPath + "/" + uniqueName);

		file.transferTo(saveFile); // 保存文件

		// 数据库中保存文件信息
		UploadFile uploadedFile = new UploadFile();
		uploadedFile.setName(originalFilename); // 保存原始文件名
		uploadedFile.setUrl(saveFile.getPath()); // 保存文件实际存储路径
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
		// 从数据库获取文件名
		String fileName = getFileNameById(fileId);
		if (fileName == null || fileName.isEmpty()) {
			throw new RuntimeException("指定されたファイルが存在しません。");
		}

		// 构建文件路径
		Path filePath = Paths.get(FILE_DIRECTORY, fileName);

		try {
			// 创建资源对象
			Resource resource = new UrlResource(filePath.toUri());

			// 检查资源是否存在并可读
			if (!resource.exists() || !resource.isReadable()) {
				throw new RuntimeException("ファイルが存在しないか、読み取ることができません。");
			}

			return resource; // 返回资源对象
		} catch (IOException e) {
			// 捕获并处理异常
			throw new RuntimeException("ファイルのダウンロード中にエラーが発生しました。", e);
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
		return true;
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
