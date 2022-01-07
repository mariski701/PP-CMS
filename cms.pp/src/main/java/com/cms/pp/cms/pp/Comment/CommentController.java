package com.cms.pp.cms.pp.Comment;


import com.cms.pp.cms.pp.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/comments/")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;

    @GetMapping("find/{id}")
    public Comment findCommentById(@PathVariable long id) {
        return commentService.findCommentById(id);
    }

    @GetMapping("findAll")
    public List<Comment> findAll() {
        return commentService.findAll();
    }

    @GetMapping("find/user/{id}")
    public  List<Comment> findByUserId(@PathVariable int id) {
        return commentService.findByUsers(id);
    }

    @GetMapping("find/{username}")
    public List<Comment> findByUserName(@PathVariable String username) { return commentService.findByUserName(username); }

    @GetMapping("find/article/{id}")
    public  List<Comment> findByArticleContentId(@PathVariable int id) {
        return commentService.findByArticleContent(id);
    }

    @PostMapping("add")
    public Object addComment(@RequestBody CommentDTO commentDTO) {
        return commentService.addComment(commentDTO);
    }

    @PutMapping("/cms/edit/{id}")
    public Object editCommentInCMS(@PathVariable Long id, @RequestBody Map<String, String> body) {
        return commentService.editCommentInCMS(id, body.get("content"));
    }

    @PutMapping("edit") //long commentId, String commentContent
    public Object editCommentByUser(@RequestBody Map<String, String> body) {
        return commentService.editCommentByUser(Long.parseLong(body.get("commentId")), body.get("content"));
    }

}
