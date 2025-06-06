package com.snipe.learning.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snipe.learning.entity.CommentReply;

@Repository
public interface CommentReplyRepository extends JpaRepository<CommentReply, Integer> {
    List<CommentReply> findByCommentId(Integer commentId);
}