package com.cms.pp.cms.pp.Comment;

import com.cms.pp.cms.pp.Article.ArticleContent;
import com.cms.pp.cms.pp.Article.ArticleContentRepository;
import com.cms.pp.cms.pp.ConfigurationFlags.ConfigurationFlags;
import com.cms.pp.cms.pp.ConfigurationFlags.ConfigurationFlagsRepository;
import com.cms.pp.cms.pp.ErrorProvidedDataHandler;
import com.cms.pp.cms.pp.Priviliges.Privilege;
import com.cms.pp.cms.pp.Priviliges.PrivilegeRepository;
import com.cms.pp.cms.pp.Role.Role;
import com.cms.pp.cms.pp.Role.RoleRepository;
import com.cms.pp.cms.pp.user.User;
import com.cms.pp.cms.pp.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
        return commentRepository.findByUser(user);

    }

    public List<Comment> findByUserName(String userName) {
        User user = userRepository.findByUserName(userName);
        if (user == null) {
            return null;
        }
        return commentRepository.findByUser(userName);
    }

    public List<Comment> findByArticleContent(int id) {
        ArticleContent articleContent = articleContentRepository.findById(id).orElse(null);
        if (articleContent == null) {
            return null;
        }
        return commentRepository.findByArticleContent(articleContent);
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
            if (username.equals("anonymousUser")) {
                errorProvidedDataHandler.setError("3005");//user not logged in
                return errorProvidedDataHandler;
            }
            else
            {
                ArticleContent articleContent = articleContentRepository.findById(commentDTO.getArticleId()).orElse(null);
                if (commentDTO.getContent().equals("")) {
                    errorProvidedDataHandler.setError("3004");//content empty
                    return errorProvidedDataHandler;
                }
                if (articleContent == null) {
                    errorProvidedDataHandler.setError("3016");//article not found
                    return errorProvidedDataHandler;
                }
                if(!articleContent.isCommentsAllowed()) {
                    errorProvidedDataHandler.setError("3031"); //comments are turned off in this article
                    return errorProvidedDataHandler;
                }
                Comment comment = new Comment();
                comment.setContent(commentDTO.getContent());
                comment.setUser(userRepository.findByUserName(username));
                comment.setArticleContent(articleContent);
                errorProvidedDataHandler.setError("2001");//success
                commentRepository.save(comment);
                return errorProvidedDataHandler;
            }
        }
        else
        {
            errorProvidedDataHandler.setError("4008");//comments turned off
            return errorProvidedDataHandler;
        }

    }

    public Object editCommentByUser(long commentId, String commentContent) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (principal instanceof UserDetails) {
            username = ((UserDetails)principal).getUsername();
        }
        else {
            username = principal.toString();
        }
        if (username.equals("anonymousUser")) {
            errorProvidedDataHandler.setError("3005");
            return errorProvidedDataHandler;
        }
        else {
            User user = userRepository.findByUserName(username);
            Comment comment = commentRepository.findById(commentId).orElse(null);
            if (comment == null) {
                errorProvidedDataHandler.setError("3019"); //comment not found
                return errorProvidedDataHandler;
            }
            if (!(user.getId() == comment.getUser().getId())) {
                errorProvidedDataHandler.setError("4003");
                return errorProvidedDataHandler;
            }
            if (commentContent.equals("")) {
                errorProvidedDataHandler.setError("3004");
                return errorProvidedDataHandler;
            }
            comment.setContent(commentContent);
            errorProvidedDataHandler.setError("2001");
            commentRepository.save(comment);
            return errorProvidedDataHandler;
        }
    }

    public Object editCommentInCMS(Long id, String content) {
        Comment comment = commentRepository.findById(id).orElse(null);
        ErrorProvidedDataHandler errorProvidedDataHandler = new ErrorProvidedDataHandler();
        if (comment == null) {
            errorProvidedDataHandler.setError("3019"); //comment not found
            return errorProvidedDataHandler;
        }
        if (content.equals("")) {
            errorProvidedDataHandler.setError("3004"); //content empty
            return errorProvidedDataHandler;
        }
        comment.setContent(content);
        errorProvidedDataHandler.setError("2001");
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
        if (username.equals("anonymousUser")) {
            errorProvidedDataHandler.setError("3005");
            return errorProvidedDataHandler;
        }
        User principalUser = userRepository.findByUserName(username);
        Comment comment = commentRepository.findById(id).orElse(null);
        Role principalRole = principalUser.getRoles().stream().findAny().orElse(null);
        Collection<Privilege> principalPrivileges = roleRepository.findByName(principalRole.getName()).getPrivileges();
        User userComment = comment.getUser();
        if (principalPrivileges.contains(privilegeRepository.findByName("EDIT_COMMENT")))
        {
            canEdit = true;
        }

        if (principalUser.getId() == userComment.getId())
        {
            canEdit = true;
        }
        System.out.println(principalPrivileges);

        if (comment == null) {
            errorProvidedDataHandler.setError("3019"); //comment not found
            return errorProvidedDataHandler;
        }

        if (canEdit)
        {
            commentRepository.delete(comment);
            errorProvidedDataHandler.setError("2001");
            return errorProvidedDataHandler;
        }
        else {
            errorProvidedDataHandler.setError("3036");
            return errorProvidedDataHandler;
        }

    }

}
