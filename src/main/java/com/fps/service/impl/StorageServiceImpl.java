package com.fps.service.impl;

import com.fps.service.StorageService;
import com.fps.service.exception.StorageException;
import com.fps.service.util.StorageProperties;
import org.elasticsearch.common.inject.Inject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by macbookpro on 4/6/17.
 */
public class StorageServiceImpl implements StorageService {

    private final Path rootLocation;

    @Inject
    public StorageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }


    @Override
    public void store(MultipartFile file) {
        try {

            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }


}
