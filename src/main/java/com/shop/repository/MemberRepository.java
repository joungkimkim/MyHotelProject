package com.shop.repository;

import com.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query(value = "SELECT * FROM member where email=?", nativeQuery = true)
    Member findByEmail(@Param("email")String email);
    @Query(value = "SELECT * FROM member where member_id=?", nativeQuery = true)
    Member findByMemberId(@Param("id") Long id);
    @Query(value = "SELECT * FROM member ", nativeQuery = true)
    List<Member> findByAll();

    boolean existsByEmail(String email);

}
