package com.example.blog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog.model.Response;

@Repository
public interface ResponseRepository extends JpaRepository<Response,Integer> {

    List<Response> findByBoardSeq(Integer boardSeq);
    Response findByReplySeq(Integer replySeq);

    
    
}

//Response 테이블  행 1개
// List<Response> 행 2개이상