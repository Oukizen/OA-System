package com.example.demo.service;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.UploadFile;
import com.example.demo.utill.Pager;

public interface UploadedFileService {

    UploadFile storeFile(MultipartFile file) throws IOException;

    boolean deleteFile(Long fileId);

    List<UploadFile> getAllFiles();

    UploadFile getFileById(Long fileId);

    boolean batchDeleteFiles(List<Long> ids) throws IOException;

    Resource downloadFile(Long fileId);

    String getFileNameById(Long fileId);

    String getFileUrlById(Long fileId);

    UploadFile updateFile(UploadFile file);

    Pager<UploadFile> getFilesByPage(int page, int size, String name);

    // 新增方法：获取文件总数
    long getFileCount();
}

