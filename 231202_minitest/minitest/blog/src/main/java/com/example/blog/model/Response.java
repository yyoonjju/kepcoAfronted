package com.example.blog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Response {
  @Id //id 자동으로 생성할 건지 말건지
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer replySeq;
  
  private String replyId;
  private Integer boardSeq; 
  private String replyContents;
}
