import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { ChevronLeft, Users } from 'lucide-react';
import axios from 'axios';

const ParkingSpaceListing = () => {

  const neighbourhoodId = localStorage.getItem("neighbourhoodId");
  const userId = localStorage.getItem("userId"); // Get logged-in user ID
  const token = localStorage.getItem('token');

  const [listingData, setListingData] = useState({
    userId,
    neighbourhoodId,
    parkingSpaceName: '',
    parkingType: '',
    price: '',
    priceType: '',
    available: '',
    feature: '', 
    contactInfo: '',
  });
  const [loading, setLoading] = useState(false);
  const [notification, setNotification] = useState({
    type: '', // 'success' or 'error'
    message: '',
  });
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value, type } = e.target;
  
    setListingData((prevData) => ({
      ...prevData,
      [name]: type === "radio" ? Boolean(Number(value)) : value, // Ensure proper boolean conversion
    }));
  };
  


  const handleSubmit = (e) => {
    e.preventDefault();  // Prevent form default submission
    setLoading(true);  // Set loading state to true when the form is submitted
    setNotification({ type: '', message: '' });  // Reset any previous notifications
  
    const apiUrl = process.env.REACT_APP_API_BASE_URL;  // Get the API base URL from environment variables
    const createParkingSpaceEndpoint = process.env.REACT_APP_CREATE_PARKING_SPACE;  // Get the endpoint for creating parking space from environment variables
  
    axios
      .post(`${apiUrl}${createParkingSpaceEndpoint}`, listingData,  {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })  // Make the POST request to the API
      .then((response) => {  // If the request is successful
        setLoading(false);  // Set loading state to false
         console.log(response);
         
        setNotification({
          type: 'success',
          message: 'Parking space listed successfully!',
        });  // Display success notification
  
        // Clear notification and redirect after 2 seconds
        setTimeout(() => {
          setNotification({ type: '', message: '' });  // Reset notification
          navigate('/resident');  // Redirect to the /resident page
        }, 2000);
      })
      .catch((error) => {  // If an error occurs
        setLoading(false);  // Set loading state to false
  
        setNotification({
          type: 'error',
          message: 'Error while listing parking space. Please try again.',
        });  // Display error notification
  
        // Clear notification after 2 seconds
        setTimeout(() => {
          setNotification({ type: '', message: '' });  // Reset notification
        }, 2000);
  
        console.error(error);  // Log the error for debugging
      });
  };
  
   console.log(listingData);
   
  const handleRemove = () => {
    setListingData({
    parkingSpaceName: '',
    parkingType: '',
    price: '',
    priceType: '',
    available: '',
    feature: '', 
    contactInfo: '',
    });
    setTimeout(() => {
      navigate('/resident');
    }, 500); // Redirect after 3 seconds
  };

  const handleGoBack = () => {
    navigate('/resident');
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
            <h1
              className="text-2xl font-bold text-[#4873AB] cursor-pointer"
              onClick={() => navigate('/')}
            >
              Neighborly
            </h1>
          </div>

          {/* Back Button */}
          <button
            onClick={handleGoBack}
            className="flex items-center bg-[#4873AB] text-white py-2 px-4 rounded-lg hover:bg-[#3c6498] focus:outline-none focus:ring-2 focus:ring-[#4873AB]"
          >
            <ChevronLeft className="h-5 w-5 mr-2" />
            Back to Resident
          </button>
        </div>
      </header>

      {/* Main Content */}
      <main className="flex flex-col items-center justify-center min-h-screen bg-blue-50 p-6">
        <div className="w-full max-w-4xl bg-white shadow-md p-6 rounded-lg">
          {/* Title */}
          <h1 className="text-3xl font-semibold text-center text-gray-800 mb-6">
            List Your Parking Space
          </h1>

          <form onSubmit={handleSubmit} className="space-y-6">
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
              {/* Parking Space Name */}
              <div className="flex flex-col">
                <label
                  htmlFor="parkingSpaceName"
                  className="text-sm font-medium text-gray-600"
                >
                  Parking Space Name
                </label>
                <input
                  type="text"
                  id="parkingSpaceName"
                  name="parkingSpaceName"
                  value={listingData.parkingSpaceName}
                  onChange={handleChange}
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
                  value={listingData.parkingType}
                  onChange={handleChange}
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
        
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-6">
              {/* Price */}
              <div className="flex flex-col">
                <label htmlFor="price" className="text-sm font-medium text-gray-600">
                  Price
                </label>
                <input
                  type="number"
                  id="price"
                  name="price"
                  value={listingData.price}
                  onChange={handleChange}
                  required
                  className="mt-2 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#4873AB]"
                />
              </div>

              {/* Price Type */}
              <div className="flex flex-col">
                <label htmlFor="priceType" className="text-sm font-medium text-gray-600">
                  Price Type
                </label>
                <select
                  id="priceType"
                  name="priceType"
                  value={listingData.priceType}
                  onChange={handleChange}
                  required
                  className="mt-2 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#4873AB]"
                >
             <option value="">-- Select Price Type --</option>
             <option value="HOURLY">HOURLY</option>
             <option value="DAILY">DAILY</option>
             <option value="WEEKLY">WEEKLY</option>
             <option value="MONTHLY">MONTHLY</option>

                </select>
              </div>
            </div>

{/* Availability */}
<div className="flex flex-col w-full">
  <label htmlFor="availability" className="text-sm font-medium text-gray-600 text-center">
    Availability
  </label>
  <div className="flex items-center justify-center w-full mt-2 space-x-4">
    <label className="flex items-center">
      <input
        type="radio"
        id="available-yes"
        name="available"
        value="1"
        checked={Boolean(listingData.available)} // Ensure true/false
        onChange={handleChange}
        className="h-5 w-5 text-blue-500 border-gray-300"
      />
      <span className="ml-2 text-gray-600">Available</span>
    </label>

    <label className="flex items-center">
      <input
        type="radio"
        id="available-no"
        name="available"
        value="0"
        checked={!Boolean(listingData.available)} // Ensure false when unavailable
        onChange={handleChange}
        className="h-5 w-5 text-blue-500 border-gray-300"
      />
      <span className="ml-2 text-gray-600">Not Available</span>
    </label>
  </div>
</div>






      

            {/* Special Features */}
            <div className="flex flex-col">
              <label htmlFor="feature" className="text-sm font-medium text-gray-600">
                Special Features
              </label>
              <select
                id="feature"
                name="feature"
                value={listingData.feature}
                onChange={handleChange}
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
         
            {/* Contact Info */}
            <div className="flex flex-col">
              <label
                htmlFor="contactInfo"
                className="text-sm font-medium text-gray-600"
              >
                Contact Information
              </label>
              <input
                type="text"
                id="contactInfo"
                name="contactInfo"
                value={listingData.contactInfo}
                onChange={handleChange}
                required
                className="mt-2 p-3 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-[#4873AB]"
              />
            </div>

            {/* Buttons */}
            <div className="flex justify-center space-x-4 mt-8">
              <button
                type="submit"
                disabled={loading}
                className="w-full sm:w-1/2 bg-[#4873AB] text-white py-2 rounded-lg hover:bg-[#3c6498]"
              >
                {loading ? 'Posting...' : 'Post Parking Space'}
              </button>
              <button
                type="button"
                onClick={handleRemove}
                className="w-full sm:w-1/2 bg-red-600 text-white py-2 rounded-lg hover:bg-red-700"
              >
                Cancel
              </button>
            </div>
          </form>

          {/* Notification Message */}
          {notification.message && (
            <div
              className={`mt-4 text-center font-semibold ${
                notification.type === 'success' ? 'text-green-500 bg-green-50' : 'text-red-500 bg-red-50'
              }`}
            >
              {notification.message}
            </div>
          )}
        </div>
      </main>
    </div>
  );
};

export default ParkingSpaceListing;
