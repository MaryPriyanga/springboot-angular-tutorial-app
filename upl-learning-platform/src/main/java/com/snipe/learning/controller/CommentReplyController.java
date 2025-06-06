package com.snipe.learning.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snipe.learning.entity.CommentReply;
import com.snipe.learning.service.CommentReplyService;

@RestController
@RequestMapping("/api/replies")
public class CommentReplyController {

    @Autowired
    private CommentReplyService replyService;

    @PostMapping
    @PreAuthorize("hasRole('Admin') or hasRole('Instructor')")
    public ResponseEntity<CommentReply> addReply(@RequestBody CommentReply reply, Principal principal) {
        reply.setRepliedBy(principal.getName());
        reply.setCreatedAt(LocalDateTime.now());
        // Extract role from SecurityContext
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        reply.setReplierRole(auth.getAuthorities().iterator().next().getAuthority());
        return ResponseEntity.ok(replyService.addReply(reply));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<List<CommentReply>> getReplies(@PathVariable Integer commentId) {
        return ResponseEntity.ok(replyService.getRepliesByCommentId(commentId));
    }
}
