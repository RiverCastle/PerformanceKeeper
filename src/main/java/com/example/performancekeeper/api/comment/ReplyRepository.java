package com.example.performancekeeper.api.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
    List<ReplyEntity> findAllByComment(CommentEntity comment);
}
