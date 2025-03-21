package com.dalhousie.Neighbourly.post.service;

import com.dalhousie.Neighbourly.neighbourhood.service.NeighbourhoodService;
import com.dalhousie.Neighbourly.post.dto.PostRequest;
import com.dalhousie.Neighbourly.post.dto.PostResponse;
import com.dalhousie.Neighbourly.post.entity.Post;
import com.dalhousie.Neighbourly.post.repository.PostRepository;
import com.dalhousie.Neighbourly.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class PostServiceImplTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserService userService;

    @Mock
    private NeighbourhoodService neighbourhoodService;

    @InjectMocks
    private PostServiceImpl postService;

    private PostRequest postRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postRequest = new PostRequest();
        postRequest.setUserId(1);
        postRequest.setNeighbourhoodId(1);
        postRequest.setPostType("General");
        postRequest.setPostContent("This is a post");
    }

    @Test
    void testCreatePost_UserNotFound() {
        // Arrange
        when(userService.isUserPresent(postRequest.getUserId())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.createPost(postRequest);
        });

        assertEquals("User not found", exception.getMessage());
        verify(userService).isUserPresent(postRequest.getUserId());
        verify(postRepository, never()).save(any());
    }

    @Test
    void testCreatePost_UserNotPartOfCommunity() {
        // Arrange
        when(userService.isUserPresent(postRequest.getUserId())).thenReturn(true);
        when(userService.isUserPartOfanyCommunity(postRequest.getUserId())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.createPost(postRequest);
        });

        assertEquals("User is not part of any community", exception.getMessage());
        verify(userService).isUserPresent(postRequest.getUserId());
        verify(userService).isUserPartOfanyCommunity(postRequest.getUserId());
        verify(postRepository, never()).save(any());
    }

    @Test
    void testCreatePost_NeighbourhoodNotFound() {
        // Arrange
        when(userService.isUserPresent(postRequest.getUserId())).thenReturn(true);
        when(userService.isUserPartOfanyCommunity(postRequest.getUserId())).thenReturn(true);
        when(neighbourhoodService.isNeighbourhoodExist(postRequest.getNeighbourhoodId())).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.createPost(postRequest);
        });

        assertEquals("Neighborhood not found", exception.getMessage());
        verify(userService).isUserPresent(postRequest.getUserId());
        verify(userService).isUserPartOfanyCommunity(postRequest.getUserId());
        verify(neighbourhoodService).isNeighbourhoodExist(postRequest.getNeighbourhoodId());
        verify(postRepository, never()).save(any());
    }

    @Test
    void testCreatePost_Success() {
        // Arrange
        when(userService.isUserPresent(postRequest.getUserId())).thenReturn(true);
        when(userService.isUserPartOfanyCommunity(postRequest.getUserId())).thenReturn(true);
        when(neighbourhoodService.isNeighbourhoodExist(postRequest.getNeighbourhoodId())).thenReturn(true);
        when(postRepository.save(any(Post.class))).thenReturn(new Post());

        // Act
        PostResponse response = postService.createPost(postRequest);

        // Assert
        assertNotNull(response);
        verify(userService).isUserPresent(postRequest.getUserId());
        verify(userService).isUserPartOfanyCommunity(postRequest.getUserId());
        verify(neighbourhoodService).isNeighbourhoodExist(postRequest.getNeighbourhoodId());
        verify(postRepository).save(any(Post.class));
    }

    @Test
    void testUpdatePostContent_PostNotFound() {
        // Arrange
        int postId = 1;
        String newContent = "Updated content";
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.updatePostContent(postId, newContent);
        });

        assertEquals("Post not found", exception.getMessage());
        verify(postRepository).findById(postId);
    }

    @Test
    void testUpdatePostContent_Success() {
        // Arrange
        int postId = 1;
        String newContent = "Updated content";
        Post post = new Post();
        post.setPostId(postId);
        post.setPostContent("Old content");
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        // Act
        PostResponse response = postService.updatePostContent(postId, newContent);

        // Assert
        assertNotNull(response);
        assertEquals(newContent, response.getPostContent());
        verify(postRepository).findById(postId);
        verify(postRepository).save(post);
    }

   @Test
    void testGetApprovedPosts_Success() {
        // Given (created two post in the same neighbourhood)
        int neighbourhoodId = 1;
        int postId = 1;
        Post post = new Post();
        post.setPostId(postId);
        post.setApproved(false);

        postId = 2;
        Post post2 = new Post();
        post2.setPostId(postId);
        post2.setApproved(false);

        List<Post> mockPosts = Arrays.asList(post, post2);

        when(neighbourhoodService.isNeighbourhoodExist(neighbourhoodId)).thenReturn(true);
        when(postRepository.findByNeighbourhoodIdAndApproved(neighbourhoodId, true)).thenReturn(mockPosts);

        // When
        List<PostResponse> response = postService.getApprovedPostsByNeighbourhood(neighbourhoodId);

        // Then
        assertEquals(2, response.size());
        verify(postRepository, times(1)).findByNeighbourhoodIdAndApproved(neighbourhoodId, true);
        verify(neighbourhoodService, times(1)).isNeighbourhoodExist(neighbourhoodId);
    }

    @Test
    void testGetApprovedPosts_EmptyList() {
        // Given
        int neighbourhoodId = 2;
        when(neighbourhoodService.isNeighbourhoodExist(neighbourhoodId)).thenReturn(true);
        when(postRepository.findByNeighbourhoodIdAndApproved(neighbourhoodId, true)).thenReturn(Collections.emptyList());

        // When
        List<PostResponse> response = postService.getApprovedPostsByNeighbourhood(neighbourhoodId);

        // Then
        assertNotNull(response);
        assertTrue(response.isEmpty());

        verify(postRepository, times(1)).findByNeighbourhoodIdAndApproved(neighbourhoodId, true);
        verify(neighbourhoodService, times(1)).isNeighbourhoodExist(neighbourhoodId);
    }

    @Test
    void testGetApprovedPosts_NeighbourhoodNotFound() {
        // Given
        int neighbourhoodId = 3;
        when(neighbourhoodService.isNeighbourhoodExist(neighbourhoodId)).thenReturn(false);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> postService.getApprovedPostsByNeighbourhood(neighbourhoodId));

        assertEquals("Neighbourhood not found", exception.getMessage());
        verify(neighbourhoodService, times(1)).isNeighbourhoodExist(neighbourhoodId);
        verify(postRepository, never()).findByNeighbourhoodIdAndApproved(anyInt(), anyBoolean());
    }


    @Test
    void testApprovePost_PostNotFound() {
        // Arrange
        int postId = 1;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.approvePost(postId);
        });

        assertEquals("Post not found", exception.getMessage());
        verify(postRepository).findById(postId);
    }

    @Test   
    void testApprovePost_Success() {
        // Arrange
        int postId = 1;
        Post post = new Post();
        post.setPostId(postId);
        post.setApproved(false);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        // Act
        PostResponse response = postService.approvePost(postId);

        // Assert
        assertNotNull(response);
        assertTrue(response.isApproved());
        verify(postRepository).findById(postId);
        verify(postRepository).save(post);
    }

    @Test
    void testGetDisapprovedPostsByNeighbourhood_NeighbourhoodNotFound() {
        // Arrange
        int neighbourhoodId = 1;
        when(neighbourhoodService.isNeighbourhoodExist(neighbourhoodId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.getDisapprovedPostsByNeighbourhood(neighbourhoodId);
        });

        assertEquals("Neighbourhood not found", exception.getMessage());
        verify(neighbourhoodService).isNeighbourhoodExist(neighbourhoodId);
        verify(postRepository, never()).findByNeighbourhoodIdAndApproved(neighbourhoodId, false);
    }

    @Test
    void testGetDisapprovedPostsByNeighbourhood_Success() {
        // Arrange
        int neighbourhoodId = 1;
        Post post1 = new Post();
        post1.setApproved(false);
        Post post2 = new Post();
        post2.setApproved(false);
        when(neighbourhoodService.isNeighbourhoodExist(neighbourhoodId)).thenReturn(true);
        when(postRepository.findByNeighbourhoodIdAndApproved(neighbourhoodId, false)).thenReturn(Arrays.asList(post1, post2));

        // Act
        List<PostResponse> responses = postService.getDisapprovedPostsByNeighbourhood(neighbourhoodId);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(neighbourhoodService).isNeighbourhoodExist(neighbourhoodId);
        verify(postRepository).findByNeighbourhoodIdAndApproved(neighbourhoodId, false);
    }

    @Test
    void deletePost_PostNotFound() {
        // Arrange
        int postId = 1;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            postService.deletePost(postId);
        });

        assertEquals("Post not found", exception.getMessage());
        verify(postRepository).findById(postId);
    }

    @Test
    void deletePost_Success() {
        // Arrange
        int postId = 1;
        Post post = new Post();
        post.setPostId(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // Act
        postService.deletePost(postId);

        // Assert
        verify(postRepository).findById(postId);
        verify(postRepository).delete(post);
    }


    
}
