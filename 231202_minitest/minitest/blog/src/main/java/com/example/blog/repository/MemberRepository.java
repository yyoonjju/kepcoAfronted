package com.example.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.blog.model.Member;


@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
   Member findByMemberIdAndMemberPw(String memberId, String memberPw);
   Member findByMemberId(String memberId);
}
