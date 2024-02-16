package com.example.performancekeeper.api.repository;

import com.example.performancekeeper.api.entity.comment.CommentEntity;
import com.example.performancekeeper.api.entity.comment.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    @Query("select r from ReplyEntity r join fetch r.writer where r.comment = :comment")
    List<ReplyEntity> findAllByComment(@Param("comment") CommentEntity comment);
}
