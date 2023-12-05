package com.example.blog.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; //인터페이스
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.blog.model.Board;
import com.example.blog.model.Member;
import com.example.blog.model.Response;
import com.example.blog.repository.BoardRepository;
import com.example.blog.repository.ResponseRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HtmlController {
  
  @Autowired
  BoardRepository boardRepository;
  @Autowired
  ResponseRepository responseRepository;

  @GetMapping("/board")
  public String boardList(
    Model model,
    @RequestParam(defaultValue="1") int page
    ){
    List<Board>data = boardRepository.findAll();
    model.addAttribute("boardList", data);

    // 페이지 표시 Pagination, linkUrl
    int startPage=((page-1)/10)*10+1;
    int endPage=startPage+9;
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);
    model.addAttribute("page", page);

    return "html/board";
  }
//   @PostMapping("/board")
//   public String boardPost(@RequestParam("boardSeq") Integer boardSeq, Model model, HttpSession session){
//     Board board=boardRepository.findByBoardSeq(boardSeq);
//     model.addAttribute("board", board);

    

//     String memberId;
//         Member member = (Member) session.getAttribute("member");
//         if (member != null) {
//             memberId = member.getMemberId();
//         } else {
//             memberId = "guest";
//         }
//     model.addAttribute("memberId", memberId);

//     return "html/content";
//   }
}