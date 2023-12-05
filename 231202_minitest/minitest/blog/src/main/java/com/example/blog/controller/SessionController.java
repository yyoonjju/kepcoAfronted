package com.example.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.blog.model.Board;
import com.example.blog.model.Member;
import com.example.blog.repository.MemberRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class SessionController {

    @Autowired
    MemberRepository memberRepository;

    // 회원가입

    @GetMapping("/register")
    public String register() {
        return "html/registerForm"; // 회원가입 버튼 누르면 회원가입 화면으로 이동
    }

    @PostMapping("/register")
    public String register2(
            @RequestParam("memberId") String memberId,
            @RequestParam("memberPw") String memberPw,
            @RequestParam("memberName") String memberName,
            HttpSession session) {

        Member member = new Member();
        member.setMemberId(memberId);
        member.setMemberPw(memberPw);
        member.setMemberName(memberName);

        memberRepository.save(member);

        session.setAttribute("member", member);

        return "redirect:/board";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/board";
    }

    @GetMapping("/login")
    public String login() {
        return "html/loginForm"; // 로그인 버튼 누르면 로그인 화면으로 이동
    }

    @PostMapping("/login")
    public String login2(
            @RequestParam("memberId") String memberId,
            @RequestParam("memberPw") String memberPw,
            HttpSession session) {

        Member member;
        member = memberRepository.findByMemberIdAndMemberPw(memberId, memberPw);
        
        if (member != null) {
            // 로그인 성공
            session.setAttribute("member",member);
            return "redirect:/board";   
        } else {
            // 로그인 실패
            session.invalidate(); 
            return "html/loginfail";
        }
        
    }

    
}
