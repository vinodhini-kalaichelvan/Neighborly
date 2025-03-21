import React, { useState, useEffect } from "react";
import axios from "axios";
import {
  Users,
  Plus,
  Phone,
  Mail,
  Tag,
  Clock,
  Edit,
  Trash,
  ArrowLeft,
} from "lucide-react";
import { Link, useNavigate } from "react-router-dom";

const ViewPosts = () => {
  const [requests, setRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [flashMessage, setFlashMessage] = useState(null); // State for flash message
  const [flashType, setFlashType] = useState("success"); // State for flash message type (success or error)

  // local storage
  const neighborhoodId = localStorage.getItem("neighbourhoodId");
  const currentUserId = localStorage.getItem("userId"); // Get logged-in user ID
  const token = localStorage.getItem('token');
  const navigate = useNavigate(); // Hook to navigate programmatically

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        if (neighborhoodId) {
          const response = await axios.get(
            `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_APPROVED_POSTS_ENDPOINT}/${neighborhoodId}`,
            {
              headers: {
                Authorization: `Bearer ${token}`
              }
            }
          );

          if (response.data.result === "SUCCESS") {
            setRequests(response.data.data);
          } else {
            setError(response.data.message);
          }
        } else {
          setError("Neighborhood not found!");
        }
      } catch (err) {
        console.error("Error fetching requests:", err);
        setError(
          err.response?.data?.message || "Failed to load requests. Please try again."
        );
      } finally {
        setLoading(false);
      }
    };

    fetchRequests();
  }, [neighborhoodId]);

  const formatDate = (dateString) => {
    if (!dateString) return "Unknown date";
    const date = new Date(dateString);
    if (isNaN(date)) return "Invalid date";

    return new Intl.DateTimeFormat("en-US", {
      month: "short",
      day: "numeric",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    }).format(date);
  };

  // Handle the delete action
  const handleDelete = async (id) => {
    try {
      const response = await axios.delete(
        `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_DELETE_POST_ENDPOINT}/${id}`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      if (response.data.result === "SUCCESS") {
        // Remove the post from the state (view) immediately
        setRequests((prevRequests) => prevRequests.filter((request) => request.postId !== id));
        setFlashMessage("Post deleted successfully!"); // Set success flash message
        setFlashType("success"); // Set flash type to success
      } else {
        setFlashMessage("Failed to delete post. Please try again."); // Set error flash message
        setFlashType("error"); // Set flash type to error
      }
    } catch (err) {
      console.error("Error deleting post:", err);
      setFlashMessage(
        err.response?.data?.message || "Failed to delete post. Please try again."
      );
      setFlashType("error"); // Set flash type to error
    } finally {
      setTimeout(() => setFlashMessage(null), 3000); // Hide flash message after 3 seconds
    }
  };

  // Handle the edit action (navigate to edit page)
  const handleEdit = (id) => {
    navigate(`/editpost/${id}`);
  };

  return (
    <div className="min-h-screen bg-blue-50">
      {/* Header */}
      <header className="bg-white text-[#4873AB] p-4 shadow-md">
        <div className="container mx-auto flex justify-between items-center">
          <div className="flex items-center space-x-2">
            <Link to="/" className="hover:bg-gray-100 p-1 rounded-lg">
              <Users className="h-7 w-7" />
            </Link>
            <h1 className="text-2xl font-bold">Neighborly</h1>
          </div>
          <Link to="/post">
            <button className="flex items-center space-x-1 bg-[#4873AB] text-white px-3 py-2 rounded-lg hover:bg-blue-600 transition">
              <Plus className="h-4 w-4" />
              <span>Post New Request</span>
            </button>
          </Link>
        </div>
      </header>

      {/* Flash Message */}
      {flashMessage && (
        <div
          className={`fixed top-4 right-4 text-white px-4 py-2 rounded-lg shadow-md ${
            flashType === "success" ? "bg-green-500" : "bg-red-500"
          }`}
        >
          {flashMessage}
        </div>
      )}

      <main className="container mx-auto p-4 max-w-5xl">
        <div className="flex items-center mb-6">
          <Link to="/resident" className="mr-3 hover:bg-gray-200 p-1 rounded-full">
            <ArrowLeft className="h-5 w-5 text-gray-600" />
          </Link>
          <div>
            <h2 className="text-2xl font-bold text-gray-900 mb-2">Community Requests</h2>
            <p className="text-gray-600">Offer your support to neighbors in need</p>
          </div>
        </div>

        {loading ? (
          <div className="flex justify-center py-12">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-[#4873AB]"></div>
          </div>
        ) : error ? (
          <div className="bg-red-50 text-red-500 p-4 rounded-lg text-center">{error}</div>
        ) : requests.length === 0 ? (
          <div className="bg-white rounded-lg shadow-md p-8 text-center">
            <p className="text-lg text-gray-600 mb-4">No community posts yet</p>
          </div>
        ) : (
          <div className="grid gap-6 md:grid-cols-2">
            {requests.map((request) => {
              console.log("Post ID:", request.postId, "User ID:", request.userId);
              const isOwner = String(request.userId) === String(currentUserId); // Fix ID comparison

              return (
                <div
                  key={request.postId}
                  className="relative bg-white rounded-lg shadow-md overflow-hidden border border-gray-200 p-5 transition-transform transform hover:scale-105 hover:shadow-lg"
                >
                  {/* Show edit & delete icons only if user owns the post */}
                  {isOwner && (
                    <div className="absolute bottom-2 right-2 flex space-x-2">
                      <button onClick={() => handleEdit(request.postId)} className="p-2 text-gray-500 hover:text-blue-600">
                        <Edit className="h-5 w-5" />
                      </button>
                      <button onClick={() => handleDelete(request.postId)} className="p-2 text-gray-500 hover:text-red-600">
                        <Trash className="h-5 w-5" />
                      </button>
                    </div>
                  )}

                  {/* Post Content */}
                  <h3 className="text-xl font-semibold text-gray-800">{request.postType}</h3>
                  {request.postContent && (
                    <p className="text-gray-600 mb-4">{request.postContent}</p>
                  )}
                  <div className="flex items-center text-gray-500 text-sm">
                    <Clock className="h-3 w-3 mr-1" />
                    <span>Posted {formatDate(request.dateTime)}</span>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </main>
    </div>
  );
};

export default ViewPosts;