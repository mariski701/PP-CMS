package com.cms.pp.cms.pp.Comment;

import com.cms.pp.cms.pp.Article.ArticleContent;
import com.cms.pp.cms.pp.user.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {
    Comment findByContent(String name);
    List<Comment> findByUser(User user);
    List<Comment> findByUser(User user, Sort sort);
    List<Comment> findByArticleContent(ArticleContent articleContent);

    List<Comment> findByArticleContent(ArticleContent articleContent, Sort sort);
    List<Comment> findByUser(String userName);
    List<Comment> findByUser(String userName, Sort sort);

    @Query("SELECT new com.cms.pp.cms.pp.Comment.CommentsCountModel(c.user.id, COUNT(c.user.id))"
    + "FROM Comment AS c GROUP BY c.user.id ORDER BY c.user.id desc ")
    List<CommentsCountModel> countTotalCommentsByUser();
}
