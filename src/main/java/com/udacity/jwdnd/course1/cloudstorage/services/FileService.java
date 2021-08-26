package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgAlreadyExistsException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.ArgNotFoundException;
import com.udacity.jwdnd.course1.cloudstorage.exceptions.NoRowsAffectedException;
import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.FileModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public boolean isFileNameFound(String fileName) {
        return fileMapper.checkFile(fileName) == 1;
    }

    public int saveFile(FileModel file) throws ArgAlreadyExistsException, NoRowsAffectedException {
        if (isFileNameFound(file.getFileName())) {
            throw new ArgAlreadyExistsException(String.format("%s file already exists", file.getFileName()));
        }

        int rows = fileMapper.createFile(file);
        if (rows < 1) {
            throw new NoRowsAffectedException(String.format("%d file(s) created", rows));
        }

        return file.getFileId();
    }

    public List<FileModel> getFiles() {
        return fileMapper.getFiles();
    }

    public FileModel getFile(String fileName) throws ArgNotFoundException {
        if (!isFileNameFound(fileName)) {
            throw new ArgNotFoundException(String.format("%s file not found", fileName));
        }

        return fileMapper.getFile(fileName);
    }

    public void deleteFile(String fileName) throws ArgNotFoundException, NoRowsAffectedException {
        if (!isFileNameFound(fileName)) {
            throw new ArgNotFoundException(String.format("%s file not found", fileName));
        }

        if (!fileMapper.deleteFile(fileName)) {
            throw new NoRowsAffectedException("Failed to delete file");
        }
    }
}
