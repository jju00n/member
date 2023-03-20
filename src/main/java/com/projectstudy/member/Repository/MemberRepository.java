package com.projectstudy.member.repository;

import com.projectstudy.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByUserId(String id);
}
