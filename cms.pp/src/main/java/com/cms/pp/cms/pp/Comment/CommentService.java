package com.cms.pp.cms.pp.Comment;

import com.cms.pp.cms.pp.Article.ArticleContent;
import com.cms.pp.cms.pp.Article.ArticleContentRepository;
import com.cms.pp.cms.pp.ConfigurationFlags.ConfigurationFlags;
import com.cms.pp.cms.pp.ConfigurationFlags.ConfigurationFlagsRepository;
import com.cms.pp.cms.pp.user.User;
import com.cms.pp.cms.pp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    @Autowired
    ConfigurationFlagsRepository configurationFlagsRepository;

    public Comment findCommentById(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public List<Comment> findByUsers(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        else {
            return commentRepository.findByUser(user);
        }
    }

    public List<Comment> findByArticleContent(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) {
            return null;
        }
        else
            return commentRepository.findByArticleContent(articleContent);
    }

    public String addComment(CommentDTO commentDTO) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        if (configurationFlags.isComments()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            }
            else {
                username = principal.toString();
            }
            if (username.equals("anonymousUser")) {
                return "message.3005"; //user not logged in
            }
            else
            {
                ArticleContent articleContent = articleContentRepository.findById(commentDTO.getArticleId()).orElse(null);
                if (commentDTO.getContent().equals("")) {
                    return "message.3004";//content empty
                }
                if (articleContent == null) {
                    return "message.404"; //article not found
                }
                Comment comment = new Comment();
                comment.setContent(commentDTO.getContent());
                comment.setUser(userRepository.findByUserName(username));
                comment.setArticleContent(articleContent);
                commentRepository.save(comment);
                return "message.2001"; //success
            }
        }
        else
            return "message.3015"; //comments turned off
    }

    public String editCommentByUser(long commentId, String commentContent) { //todo
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (username.equals("anonymousUser")) {
            return "message.3005";
        }
        else {
            User user = userRepository.findByUserName(username);
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment == null) {
                return "message.404";
            }
            if (!(user.getId() == comment.getId())) {
                return "message.403";
            }
            if (commentContent.equals(""))
                return "message.3004";
            comment.setContent(commentContent);
            commentRepository.save(comment);
            return "message.2001";
        }
    }

    public String editCommentInCMS(Long id, String content) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            return "message.404";
        }
        if (id.equals(null)) {
            return "message.404";
        }
        if (content.equals("")) {
            return "message.3004";
        }
        comment.setContent(content);
        commentRepository.save(comment);
        return "message.2001";
    }

}
