package com.cms.pp.cms.pp.mapper;

import com.cms.pp.cms.pp.model.dto.CommentDTO;
import com.cms.pp.cms.pp.model.entity.ArticleContent;
import com.cms.pp.cms.pp.model.entity.Comment;
import com.cms.pp.cms.pp.model.entity.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Data
public class AddCommentMapper {

	public Comment mapToComment(CommentDTO commentDTO, User user, ArticleContent articleContent) {
		return new Comment().setContent(commentDTO.getContent()).setUser(user).setArticleContent(articleContent);
	}

}
