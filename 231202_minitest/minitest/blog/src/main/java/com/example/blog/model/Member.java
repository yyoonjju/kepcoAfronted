package com.example.blog.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity // 적용해야 JPA가 인식
public class Member {
    @Id
    String memberId;
    String memberPw;
    String memberName;
}
