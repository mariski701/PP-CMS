package com.cms.pp.cms.pp.controller;

import com.cms.pp.cms.pp.configuration.CustomCorsConfigAnnotation;
import com.cms.pp.cms.pp.model.dto.CommentDTO;
import com.cms.pp.cms.pp.model.entity.Comment;
import com.cms.pp.cms.pp.service.ICommentService;
import com.cms.pp.cms.pp.service.IUserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Data
@RequiredArgsConstructor
@RestController
@CustomCorsConfigAnnotation
@RequestMapping("/api/comments/")
public class CommentController {

	private final ICommentService ICommentService;

	private final IUserService userService;

	@GetMapping("find/{id}")
	public Comment findCommentById(@PathVariable long id) {
		return ICommentService.findCommentById(id);
	}

	@GetMapping("findAll")
	public List<Comment> findAll() {
		return ICommentService.findAll();
	}

	@GetMapping("find/user/{id}")
	public List<Comment> findByUserId(@PathVariable int id) {
		return ICommentService.findByUsers(id);
	}

	@GetMapping("find/{username}")
	public List<Comment> findByUserName(@PathVariable String username) {
		return ICommentService.findByUserName(username);
	}

	@GetMapping("find/article/{id}")
	public List<Comment> findByArticleContentId(@PathVariable int id) {
		return ICommentService.findByArticleContent(id);
	}

	@PostMapping("add")
	public Object addComment(@RequestBody CommentDTO commentDTO) {
		return ICommentService.addComment(commentDTO);
	}

	@PutMapping("/cms/edit/{id}")
	public Object editCommentInCMS(@PathVariable Long id, @RequestBody Map<String, String> body) {
		return ICommentService.editCommentInCMS(id, body.get("content"));
	}

	@PutMapping("edit")
	public Object editCommentByUser(@RequestBody Map<String, String> body) {
		return ICommentService.editCommentByUser(Long.parseLong(body.get("commentId")), body.get("content"));
	}

	@DeleteMapping("remove/{id}")
	public Object removeOwnComment(@PathVariable long id) {
		return ICommentService.removeOwnComment(id);
	}

}
