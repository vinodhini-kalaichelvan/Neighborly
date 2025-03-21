import React, { useState, useEffect } from "react";
import axios from "axios";
import { Link, useNavigate, useParams } from "react-router-dom";
import { User, Mail, Phone, FileText, Tag, Users, ArrowLeft } from 'lucide-react';

const EditPost = () => {
  const { postId } = useParams();  // Capture the postId from the URL params
  const [formData, setFormData] = useState({
    postType: "",
    postContent: "",
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("");
  const [approvalMessage, setApprovalMessage] = useState("");
  const [userId, setUserId] = useState(null);
  const [neighbourhoodId, setNeighbourhoodId] = useState(null);
  const token = localStorage.getItem('token');

  const navigate = useNavigate();

   useEffect(() => {
      const storedUserId = localStorage.getItem("userId");
      const storedNeighbourhoodId = localStorage.getItem("neighbourhoodId");
  
      if (storedUserId) setUserId(parseInt(storedUserId));
      if (storedNeighbourhoodId) setNeighbourhoodId(parseInt(storedNeighbourhoodId));
    }, []);

  

 

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMessage("");
    setMessageType("");
    setApprovalMessage("");

    if (!userId || !neighbourhoodId) {
      setMessage("User ID or Neighbourhood ID is missing. Please log in again.");
      setMessageType("error");
      return;
    }

    const payload = {
      postType: formData.postType,
      postContent: typeof formData.postContent === "string" ? formData.postContent : JSON.stringify(formData.postContent),
    };
    
    console.log("Payload being sent:", payload);
    
    

    try {
      setLoading(true);
      const apiUrl = `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_UPDATE_POST_ENDPOINT}/${postId}`;
      await axios.put(apiUrl, payload, {
        headers: { "Content-Type": "application/json",  Authorization: `Bearer ${token}` },
      }); // Update the post in the database



      setMessage("Post updated successfully!");
      setMessageType("success");

      setTimeout(() => {
        navigate("/viewpost"); // Redirect to the view post page after successful update
      }, 2000);
    } catch (error) {
      setMessage("Failed to update post. Please check your input.");
      setMessageType("error");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex">
      <div className="hidden lg:flex w-1/2 bg-[#4873AB] p-7 flex-col">
        <div className="flex items-center space-x-2">

        <Link to="/" className="hover:bg-gray-400 p-1 rounded-lg">
            <Users className="h-7 w-7 text-white" />
          </Link>

          <Link to="/" className="hover:bg-gray-400 p-1 rounded-lg">
            <h1 className="text-2xl font-bold text-white">Neighborly</h1>
          </Link>
        </div>


        <div className="flex flex-1 flex-col justify-center items-center text-center space-y-8">
          <h1 className="text-5xl font-bold text-white">Edit Your Post</h1>
          <p className="text-blue-100 mt-4 text-2xl">Refine your details and share the love!</p>
        </div>
      </div>

      <div className="flex-1 flex items-center justify-center p-8 bg-gray-50">
        <div className="w-full max-w-md bg-white rounded-lg shadow-xl border-2 border-gray-200">
          <div className="p-8">
            <Link to="/viewpost" className="mr-3 p-1 rounded-full">
              <ArrowLeft className="h-5 w-5 text-gray-600" />
            </Link>

            <h2 className="text-3xl font-bold text-gray-900">Edit Post</h2>
            <p className="text-gray-600 mt-2">Modify the details of your post</p>

            {message && (
              <div className={`mt-4 p-4 text-white rounded-lg ${messageType === "success" ? "bg-green-500" : "bg-red-500"}`}>
                {message}
              </div>
            )}

            {approvalMessage && (
              <p className="p-4 rounded-lg bg-green-50 text-green-500">{approvalMessage}</p>
            )}

            <form onSubmit={handleSubmit} className="space-y-4 mt-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Post Type</label>
                <select
                  name="postType"
                  value={formData.postType || ""}
                  onChange={handleChange}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  required
                >
                  <option value="">Select Category</option>
                  <option value="Tools">Tools</option>
                  <option value="Services">Services</option>
                  <option value="Events">Events</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">Post Content</label>
                <textarea
                  name="postContent"
                  value={formData.postContent || ""}
                  onChange={handleChange}
                  className="w-full p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                  rows="6"
                  placeholder="Write your post here..."
                  required
                />
              </div>

              <button
                type="submit"
                className="w-full bg-[#4873AB] text-white py-2 px-4 rounded-lg hover:bg-[#1e40af] transition duration-200"
                disabled={loading}
              >
                {loading ? "Updating..." : "Update Post"}
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default EditPost;
