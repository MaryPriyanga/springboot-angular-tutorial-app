package com.snipe.learning.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.snipe.learning.entity.CommentReply;
import com.snipe.learning.exception.UPLException;

@Service
public interface CommentReplyService {
	
	public CommentReply addReply(CommentReply reply) throws UPLException;
	public List<CommentReply> getRepliesByCommentId(Integer commentId) throws UPLException;

}
