package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.UploadFile;
import com.example.demo.utill.Pager;

public interface UploadedFileService {

	// 存储文件
	UploadFile storeFile(MultipartFile file) throws IOException;

	// 删除文件
	boolean deleteFile(Long fileId);

	// 获取所有文件
	List<UploadFile> getAllFiles();

	// 根据文件ID查询文件
	UploadFile getFileById(Long fileId);

	// 批量删除文件
	boolean batchDeleteFiles(List<Long> ids);

	// 下载文件
	Resource downloadFile(Long fileId);

	// 获取文件的原始文件名
	String getFileNameById(Long fileId);

	// 获取文件的存储路径
	String getFileUrlById(Long fileId);

	// 更新文件信息
	UploadFile updateFile(UploadFile file);

	Pager<UploadFile> getFilesByPage(int page, int size, String name);

}
