package com.projectstudy.member.repository;

import com.projectstudy.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByUserId(String id);
    Member findMemberByUserPhone(String phone);
    Member findByUserIdAndUserPhone(String id, String phone);

    @Modifying
    @Transactional
    @Query(value = "update member m set m.user_pw = :pw where m.user_id = :id and m.phone = :phone", nativeQuery = true)
    void updatePw(String id, String phone, String pw);

    
}
