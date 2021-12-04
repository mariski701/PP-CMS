package com.cms.pp.cms.pp.Comment;

import com.cms.pp.cms.pp.Article.ArticleContent;
import com.cms.pp.cms.pp.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    Comment findByContent(String name);
    List<Comment> findByUser(User user);
    List<Comment> findByArticleContent(ArticleContent articleContent);
}
