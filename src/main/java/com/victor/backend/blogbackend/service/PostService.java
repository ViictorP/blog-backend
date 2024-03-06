package com.victor.backend.blogbackend.service;

import com.victor.backend.blogbackend.api.model.EditPostBody;
import com.victor.backend.blogbackend.api.model.LikeResponseBody;
import com.victor.backend.blogbackend.api.model.PostBody;
import com.victor.backend.blogbackend.api.model.PostResponseBody;
import com.victor.backend.blogbackend.exception.PostNotFoundException;
import com.victor.backend.blogbackend.exception.UserNotFoundException;
import com.victor.backend.blogbackend.exception.UserDontHavePostYetException;
import com.victor.backend.blogbackend.exception.UserPermissionDenied;
import com.victor.backend.blogbackend.model.LocalUser;
import com.victor.backend.blogbackend.model.Post;
import com.victor.backend.blogbackend.model.dao.LocalUserDAO;
import com.victor.backend.blogbackend.model.dao.PostDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostDAO postDAO;

    @Autowired
    private LocalUserDAO localUserDAO;

    @Autowired
    private JWTService jwt;

    public PostResponseBody createPost(PostBody postBody, String token) throws UserNotFoundException {
        String username = jwt.getUsername(token);
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException();
        }

        Post post = new Post();
        post.setTitle(postBody.getTitle());
        post.setContent(postBody.getContent());
        LocalUser user = opUser.get();
        post.setAuthor(user);
        post.setEdited(false);
        post.setTime(LocalDateTime.now());
        postDAO.save(post);
        return new PostResponseBody(post);
    }

    public List<PostResponseBody> allPosts() {
        List<Post> postList = postDAO.OrderByTimeDesc();
        return postList.stream().map(x -> new PostResponseBody(x)).toList();
    }

    public List<PostResponseBody> findUsersPosts(String username) throws UserDontHavePostYetException {
        Optional<List<Post>> opPostList = postDAO.findByAuthor_UsernameOrderByTimeDesc(username);
        if (opPostList.isEmpty()) {
            throw new UserDontHavePostYetException();
        }

        List<Post> posts = opPostList.get();
        return posts.stream().map(x -> new PostResponseBody(x)).toList();
    }

    public PostResponseBody editPost(EditPostBody editPostBody, String token, long postId) throws UserNotFoundException, PostNotFoundException, UserPermissionDenied {
        String username = jwt.getUsername(token);
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        Optional<Post> opPost = postDAO.findById(postId);
        if (opPost.isEmpty()) {
            throw new PostNotFoundException();
        }
        LocalUser user = opUser.get();
        Post post = opPost.get();

        if (!user.equals(post.getAuthor())) {
            throw new UserPermissionDenied();
        }

        if (editPostBody.getTitle() != null) {
            post.setTitle(editPostBody.getTitle());
        }
        if (editPostBody.getContent() != null) {
            post.setContent(editPostBody.getContent());
        }
        post.setEdited(true);
        postDAO.save(post);
        return new PostResponseBody(post);
    }

    public void deletePost(String token, long postId) throws UserNotFoundException, PostNotFoundException, UserPermissionDenied {
        String username = jwt.getUsername(token);
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        Optional<Post> opPost = postDAO.findById(postId);
        if (opPost.isEmpty()) {
            throw new PostNotFoundException();
        }
        LocalUser user = opUser.get();
        Post post = opPost.get();

        if (!user.equals(post.getAuthor())) {
            throw new UserPermissionDenied();
        }

        postDAO.delete(post);
    }

    public LikeResponseBody likePost(String token, long postId) throws UserNotFoundException, PostNotFoundException {
        String username = jwt.getUsername(token);
        Optional<LocalUser> opUser = localUserDAO.findByUsernameIgnoreCase(username);
        if (opUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        Optional<Post> opPost = postDAO.findById(postId);
        if (opPost.isEmpty()) {
            throw new PostNotFoundException();
        }
        LocalUser user = opUser.get();
        Post post = opPost.get();

        post.getPostLikes().stream().filter(authorLike -> authorLike.equals(user)).findFirst().ifPresentOrElse(
                foundLike -> post.getPostLikes().remove(foundLike),
                () -> post.getPostLikes().add(user)
        );

        postDAO.save(post);
        return new LikeResponseBody(post.getPostLikes().size());
    }
}
