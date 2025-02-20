import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom"; // For redirection
import commImage from '../assets/comm.jpg'; // Example of one image

const CreateCommunity = () => {
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    phone: "",
    address: "",
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const navigate = useNavigate(); // Hook to handle navigation
  const [imageVisible, setImageVisible] = useState(false); // State to handle image visibility

  useEffect(() => {
    // Once the component mounts, trigger the image animation
    setImageVisible(true);
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage("");

    try {
     const response = await fetch(`${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_CREATE_COMMUNITY_ENDPOINT}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      const result = await response.json();

      if (response.ok) {
        setMessage("Community request submitted successfully!");
        setFormData({ name: "", email: "", phone: "", address: "" });

        // ✅ Redirect to pending approval or dashboard
        navigate("/"); // Change this if needed
      } else {
        setMessage(result.error || "Failed to create community.");
      }
    } catch (error) {
      setMessage("Error submitting request.");
    }

    setLoading(false);
  };

  return (
      <div className="min-h-screen bg-gradient-to-r from-[#5072A7] via-[#3a5a7d] to-[#2f4757] flex justify-center items-center py-8 px-4">
        <div className="flex flex-col md:flex-row w-full max-w-6xl bg-white p-8 rounded-lg shadow-lg space-y-8 md:space-y-0 md:space-x-8">

          {/* Left side - Image */}
          <div className={`w-full md:w-1/2 h-[70vh] rounded-lg bg-cover bg-center transition-all duration-1000 ${imageVisible ? 'transform translate-y-0 opacity-100' : 'transform translate-y-20 opacity-0'}`}
               style={{ backgroundImage: `url(${commImage})` }}
          >
            {/* The image container */}
          </div>

          {/* Right side - Form */}
          <div className="w-full md:w-1/2 p-6 md:p-8 flex flex-col justify-center items-center space-y-6">
            <h2 className="text-3xl font-semibold text-center mb-6 text-gray-700">Create a New Community</h2>
            {message && <p className="text-center text-red-500 mb-4">{message}</p>}

            <form onSubmit={handleSubmit} className="space-y-4 w-full max-w-lg">
              <div>
                <input
                    type="text"
                    name="name"
                    placeholder="Community Name"
                    value={formData.name}
                    onChange={handleChange}
                    required
                    className="w-full px-4 py-3 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
                />
              </div>

              <div>
                <input
                    type="email"
                    name="email"
                    placeholder="Email Address"
                    value={formData.email}
                    onChange={handleChange}
                    required
                    className="w-full px-4 py-3 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
                />
              </div>

              <div>
                <input
                    type="tel"
                    name="phone"
                    placeholder="Phone Number"
                    value={formData.phone}
                    onChange={handleChange}
                    required
                    className="w-full px-4 py-3 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
                />
              </div>

              <div>
                <input
                    type="text"
                    name="address"
                    placeholder="Community Address"
                    value={formData.address}
                    onChange={handleChange}
                    required
                    className="w-full px-4 py-3 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-400"
                />
              </div>

              <div className="text-center">
                <button
                    type="submit"
                    disabled={loading}
                    className="w-full py-3 px-4 bg-[#5072A7] text-white rounded-md hover:bg-[#3a5a7d] focus:outline-none focus:ring-2 focus:ring-[#5072A7]"
                >
                  {loading ? "Submitting..." : "Create Community"}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
  );
};

export default CreateCommunity;
