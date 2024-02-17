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
import com.cms.pp.cms.pp.model.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.model.entity.Privilege;
import com.cms.pp.cms.pp.repository.PrivilegeRepository;
import com.cms.pp.cms.pp.model.entity.Role;
import com.cms.pp.cms.pp.repository.RoleRepository;
import com.cms.pp.cms.pp.model.entity.User;
import com.cms.pp.cms.pp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class CommentService {
    private static final String ANONYMOUS_USER = "anonymousUser";
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ArticleContentRepository articleContentRepository;
    @Autowired
    ConfigurationFlagsRepository configurationFlagsRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PrivilegeRepository privilegeRepository;

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
        return commentRepository.findByUser(user, Sort.by("id").descending());

    }

    public List<Comment> findByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            return null;
        }
        return commentRepository.findByUser(userName, Sort.by("id").descending());
    }

    public List<Comment> findByArticleContent(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) {
            return null;
        }
        return commentRepository.findByArticleContent(articleContent, Sort.by("id").descending());
    }


    public Object addComment(CommentDTO commentDTO) {
        ConfigurationFlags configurationFlags = configurationFlagsRepository.getById(1);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (configurationFlags.isComments()) {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String username = "";
            if (principal instanceof UserDetails) {
                username = ((UserDetails)principal).getUsername();
            }
            else {
                username = principal.toString();
            }
            if (username.equals(ANONYMOUS_USER)) {
                errorProvidedDataHandler.setError(Code.CODE_3005.getValue());
                return errorProvidedDataHandler;
            }
            else
            {
                ArticleContent articleContent = articleContentRepository.findById(commentDTO.getArticleId()).orElse(null);
                if (commentDTO.getContent().isEmpty()) {
                    errorProvidedDataHandler.setError(Code.CODE_3004.getValue());
                    return errorProvidedDataHandler;
                }
                if (articleContent == null) {
                    errorProvidedDataHandler.setError(Code.CODE_3016.getValue());
                    return errorProvidedDataHandler;
                }
                if(!articleContent.isCommentsAllowed()) {
                    errorProvidedDataHandler.setError(Code.CODE_3031.getValue());
                    return errorProvidedDataHandler;
                }
                Comment comment = new Comment();
                comment.setContent(commentDTO.getContent());
                comment.setUser(userRepository.findByUserName(username));
                comment.setArticleContent(articleContent);
                errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
                commentRepository.save(comment);
                return errorProvidedDataHandler;
            }
        }
        else
        {
            errorProvidedDataHandler.setError(Code.CODE_4008.getValue());
            return errorProvidedDataHandler;
        }

    }

    public Object editCommentByUser(long commentId, String commentContent) {
        boolean canEdit = false;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (username.equals(ANONYMOUS_USER)) {
            errorProvidedDataHandler.setError(Code.CODE_3005.getValue());
            return errorProvidedDataHandler;
        }
        else {
            User principalUser = userRepository.findByUserName(username);
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment == null) {
                errorProvidedDataHandler.setError(Code.CODE_3019.getValue());
                return errorProvidedDataHandler;
            }
            Role principalRole = principalUser.getRoles().stream().findAny().orElse(null);
            Collection<Privilege> principalPrivileges = roleRepository.findByName(principalRole.getName()).getPrivileges();
            if (principalPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_COMMENT.getPrivilegeName())))
            {
                canEdit = true;
            }
            if (!canEdit && principalUser.getId() != comment.getUser().getId()) {
                errorProvidedDataHandler.setError(Code.CODE_4003.getValue());
                return errorProvidedDataHandler;
            }
            else
            {
                canEdit = true;
            }
            if (commentContent.isEmpty()) {
                errorProvidedDataHandler.setError(Code.CODE_3004.getValue());
                return errorProvidedDataHandler;
            }
            if (canEdit)
            {
                comment.setContent(commentContent);
                errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
                commentRepository.save(comment);
                return errorProvidedDataHandler;
            }
            else {
                errorProvidedDataHandler.setError(Code.CODE_4003.getValue());
                return errorProvidedDataHandler;
            }

        }
    }

    public Object editCommentInCMS(Long id, String content) {
        Comment comment = commentRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (comment == null) {
            errorProvidedDataHandler.setError(Code.CODE_3019.getValue());
            return errorProvidedDataHandler;
        }
        if (content.isEmpty()) {
            errorProvidedDataHandler.setError(Code.CODE_3004.getValue());
            return errorProvidedDataHandler;
        }
        comment.setContent(content);
        errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
        commentRepository.save(comment);
        return errorProvidedDataHandler;
    }

    public Object removeOwnComment(long id) {
        boolean canEdit = false;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (username.equals(ANONYMOUS_USER)) {
            errorProvidedDataHandler.setError(Code.CODE_3005.getValue());
            return errorProvidedDataHandler;
        }
        User principalUser = userRepository.findByUserName(username);
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null) {
            errorProvidedDataHandler.setError(Code.CODE_3019.getValue());
            return errorProvidedDataHandler;
        }
        Role principalRole = principalUser.getRoles().stream().findAny().orElse(null);
        Collection<Privilege> principalPrivileges = roleRepository.findByName(principalRole.getName()).getPrivileges();
        User userComment = comment.getUser();
        if (principalPrivileges.contains(privilegeRepository.findByName(PrivilegeName.EDIT_COMMENT.getPrivilegeName())))
            canEdit = true;

        if (principalUser.getId() == userComment.getId())
            canEdit = true;

        if (canEdit)
        {
            commentRepository.delete(comment);
            errorProvidedDataHandler.setError(Code.CODE_2001.getValue());
            return errorProvidedDataHandler;
        }
        else {
            errorProvidedDataHandler.setError(Code.CODE_3036.getValue());
            return errorProvidedDataHandler;
        }

    }

}
