package com.dalhousie.Neighbourly.post.service;

import com.dalhousie.Neighbourly.neighbourhood.service.NeighbourhoodService;
import com.dalhousie.Neighbourly.post.dto.PostRequest;
import com.dalhousie.Neighbourly.post.dto.PostResponse;
import com.dalhousie.Neighbourly.post.entity.Post;
import com.dalhousie.Neighbourly.post.repository.PostRepository;
import com.dalhousie.Neighbourly.user.service.UserService;

import lombok.RequiredArgsConstructor;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserService userService;
    private final NeighbourhoodService neighbourhoodService;

    public PostResponse createPost(PostRequest postRequest) {
        // Check if user is present
        if (!userService.isUserPresent(postRequest.getUserId())) {
            throw new RuntimeException("User not found");
        }

        if(!userService.isUserPartOfanyCommunity(postRequest.getUserId())){
            throw new RuntimeException("User is not part of any community");
        }

     
        if (!neighbourhoodService.isNeighbourhoodExist(postRequest.getNeighbourhoodId())) {
            throw new RuntimeException("Neighborhood not found");
        }

        // Create the Post entity and save
        Post post = new Post();
        post.setUserId(postRequest.getUserId());
        post.setNeighbourhoodId(postRequest.getNeighbourhoodId());
        post.setPostType(postRequest.getPostType());
        post.setPostContent(postRequest.getPostContent());
        post.setDateTime(LocalDateTime.now());
        post.setApproved(false);

        post = postRepository.save(post);
        return mapToResponse(post);
    }

  
    public PostResponse updatePostContent(int postId, String content) {
        // Check if the content is in JSON format and extract postContent if it's valid JSON
        String postContent = content;
        try {
            JSONObject json = new JSONObject(content);
            if (json.has("postContent")) {
                postContent = json.getString("postContent"); // Extract postContent from JSON if present
            }
        } catch (JSONException e) {
            // If it's not valid JSON, we will simply use the original content
            // Log the error to help track invalid JSON inputs
            System.err.println("Invalid JSON content: " + content); // Log the invalid JSON content for debugging
        }
    
        // Find the post by its ID and update the content
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setPostContent(postContent);  // Set the post content (either extracted or original)
        post = postRepository.save(post);   // Save the updated post
    
        return mapToResponse(post);  // Return the response after saving
    }

    public List<PostResponse> getApprovedPostsByNeighbourhood(int neighbourhoodId) {
        if (!neighbourhoodService.isNeighbourhoodExist(neighbourhoodId)) {
            throw new RuntimeException("Neighbourhood not found");
        }
        return postRepository.findByNeighbourhoodIdAndApproved(neighbourhoodId, true)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PostResponse mapToResponse(Post post) {
        return new PostResponse(
                post.getPostId(),
                post.getUserId(),
                post.getNeighbourhoodId(),
                post.getPostType(),
                post.getPostContent(),
                post.getDateTime() != null ? post.getDateTime().toString() : null, 
                post.isApproved()
        );
    }

    public PostResponse approvePost(int postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
        post.setApproved(true);
        post = postRepository.save(post);
        return mapToResponse(post);
    }

    public void deletePost(int postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Post not found");
        }
        postRepository.deleteById(postId);
    }

    public List<PostResponse> getDisapprovedPostsByNeighbourhood(int neighbourhoodId) {
        if (!neighbourhoodService.isNeighbourhoodExist(neighbourhoodId)) {
            throw new RuntimeException("Neighbourhood not found");
        }
        return postRepository.findByNeighbourhoodIdAndApproved(neighbourhoodId, false)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
        
    }
    
    
}
