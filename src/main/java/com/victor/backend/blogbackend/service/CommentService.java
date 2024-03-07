package com.victor.backend.blogbackend.service;

import com.victor.backend.blogbackend.api.model.CommentBody;
import com.victor.backend.blogbackend.api.model.CommentResponseBody;
import com.victor.backend.blogbackend.api.model.EditCommentBody;
import com.victor.backend.blogbackend.api.model.LikeResponseBody;
import com.victor.backend.blogbackend.exception.*;
import com.victor.backend.blogbackend.model.Comment;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.model.Post;
import com.victor.backend.blogbackend.model.dao.CommentDAO;
import com.victor.backend.blogbackend.model.dao.LocalUserDAO;
import com.victor.backend.blogbackend.model.dao.PostDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentDAO commentDAO;

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private JWTService jwt;

    @Autowired
    private PostDAO postDAO;


    public CommentResponseBody makeComment(CommentBody commentBody, int postId, String token) throws PostNotFoundException, UserNotFoundException {
        String username = jwt.getUsername(token);
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        Optional<Post> opPost = postDAO.findById((long) postId);
        if (opPost.isEmpty()) {
            throw new PostNotFoundException();
        }
        if (opUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        LocalUser user = opUser.get();
        Post post = opPost.get();
        Comment comment = new Comment();
        comment.setAuthor(user);
        comment.setPost(post);
        comment.setContent(commentBody.getContent());
        comment.setTime(LocalDateTime.now());
        comment.setEdited(false);
        return new CommentResponseBody(commentDAO.save(comment));
    }

    public List<CommentResponseBody> postComments(int postId) {
        List<Comment> commentList = commentDAO.findAll();
        return commentList.stream().map(x -> new CommentResponseBody(x)).toList();
    }

    public List<CommentResponseBody> findUsersComments(String username) throws UserDontHaveCommentYetException {
        Optional<List<Comment>> opCommentList = commentDAO.findByAuthor_UsernameOrderByTimeDesc(username);
        if (opCommentList.isEmpty()) {
            throw new UserDontHaveCommentYetException();
        }

        List<Comment> posts = opCommentList.get();
        return posts.stream().map(x -> new CommentResponseBody(x)).toList();
    }

    public CommentResponseBody editComment(EditCommentBody editCommentBody, String token, long commentId) throws UserNotFoundException, PostNotFoundException, UserPermissionDenied {
        String username = jwt.getUsername(token);
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        Optional<Comment> opComment = commentDAO.findById(commentId);
        if (opComment.isEmpty()) {
            throw new PostNotFoundException();
        }
        LocalUser user = opUser.get();
        Comment comment = opComment.get();

        if (!user.equals(comment.getAuthor())) {
            throw new UserPermissionDenied();
        }

        if (editCommentBody.getContent() != null) {
            comment.setContent(editCommentBody.getContent());
        }
        comment.setEdited(true);
        commentDAO.save(comment);
        return new CommentResponseBody(comment);
    }

    public void deleteComment(String token, long commentId) throws UserNotFoundException, PostNotFoundException, UserPermissionDenied {
        String username = jwt.getUsername(token);
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        Optional<Comment> opComment = commentDAO.findById(commentId);
        if (opComment.isEmpty()) {
            throw new PostNotFoundException();
        }
        LocalUser user = opUser.get();
        Comment comment = opComment.get();

        if (!user.equals(comment.getAuthor())) {
            throw new UserPermissionDenied();
        }

        commentDAO.delete(comment);
    }

    public LikeResponseBody likeComment(String token, long commentId) throws UserNotFoundException, PostNotFoundException {
        String username = jwt.getUsername(token);
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        Optional<Comment> opComment = commentDAO.findById(commentId);
        if (opComment.isEmpty()) {
            throw new PostNotFoundException();
        }
        LocalUser user = opUser.get();
        Comment comment = opComment.get();

        comment.getCommentLikes().stream().filter(authorLike -> authorLike.equals(user)).findFirst().ifPresentOrElse(
                foundLike -> comment.getCommentLikes().remove(foundLike),
                () -> comment.getCommentLikes().add(user)
        );

        commentDAO.save(comment);
        return new LikeResponseBody(comment.getCommentLikes().size());
    }

    public List<CommentResponseBody> findUsersLikedComments(String username) throws UserDontHaveLikedCommentException {
        Optional<List<Comment>> commentLikedList = commentDAO.findByCommentLikes_UsernameOrderByTimeDesc(username);
        if (commentLikedList.isEmpty()) {
            throw new UserDontHaveLikedCommentException();
        }
        List<Comment> likedPosts = commentLikedList.get();
        return likedPosts.stream().map(x -> new CommentResponseBody(x)).toList();
    }
}
