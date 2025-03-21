import React, { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { ChevronDown, ChevronUp, Lock, Users } from 'lucide-react';
import axios from 'axios';

const BrowseParkingSpaces = () => {
  const neighbourhoodId = localStorage.getItem("neighbourhoodId");
  const userId = localStorage.getItem("userId");
  const token = localStorage.getItem('token');
  const [flashMessage, setFlashMessage] = useState("");
  const [flashType, setFlashType] = useState(""); // "success" or "error"

  const navigate = useNavigate();
  const [parkingSpaces, setParkingSpaces] = useState([]);
  const [filters, setFilters] = useState({
    parkingSpaceName: '',  
    parkingType: '',
    feature: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [originalSpaces, setOriginalSpaces] = useState([]);
  const [showFilters, setShowFilters] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [selectedSpace, setSelectedSpace] = useState(null);
  const [reservedSpaces, setReservedSpaces] = useState({});
  const [confirmedSpaces, setConfirmedSpaces] = useState({});


  

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({
      ...filters,
      [name]: value,
    });
  };

  const applyFilters = () => {
    const filteredSpaces = parkingSpaces.filter((space) => {
      return (
        (filters.parkingSpaceName ? space.parkingSpaceName.includes(filters.parkingSpaceName): true) &&
        (filters.parkingType ? space.parkingType.includes(filters.parkingType) : true) &&
        (filters.feature ? space.feature.includes(filters.feature) : true)
      );
    });
    setParkingSpaces(filteredSpaces);
  };

  console.log(parkingSpaces);

  const handleReserveClick = (space) => {
    setSelectedSpace(space);
    setShowModal(true);
  };

  const handleConfirmReservation = (spaceId) => {
    // Ensure userId is available
    if (!userId) {
      console.error("User ID is missing");
      return;
    }
  
    // Prepare the URL with both parameters
    const reserveParkingUrl = `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_RESERVE_PARKING_SPACE}?parkingSpaceId=${spaceId}&userId=${userId}`;

  
    // Make the API call using Axios
    axios
      .post(reserveParkingUrl, {}, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      .then((response) => {
        console.log(response);
        if (response.status === 200) {
          // Update state to mark the space as confirmed
          setConfirmedSpaces((prev) => ({
            ...prev,
            [spaceId]: true, // Mark the space as confirmed
          }));
  
          // Update the parkingSpaces state to set available to false for the reserved space
          setParkingSpaces((prevSpaces) =>
            prevSpaces.map((space) =>
              space.parkingSpaceId === spaceId
                ? { ...space, available: false } // Update availability
                : space
            )
          );
  
          // Show success flash message
          setFlashMessage("Parking space reserved successfully! ✅");
          setFlashType("success");
        } else {
          // Show error flash message
          setFlashMessage("Failed to reserve parking space. ❌");
          setFlashType("error");
        }
      })
      .catch((error) => {
        console.error("Error during reservation:", error);
        // Show error flash message
        setFlashMessage("Error during reservation. Please try again. ❌");
        setFlashType("error");
      });
  
    // Close the modal
    setShowModal(false);
  
    // Hide flash message after 3 seconds
    setTimeout(() => {
      setFlashMessage("");
      setFlashType("");
    }, 2000);
  };
  
  useEffect(() => {
    const fetchParkingSpaces = async () => {
      setLoading(true);
      try {
        const response = await axios.get(`${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_BROWSE_PARKING_SPACE}`, {
          params: { neighbourhoodId },
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        console.log(response.data.data);

        setOriginalSpaces(response.data.data);
        setParkingSpaces(response.data.data);
      } catch (err) {
        setError('Failed to fetch parking spaces');
      } finally {
        setLoading(false);
      }
    };
    fetchParkingSpaces();
  }, []);

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
    <div className="min-h-screen bg-blue-50">

      {/* Flash Message */}
      {flashMessage && (
        <div
          className={`fixed top-5 left-1/2 transform -translate-x-1/2 px-4 py-2 rounded-lg text-white text-center shadow-lg transition-all duration-300 ${
            flashType === "success" ? "bg-green-500" : "bg-red-500"
          }`}
        >
          {flashMessage}
        </div>
      )}

      {/* Navigation Bar */}
      <header className="bg-white shadow-md py-4 w-full">
        <div className="max-w-7xl mx-auto px-4 flex items-center justify-between">
          {/* Left Side - Logo */}
          <div className="flex items-center space-x-4">
            <Link to="/" className="hover:bg-gray-100 p-2 rounded-lg">
              <Users className="h-7 w-7 text-[#4873AB]" />
            </Link>
            <h1 className="text-2xl font-bold text-[#4873AB] cursor-pointer" onClick={() => navigate('/')}>
              Neighborly
            </h1>
          </div>

          {/* Navigation Buttons */}
          <div className="flex space-x-4">
            <button
              onClick={() => navigate('/resident')}
              className="p-2 bg-[#4873AB] text-white rounded-lg hover:bg-[#3c6498] transition duration-300"
            >
              &larr; Back to Resident
            </button>
            <button
              onClick={() => navigate('/parkingHistory')}
              className="p-2 bg-[#4873AB] text-white rounded-lg hover:bg-[#3c6498] transition duration-300"
            >
              View Parking History &rarr;
            </button>
          </div>
        </div>
      </header>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto mt-6 p-4">
        <h1 className="text-3xl font-semibold text-center text-gray-800 mb-6">
          Browse Available Parking Spaces
        </h1>

        {/* Filters - Increased Width */}
        <div className="w-full max-w-2xl mx-auto p-6 bg-white shadow-md rounded-lg mb-8">
          <button
            onClick={() => setShowFilters(!showFilters)}
            className="w-full flex justify-between items-center p-4 bg-[#4873AB] text-white rounded-lg font-semibold hover:bg-[#3c6498] transition duration-300"
          >
            Filters {showFilters ? <ChevronUp /> : <ChevronDown />}
          </button>

          {showFilters && (
            <form className="space-y-4 mt-4">
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
                {/* Price Range */}
                <div className="flex flex-col">
                  <label className="text-sm font-medium text-gray-600"> Parking Space Name</label>
                  <input
                    type="text"
                    id="parkingSpaceName"
                    name="parkingSpaceName"
                    value={filters.parkingSpaceName}
                    onChange={handleFilterChange}
                    required
                    className="mt-2 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#4873AB]"
                  />
                </div>

             {/* Parking Type */}
             <div className="flex flex-col">
                <label
                  htmlFor="parkingType"
                  className="text-sm font-medium text-gray-600"
                >
                  Parking Type
                </label>
                <select
                  id="parkingType"
                  name="parkingType"
                  value={filters.parkingType}
                  onChange={handleFilterChange}
                  required
                  className="mt-2 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#4873AB]"
                >
                  <option value="">-- Select Parking Type --</option>
                  <option value="GARAGE">GARAGE</option>
                  <option value="DRIVEWAY">DRIVEWAY</option>
                  <option value="STREET">STREET</option>
                  <option value="COVERED">COVERED</option>
                  <option value="UNCOVERED">UNCOVERED</option>

                </select>  
              </div>
            </div>

             {/* Special Features */}
             <div className="flex flex-col">
              <label htmlFor="feature" className="text-sm font-medium text-gray-600">
               Feature
              </label>
              <select
                id="feature"
                name="feature"
                value={filters.feature}
                onChange={handleFilterChange}
                required
                className="mt-2 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#4873AB]"
              >
                <option value="">-- Select Features --</option>
                <option value="SECURITY_CAMERAS">SECURITY_CAMERAS</option>
                <option value="EV_CHARGING">EV_CHARGING</option>
                <option value="HANDICAP_ACCESSIBLE">HANDICAP_ACCESSIBLE</option>
                <option value="LIGHTING">LIGHTING</option>
                <option value="GATED">GATED</option>

              </select>
            </div>
         

            <button
  type="button"
  onClick={applyFilters}
  className="w-full p-4 bg-[#4873AB] text-white rounded-lg font-semibold hover:bg-[#3c6498] transition duration-300"
>
  Apply Filters
</button>

<button
  type="button"
  onClick={() => setParkingSpaces(originalSpaces)}
  className="w-full p-4 bg-gray-500 text-white rounded-lg font-semibold hover:bg-gray-600 transition duration-300 mt-2"
>
  Reload
</button>

            </form>
          )}
        </div>

        {/* Parking Spaces List */}
        {loading && <p className="text-center text-gray-500 mt-4">Loading...</p>}
        {error && <p className="text-center text-red-500 mt-4">{error}</p>}

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {parkingSpaces.length === 0 ? (
            <p className="text-center text-gray-500 col-span-3">No parking spaces available.</p>
          ) : (
            parkingSpaces.map((space) => (
              
              <div key={space.id} className="bg-white p-6 border border-gray-300 rounded-lg shadow-md hover:shadow-lg hover:scale-105 transition-transform duration-300">
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
                <p className="text-gray-600 mt-1">Price:{" "}${space.price}</p>
                <p className="text-gray-600 mt-1">Contact: {space.contactInfo}</p>
                <p className="text-gray-600 mt-1">Posted {formatDate(space.createdAt)}</p>
             
                <button
  onClick={() => handleReserveClick(space)}
  className={`mt-4 w-full py-2 ${
    !space.available || confirmedSpaces[space.parkingSpaceId]
      ? "bg-gray-400 cursor-not-allowed"
      : "bg-[#4873AB] hover:bg-[#3c6498]"
  } text-white font-semibold rounded-lg transition duration-300`}
  disabled={!space.available || confirmedSpaces[space.parkingSpaceId]}
>
  {!space.available || confirmedSpaces[space.parkingSpaceId] ? (
    <span className="flex items-center justify-center">
      <Lock className="mr-2 h-5 w-5 text-gray-600" /> Reserved
    </span>
  ) : (
    "Reserve Space"
  )}
</button>



              </div>
            ))
          )}
        </div>

        {/* Reservation Modal */}
        {showModal && (
          <div className="fixed inset-0 bg-gray-500 bg-opacity-50 flex justify-center items-center">
            <div className="bg-white p-6 rounded-lg shadow-lg w-80">
              <h3 className="text-xl font-semibold text-gray-700 mb-4">Confirm Reservation</h3>
              <p className="text-gray-600 mb-4">Are you sure you want to reserve this parking space?</p>
              <div className="flex justify-between space-x-4">
                <button
                  onClick={() => setShowModal(false)}
                  className="w-1/2 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition duration-300"
                >
                  Cancel
                </button>
                <button
                  onClick={() => handleConfirmReservation(selectedSpace.parkingSpaceId)}
                  className="w-1/2 py-2 bg-[#4873AB] text-white rounded-lg hover:bg-[#3c6498] transition duration-300"
                >
                  Confirm
                </button>
              </div>
            </div>
          </div>
        )}
      </main>
    </div>
  );
};

export default BrowseParkingSpaces;
