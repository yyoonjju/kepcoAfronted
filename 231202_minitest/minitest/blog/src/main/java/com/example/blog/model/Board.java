package com.example.blog.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Board {
  
  @Id //id 자동으로 생성할 건지 말건지
  @GeneratedValue(strategy=GenerationType.IDENTITY)
  private Integer boardSeq;
  private String title;
  private String contents;
  private String boardId;
  private String writedate;
  private String fileName;
}
