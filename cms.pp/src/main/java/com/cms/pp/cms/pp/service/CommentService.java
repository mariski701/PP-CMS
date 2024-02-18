package com.cms.pp.cms.pp.service;

import com.cms.pp.cms.pp.enums.Code;
import com.cms.pp.cms.pp.enums.PrivilegeName;
import com.cms.pp.cms.pp.model.entity.ArticleContent;
import com.cms.pp.cms.pp.repository.ArticleContentRepository;
import com.cms.pp.cms.pp.model.entity.Comment;
import com.cms.pp.cms.pp.model.dto.CommentDTO;
import com.cms.pp.cms.pp.repository.CommentRepository;
import com.cms.pp.cms.pp.model.entity.ConfigurationFlags;
import com.cms.pp.cms.pp.repository.ConfigurationFlagsRepository;
import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.repository.PrivilegeRepository;
import com.cms.pp.cms.pp.model.entity.Role;
import com.cms.pp.cms.pp.repository.RoleRepository;
import com.cms.pp.cms.pp.model.entity.User;
import com.cms.pp.cms.pp.repository.UserRepository;
import com.cms.pp.cms.pp.utils.ErrorProvidedDataHandlerUtils;
import com.cms.pp.cms.pp.utils.PrincipalUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Data
@RequiredArgsConstructor
@Service("CommentService")
public class CommentService implements ICommentService {
    private static final String ANONYMOUS_USER = "anonymousUser";
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ArticleContentRepository articleContentRepository;
    private final ConfigurationFlagsRepository configurationFlagsRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;

    @Override
    public Comment findCommentById(long id) {
        return commentRepository.findById(id).orElse(null);
    }

    @Override
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Override
    public List<Comment> findByUsers(int id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return null;
        }
        return commentRepository.findByUser(user, Sort.by("id").descending());

    }

    @Override
    public List<Comment> findByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null)
            return null;
        return commentRepository.findByUser(userName, Sort.by("id").descending());
    }

    @Override
    public List<Comment> findByArticleContent(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null)
            return null;
        return commentRepository.findByArticleContent(articleContent, Sort.by("id").descending());
    }

    @Override
    public Object addComment(CommentDTO commentDTO) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        if (configurationFlags.isComments()) {
            String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            if (username.equals(ANONYMOUS_USER))
                return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3005.getValue());
            else
            {
                ArticleContent articleContent = articleContentRepository.findById(commentDTO.getArticleId()).orElse(null);
                if (commentDTO.getContent().isEmpty()) {
                    return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3004.getValue());
                }
                if (articleContent == null) {
                    return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3016.getValue());
                }
                if(!articleContent.isCommentsAllowed()) {
                    return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3031.getValue());
                }
                commentRepository.save(new Comment()
                        .setContent(commentDTO.getContent())
                        .setUser(userRepository.findByUserName(username))
                        .setArticleContent(articleContent));
                return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
            }
        }
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_4008.getValue());
    }

    @Override
    public Object editCommentByUser(long commentId, String commentContent) {
        String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (username.equals(ANONYMOUS_USER))
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3005.getValue());
        User principalUser = userRepository.findByUserName(username);
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (commentContent.isEmpty()) {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3004.getValue());
        }
        if (comment == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3019.getValue());
        Role principalRole = principalUser.getRoles().stream().findAny().orElse(null);
        Collection<Privilege> principalPrivileges = roleRepository.findByName(principalRole.getName()).getPrivileges();
        Privilege editCommentPrivilege = privilegeRepository.findByName(PrivilegeName.EDIT_COMMENT.getPrivilegeName());
        if (principalPrivileges.contains(editCommentPrivilege) || principalUser.getId() == comment.getUser().getId()) {
            comment.setContent(commentContent);
            commentRepository.save(comment);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        } else {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_4003.getValue());
        }
    }

    @Override
    public Object editCommentInCMS(Long id, String content) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3019.getValue());
        if (content.isEmpty())
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3004.getValue());
        comment.setContent(content);
        commentRepository.save(comment);
        return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
    }

    @Override
    public Object removeOwnComment(long id) {
        String username = PrincipalUtils.getPrincipalUserName(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (username.equals(ANONYMOUS_USER))
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3005.getValue());
        User principalUser = userRepository.findByUserName(username);
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null)
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3019.getValue());
        Role principalRole = principalUser.getRoles().stream().findAny().orElse(null);
        Collection<Privilege> principalPrivileges = roleRepository.findByName(principalRole.getName()).getPrivileges();
        User userComment = comment.getUser();
        if (principalPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_COMMENT.getPrivilegeName())) || principalUser.getId() == userComment.getId()) {
            commentRepository.delete(comment);
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_2001.getValue());
        } else {
            return ErrorProvidedDataHandlerUtils.getErrorProvidedDataHandler(Code.CODE_3036.getValue());
        }
    }
}
