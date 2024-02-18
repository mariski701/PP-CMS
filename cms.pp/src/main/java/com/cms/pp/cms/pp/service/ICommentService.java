package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.model.dto.CommentDTO;
import com.cms.pp.cms.pp.model.entity.Comment;

import java.util.List;

public interface ICommentService {
    Comment findCommentById(long id);
    List<Comment> findAll();
    List<Comment> findByUsers(int id);
    List<Comment> findByUserName(String userName);
    List<Comment> findByArticleContent(int id);
    Object addComment(CommentDTO commentDTO);
    Object editCommentByUser(long commentId, String commentContent);
    Object editCommentInCMS(Long id, String content);
    Object removeOwnComment(long id);
}
