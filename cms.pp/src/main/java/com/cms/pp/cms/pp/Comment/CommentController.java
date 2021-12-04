package com.cms.pp.cms.pp.Comment;


import com.cms.pp.cms.pp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/comments/")
@RestController
public class CommentController {

    @Autowired
    CommentService commentService;
    @Autowired
    UserService userService;

    @GetMapping("find/{id}")
    Comment findCommentById(@PathVariable long id) {
        return commentService.findCommentById(id);
    }

    @GetMapping("find")
    List<Comment> findAll() {
        return commentService.findAll();
    }

    @GetMapping("find/user/{id}")
    List<Comment> findByUserId(@PathVariable int id) {
        return commentService.findByUsers(id);
    }

    @GetMapping("find/article/{id}")
    List<Comment> findByArticleContentId(@PathVariable int id) {
        return commentService.findByArticleContent(id);
    }

    @PostMapping("add/{id}")
    Comment addComment(@RequestBody Comment comment, @PathVariable int id) {
        return commentService.addComment(comment, id);
    }

}
