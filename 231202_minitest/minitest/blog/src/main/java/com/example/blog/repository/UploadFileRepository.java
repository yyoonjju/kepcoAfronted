package com.example.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog.model.UploadFile;

@Repository
public interface UploadFileRepository extends JpaRepository<UploadFile, Integer>{
    
}
