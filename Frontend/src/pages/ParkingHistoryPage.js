import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Users, Lock, Trash } from "lucide-react";
import axios from "axios";

const BookingHistoryPage = () => {
  const token = localStorage.getItem("token");
  const userId = localStorage.getItem("userId"); // Get logged-in user ID
  const neighbourhoodId = localStorage.getItem("neighbourhoodId"); // Get neighbourhood ID
  const navigate = useNavigate();
  const [parkingSpaces, setParkingSpaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [flashMessage, setFlashMessage] = useState(null); // For success/error messages
  const [flashType, setFlashType] = useState("success"); // For message type (success/error)

  useEffect(() => {
    const fetchParkingSpaces = async () => {
      setLoading(true);
      try {
        const response = await axios.get(
          `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_BROWSE_PARKING_SPACE}`,
          {
            params: { neighbourhoodId },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        console.log("API Response:", response.data); // Debugging: Log the full API response
        console.log("Parking Spaces:", response.data.data); // Debugging: Log the parking spaces

        if (response.data.result === "SUCCESS") {
          // Filter reserved spaces only (available = false)
          const reserved = response.data.data.filter(
            (space) => space.available === false
          );

          console.log("Reserved Spaces:", reserved); // Debugging: Log the filtered reserved spaces

          setParkingSpaces(reserved);
        } else {
          setError("Failed to fetch reserved parking spaces.");
        }
      } catch (err) {
        setError("Failed to fetch parking spaces.");
      } finally {
        setLoading(false);
      }
    };

    fetchParkingSpaces();
  }, []);

  
  const handleUnreserve = async (spaceId) => {
    try {
      const unreserveParkingUrl = `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_UNRESERVED_PARKING_SPACE}?parkingSpaceId=${spaceId}&userId=${userId}`;
       
      const response = await axios.post(
        unreserveParkingUrl,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );


      console.log(response.data);
      
      if (response.data.result === "SUCCESS") {
        // Update the UI instantly
        setParkingSpaces((prevSpaces) =>
          prevSpaces.map((space) =>
            space.parkingSpaceId === spaceId ? { ...space, available: true } : space
          )
        );
        setFlashMessage("Parking space unreserved successfully!");
        setFlashType("success");
      } else {
        setFlashMessage("Failed to unreserve parking space.");
        setFlashType("error");
      }
    } catch (error) {
      console.error("Error unreserving parking space:", error);
      setFlashMessage("Failed to unreserve parking space. Please try again.");
      setFlashType("error");
    } finally {
      setTimeout(() => setFlashMessage(null), 3000); // Clear flash message after 3 seconds
    }
  };

  const handleDelete = async (spaceId) => {
    try {
      const deleteParkingUrl = `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_DELETE_PARKING_SPACE}?parkingSpaceId=${spaceId}&userId=${userId}`;

      const response = await axios.delete(
        deleteParkingUrl,
        {},
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.data.result === "SUCCESS") {
        setParkingSpaces((prevSpaces) =>
          prevSpaces.filter((space) => space.parkingSpaceId !== spaceId)
        );
        setFlashMessage("Parking space deleted successfully!");
        setFlashType("success");
      } else {
        setFlashMessage("Failed to delete parking space.");
        setFlashType("error");
      }
    } catch (error) {
      console.error("Error deleting parking space:", error);
      setFlashMessage("Failed to delete parking space. Please try again.");
      setFlashType("error");
    } finally {
      setTimeout(() => setFlashMessage(null), 3000); // Clear flash message after 3 seconds
    }
  };

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

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Navigation Bar */}
      <header className="bg-white shadow-md py-4 w-full">
        <div className="max-w-7xl mx-auto px-4 flex items-center justify-between">
          {/* Left Side - Logo */}
          <div className="flex items-center space-x-4">
            <Link to="/" className="hover:bg-gray-100 p-2 rounded-lg">
              <Users className="h-7 w-7 text-[#4873AB]" />
            </Link>
            <h1 className="text-2xl font-bold text-[#4873AB] cursor-pointer" onClick={() => navigate("/")}>
              Neighborly
            </h1>
          </div>

          {/* Navigation Buttons */}
          <div className="flex space-x-4">
            <button
              onClick={() => navigate("/browseSpaces")}
              className="p-2 bg-[#4873AB] text-white rounded-lg hover:bg-[#3c6498] transition duration-300"
            >
              &larr; Back
            </button>
          </div>
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

      {/* Main Content */}
      <main className="flex flex-col items-center justify-start min-h-screen bg-blue-50 p-6">
        <div className="w-full max-w-6xl p-6 rounded-lg">
          <h2 className="text-3xl font-semibold text-center text-gray-800 mb-8">Reserved Parking Spaces</h2>

          {/* Parking Spaces List */}
          {loading && <p className="text-center text-gray-500 mt-4">Loading...</p>}
          {error && <p className="text-center text-red-500 mt-4">{error}</p>}

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
            {parkingSpaces.length === 0 ? (
              <p className="text-center text-gray-500 col-span-3">No reserved parking spaces found.</p>
            ) : (
              parkingSpaces.map((space) => (
                <div
                  key={space.parkingSpaceId}
                  className="bg-white p-6 border border-gray-300 rounded-lg shadow-md hover:shadow-lg hover:scale-105 transition-transform duration-300"
                >
                  <h3 className="text-xl font-semibold text-gray-700">{space.parkingSpaceName}</h3>
                  <p className="text-gray-600 mt-1">ParkingType: {space.parkingType}</p>
                  <p className="text-gray-600 mt-1">Feature: {space.feature}</p>

                  <p className="text-gray-600 mt-1 flex items-center">
                    Availability:
                    <span
                      className={`ml-2 w-3 h-3 rounded-full ${
                        space.available ? "bg-green-500" : "bg-red-500"
                      }`}
                    ></span>
                    <span className="ml-1">{space.available ? "Yes" : "No"}</span>
                  </p>

                  <p className="text-gray-600 mt-1">PriceType: {space.priceType}</p>
                  <p className="text-gray-600 mt-1">Price: ${space.price}</p>
                  <p className="text-gray-600 mt-1">Contact: {space.contactInfo}</p>
                  <p className="text-gray-600 mt-1">Posted {formatDate(space.createdAt)}</p>

                  <div className="flex space-x-4 mt-4">
                    <button
                      onClick={() => handleUnreserve(space.parkingSpaceId)}
                      className={`w-full py-2 ${
                        space.available ? "bg-gray-400 cursor-not-allowed" : "bg-[#4873AB] hover:bg-[#3c6498]"
                      } text-white  font-semibold rounded-lg transition duration-300`}
                      disabled={space.available} // Disable if already available
                    >
                      Unreserve
                    </button>

                    <button
                      onClick={() => handleDelete(space.parkingSpaceId)}
                      className="px-2  bg-gray-500 text-white rounded-lg hover:bg-red-700 transition flex items-center justify-center"
                    >
                      <Trash className="h-5 w-5" />
                    </button>
                  </div>
                </div>
              ))
            )}
          </div>
        </div>
      </main>
    </div>
  );
};

export default BookingHistoryPage;