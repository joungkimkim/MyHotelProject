package com.shop.repository;

import com.shop.entity.Board;
import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board ,Long>, QuerydslPredicateExecutor<Board>, BoardRepositoryCustom{

    @Query(value = "SELECT * FROM board", nativeQuery = true)
    List<Board> findAll();
    @Modifying(clearAutomatically = true)
    @Query(value = "update board set title = ? , content = ? where board_id = ?", nativeQuery = true)
    int updateByBoardId(@Param("title")String title, @Param("content") String content, @Param("id") Long id);

    @Query(value = "SELECT * FROM board  where board_id=?", nativeQuery = true)
    Board findByBoardId(@Param("id") Long id);

    @Query(value = "delete FROM board  where board_id=?", nativeQuery = true)
    Board deleteByBoardId(@Param("id") Long id);


}
