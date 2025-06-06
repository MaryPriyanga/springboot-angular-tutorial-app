package com.snipe.learning.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snipe.learning.entity.CommentReply;
import com.snipe.learning.exception.UPLException;
import com.snipe.learning.repository.CommentReplyRepository;

@Service
public class CommentReplyServiceImpl implements CommentReplyService{

	@Autowired
	private CommentReplyRepository commentReplyRepository;
	@Override
	public CommentReply addReply(CommentReply reply) throws UPLException {
		 return commentReplyRepository.save(reply);
	}

	@Override
	public List<CommentReply> getRepliesByCommentId(Integer commentId) throws UPLException {
		return commentReplyRepository.findByCommentId(commentId);
	}

}
