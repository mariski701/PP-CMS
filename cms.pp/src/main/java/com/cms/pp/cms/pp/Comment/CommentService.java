package com.cms.pp.cms.pp.Comment;

import com.cms.pp.cms.pp.Article.ArticleContent;
import com.cms.pp.cms.pp.Article.ArticleContentRepository;
import com.cms.pp.cms.pp.user.User;
import com.cms.pp.cms.pp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleContentRepository articleContentRepository;

    Comment findCommentById(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    List<Comment> findAll() {
        return commentRepository.findAll();
    }

    List<Comment> findByUsers(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        else {
            return commentRepository.findByUser(user);
        }
    }

    List<Comment> findByArticleContent(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) {
            return null;
        }
        else
            return commentRepository.findByArticleContent(articleContent);
    }

    Comment addComment(Comment comment, int id) {
        Comment comment1 = comment;
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) {
            return null;
        }
        else {
            comment1.setArticleContent(articleContent);
            return commentRepository.save(comment1);
        }

    }


}
