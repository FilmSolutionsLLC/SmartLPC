package com.fps.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by macbookpro on 4/6/17.
 */
public interface StorageService {

    void store(MultipartFile file);



}
