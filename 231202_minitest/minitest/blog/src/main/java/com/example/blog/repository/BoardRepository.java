package com.example.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog.model.Board;


@Repository // 자동으로 bean으로 생성해줌
public interface BoardRepository extends JpaRepository<Board, Integer> {
    Board findByBoardSeq(Integer boardSeq);
    Board findByBoardId(String boardId);
    
}
