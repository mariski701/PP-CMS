package com.cms.pp.cms.pp.Comment;

import com.cms.pp.cms.pp.Article.ArticleContent;
import com.cms.pp.cms.pp.Article.ArticleContentRepository;
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

    public int addComment(CommentDTO commentDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (username.equals("anonymousUser")) {
            return 3005; //user not logged in
        }
        else
        {
            ArticleContent articleContent = articleContentRepository.findById(commentDTO.getArticleId()).orElse(null);
            if (commentDTO.getContent().equals("")) {
                return 3004;//content empty
            }
            if (articleContent == null) {
                return HttpStatus.NOT_FOUND.value(); //article not found
            }
            Comment comment = new Comment();
            comment.setContent(commentDTO.getContent());
            comment.setUser(userRepository.findByUserName(username));
            comment.setArticleContent(articleContent);
            commentRepository.save(comment);
            return 2001; //success
        }
    }


}
