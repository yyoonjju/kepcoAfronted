package com.example.blog.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.blog.model.Board;
import com.example.blog.model.Member;
import com.example.blog.model.Response;
import com.example.blog.model.UploadFile;
import com.example.blog.repository.BoardRepository;
import com.example.blog.repository.MemberRepository;
import com.example.blog.repository.ResponseRepository;
import com.example.blog.repository.UploadFileRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {

    @Autowired
    BoardRepository boardRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ResponseRepository responseRepository;
    @Autowired
    UploadFileRepository uploadFileRepository;

    @GetMapping("/write")
    public String write(HttpSession session, Model model) {
        // 현재 날짜와 시간을 가져오는 로직
        LocalDateTime nowDateTime = LocalDateTime.now();
        String nowDateTimeFormat = nowDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        session.setAttribute("nowDateTime", nowDateTimeFormat);

        return "/html/write";
    }

    @PostMapping("/write")
    public String writePost2(@RequestParam("title") String title,
                            @RequestParam("contents") String contents,
                            @RequestParam("writedate") String writedate,
                            @RequestParam("file") MultipartFile file,
                            HttpSession session){

        String memberId=null;
        Member member = (Member) session.getAttribute("member");

        if(member!=null){
            memberId=member.getMemberId();
        }
        if (memberId==null||memberId.isEmpty()){
            memberId="guest";
        }
        Board board = new Board();
        board.setBoardId(memberId);
        board.setTitle(title);
        board.setContents(contents);
        board.setWritedate(writedate);

        if (!file.isEmpty()) {
            try {
                // 파일 이름 생성 (원본 파일 이름 그대로 사용하거나, 고유한 이름으로 생성)
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());

                // 파일을 서버에 저장할 디렉토리 경로
                String uploadDir = "C:\\uploadfile\\directory";

                // 디렉토리가 없으면 생성
                File dir = new File(uploadDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                // 파일을 지정된 경로에 저장
                Path filePath = Paths.get(uploadDir, fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // 파일 이름을 Board 객체에 저장
                board.setFileName(fileName);

                // 파일 업로드 성공 로그 출력 또는 다른 작업 수행
                System.out.println("File uploaded successfully: " + fileName);
            } catch (IOException e) {
                // 파일 업로드 실패 시 예외 처리
                e.printStackTrace();
                // 실패에 대한 로그 또는 다른 처리 수행
            }
        }

        boardRepository.save(board);
        session.setAttribute("board",board);


        return "redirect:/board";
    }

    @GetMapping("/content")
    public String content(@RequestParam("boardSeq") Integer boardSeq,
            @RequestParam(required = false) Integer replySeq,
            HttpSession session, Model model) {
        Board board = boardRepository.findByBoardSeq(boardSeq);
        model.addAttribute("board", board);

        String memberId;
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            memberId = member.getMemberId();
        } else {
            memberId = "guest";
        }
        model.addAttribute("memberId", memberId);

        List<Response> replyList = responseRepository.findByBoardSeq(boardSeq);
        model.addAttribute("replyList", replyList);
        System.out.println(replyList);

        if(replySeq == null){
            Response response = new Response();
            model.addAttribute("response", response); 
        } else {
           Response response = responseRepository.findByReplySeq(replySeq);
           model.addAttribute("response", response); 
        }

        return "html/content";
    }

    @PostMapping("/content")
    public String contentPost(@RequestParam("boardSeq") Integer boardSeq, Model model) {
        Board board = boardRepository.findByBoardSeq(boardSeq);
        model.addAttribute("board", board);
        return "html/modify";
    }

    // 수정
    @GetMapping("/modify")
    public String modify(@RequestParam("boardSeq") Integer boardSeq, Model model) {
        Board board = boardRepository.findByBoardSeq(boardSeq);
        model.addAttribute("board", board);

        return "html/modify";
    }

    @PostMapping("/modify")
    public String modifyPost(@RequestParam("boardSeq") Integer boardSeq,
            @RequestParam("title") String title,
            @RequestParam("contents") String contents,
            @RequestParam("boardId") String boardId,
            @RequestParam("writedate") String writedate) {
        System.out.println(boardSeq);
        Board board = new Board();

        board.setBoardSeq(boardSeq);
        board.setTitle(title);
        board.setContents(contents);
        board.setBoardId(boardId);
        board.setWritedate(writedate);
        System.out.println(board);
        boardRepository.save(board);

        return "redirect:/board";
    }

    @PostMapping("/delete")
    public String deletePost(@RequestParam("boardSeq") Integer boardSeq) {
        Board board = boardRepository.findByBoardSeq(boardSeq);
        boardRepository.delete(board);
        return "redirect:/board";
    }

    @PostMapping("/savereply")
    public String savereply(@RequestParam("boardSeq") Integer boardSeq,
            @RequestParam("replyId") String replyId,
            @RequestParam("replyContents") String replyContents) {
        Response response = new Response();
        response.setBoardSeq(boardSeq);
        response.setReplyId(replyId);
        response.setReplyContents(replyContents);
        responseRepository.save(response);
        return String.format("redirect:/content?boardSeq=%s", boardSeq);
    }

    @GetMapping("/updatereply")
    public String updatereply(@RequestParam("replySeq") Integer replySeq, Model model){
        Response response = responseRepository.findByReplySeq(replySeq);
        System.out.println(response);
        model.addAttribute("response", response);
        return "html/updatereply";
    }
    @PostMapping("/updatereply")
    public String updatereplyPost(
            @RequestParam("replySeq") Integer replySeq,
            @RequestParam("boardSeq") Integer boardSeq,
            @RequestParam("replyId") String replyId,
            @RequestParam("replyContents") String replyContents){

        Response response = new Response();
        response.setReplySeq(replySeq);
        response.setBoardSeq(boardSeq);
        response.setReplyId(replyId);
        response.setReplyContents(replyContents);
        System.out.println(response);
        responseRepository.save(response);
        return String.format("redirect:/content?boardSeq=%s", boardSeq);
    }
    @PostMapping("/deletereply")
    public String deletereplyPost(
        @RequestParam("boardSeq") Integer boardSeq,
        @RequestParam("replySeq") Integer replySeq){
        Response response=responseRepository.findByReplySeq(replySeq);
        responseRepository.delete(response);
        return String.format("redirect:/content?boardSeq=%s", boardSeq);
    }
}
