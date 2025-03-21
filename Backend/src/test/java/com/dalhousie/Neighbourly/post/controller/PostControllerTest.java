package com.dalhousie.Neighbourly.post.controller;

import com.dalhousie.Neighbourly.post.dto.PostRequest;
import com.dalhousie.Neighbourly.post.dto.PostResponse;
import com.dalhousie.Neighbourly.post.service.PostService;
import com.dalhousie.Neighbourly.util.CustomResponseBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @Test
    void testCreatePost_Success() {
        // Given
        PostRequest request = new PostRequest(101, 202, "General", "Sample post");
        PostResponse mockResponse = new PostResponse(1, 101, 202, "General", "Sample post", "2024-03-06T10:00:00", false);
        when(postService.createPost(request)).thenReturn(mockResponse);

        // When
        ResponseEntity<CustomResponseBody<PostResponse>> responseEntity = postController.createPost(request);

        // Then
        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(CustomResponseBody.Result.SUCCESS, responseEntity.getBody().result());
        assertEquals("Post created successfully", responseEntity.getBody().message());
        assertEquals(1, responseEntity.getBody().data().getPostId());

        verify(postService, times(1)).createPost(request);
    }

    @Test
    void testCreatePost_Failure() {
        // Given
        PostRequest request = new PostRequest(101, 202, "General", "Sample post");
        when(postService.createPost(request)).thenThrow(new RuntimeException("Error creating post"));

        // When
        ResponseEntity<CustomResponseBody<PostResponse>> responseEntity = postController.createPost(request);

        // Then
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(CustomResponseBody.Result.FAILURE, responseEntity.getBody().result());
        assertEquals("Error creating post", responseEntity.getBody().message());
        assertNull(responseEntity.getBody().data());

        verify(postService, times(1)).createPost(request);
    }

    @Test
    void testUpdatePostContent_Success() {
        // Given
        int postId = 1;
        String newContent = "Updated content";
        PostResponse mockResponse = new PostResponse(postId, 101, 202, "General", newContent, "2024-03-06T10:00:00", false);
        when(postService.updatePostContent(postId, newContent)).thenReturn(mockResponse);

        // When
        ResponseEntity<CustomResponseBody<PostResponse>> responseEntity = postController.updatePostContent(postId, newContent);

        // Then
        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Post updated successfully", responseEntity.getBody().message());
        assertEquals(newContent, responseEntity.getBody().data().getPostContent());

        verify(postService, times(1)).updatePostContent(postId, newContent);
    }

    @Test
    void testApprovePost_Success() {
        // Given
        int postId = 1;
        PostResponse mockResponse = new PostResponse(postId, 101, 202, "General", "Sample post", "2024-03-06T10:00:00", true);
        when(postService.approvePost(postId)).thenReturn(mockResponse);

        // When
        ResponseEntity<CustomResponseBody<PostResponse>> responseEntity = postController.approvePost(postId);

        // Then
        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(CustomResponseBody.Result.SUCCESS, responseEntity.getBody().result());
        assertEquals("Post approved successfully", responseEntity.getBody().message());
        assertTrue(responseEntity.getBody().data().isApproved());

        verify(postService, times(1)).approvePost(postId);
    }

    @Test
    void testApprovePost_Failure() {
        // Given
        int postId = 2;
        when(postService.approvePost(postId)).thenThrow(new RuntimeException("Post not found"));

        // When
        ResponseEntity<CustomResponseBody<PostResponse>> responseEntity = postController.approvePost(postId);

        // Then
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(CustomResponseBody.Result.FAILURE, responseEntity.getBody().result());
        assertEquals("Post not found", responseEntity.getBody().message());
        assertNull(responseEntity.getBody().data());

        verify(postService, times(1)).approvePost(postId);
    }

    @Test
    void testGetApprovedPosts_Success() {
        // Given
        List<PostResponse> mockPosts = Arrays.asList(
                new PostResponse(1, 101, 202, "General", "Sample post 1", "2024-03-06T10:00:00", true),
                new PostResponse(2, 102, 202, "General", "Sample post 2", "2024-03-07T11:00:00", true)
        );
        when(postService.getApprovedPostsByNeighbourhood(202)).thenReturn(mockPosts);

        // When
        ResponseEntity<CustomResponseBody<List<PostResponse>>> responseEntity = postController.getApprovedPosts(202);

        // Then
        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(CustomResponseBody.Result.SUCCESS, responseEntity.getBody().result());
        assertEquals("Approved posts retrieved successfully", responseEntity.getBody().message());
        assertEquals(2, responseEntity.getBody().data().size());

        verify(postService, times(1)).getApprovedPostsByNeighbourhood(202);
    }


    @Test
    void testGetDisapprovedPosts_Success() {
        // Given
        List<PostResponse> mockPosts = Arrays.asList(
                new PostResponse(1, 101, 202, "General", "Sample post 1", "2024-03-06T10:00:00", false),
                new PostResponse(2, 102, 202, "General", "Sample post 2", "2024-03-07T11:00:00", false)
        );
        when(postService.getDisapprovedPostsByNeighbourhood(202)).thenReturn(mockPosts);

        // When
        ResponseEntity<CustomResponseBody<List<PostResponse>>> responseEntity = postController.getDisapprovedPostsbyNeighbourhood(202);

        // Then
        assertEquals(OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(CustomResponseBody.Result.SUCCESS, responseEntity.getBody().result());
        assertEquals("Disapproved posts retrieved successfully", responseEntity.getBody().message());
        assertEquals(2, responseEntity.getBody().data().size());

        verify(postService, times(1)).getDisapprovedPostsByNeighbourhood(202);
    }

    @Test
    void testGetDisapprovedPosts_Failure() {
        // Given
        when(postService.getDisapprovedPostsByNeighbourhood(202)).thenThrow(new RuntimeException("Neighbourhood not found"));

        // When
        ResponseEntity<CustomResponseBody<List<PostResponse>>> responseEntity = postController.getDisapprovedPostsbyNeighbourhood(202);

        // Then
        assertEquals(BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(CustomResponseBody.Result.FAILURE, responseEntity.getBody().result());
        assertEquals("Neighbourhood not found", responseEntity.getBody().message());
        assertNull(responseEntity.getBody().data());

        verify(postService, times(1)).getDisapprovedPostsByNeighbourhood(202);
    }
}
