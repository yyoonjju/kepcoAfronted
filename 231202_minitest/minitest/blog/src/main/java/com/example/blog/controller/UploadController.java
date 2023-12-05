package com.example.blog.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.blog.model.Member;
import com.example.blog.model.UploadFile;
import com.example.blog.repository.UploadFileRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class UploadController {
    @Autowired
    UploadFileRepository uploadFileRepository;

    @GetMapping("upload")
    public String uploadFile(){
        return "html/upload";
    }

    @PostMapping("upload")
    public String uploadFilePost(@RequestParam("file") MultipartFile [] mFiles, Model model, HttpSession session){
        UploadFile uploadFile = new UploadFile();
        String saveFolder = "c:/data/";

        String memberId;
        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            memberId = member.getMemberId();
        } else {
            memberId = "guest";
        }

        String result="";
        for (MultipartFile mFile : mFiles){
            String uid=UUID.randomUUID().toString();
            String oName=mFile.getOriginalFilename();
            int seq=uploadFileRepository.findAll().size()+1;
            uploadFile.setSeq(seq);
            uploadFile.setOriginalFileName(oName);
            uploadFile.setUuid(uid);
            uploadFile.setMemberId(memberId);
            File saveFile=new File(saveFolder+uid); 

            try{
                mFile.transferTo(saveFile);
                uploadFileRepository.save(uploadFile);
            } catch (IllegalStateException | IOException e){
                e.printStackTrace();
            }
            result += "<p>"+oName+"<p>";
        }
        model.addAttribute("uploadFile", result);
        return "html/upload";
    }
}
