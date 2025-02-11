import { useNavigate } from "react-router-dom"; // For redirection

const JoinOrCreateCommunity = () => {
  const navigate = useNavigate();

  const handleCreateCommunity = () => {
    // Navigate to the "Create Community" page
    navigate("/create-community"); // Redirect to the create community page
  };

  const handleJoinCommunity = () => {
    navigate("/JoinCommunity"); // Redirect to the join community page
  };

  return (
    <div className="relative min-h-screen bg-gradient-to-r from-[#5072A7] via-[#3a5a7d] to-[#2f4757] flex justify-center items-center py-8 px-4">
      {/* Background image */}
      <div className="absolute inset-0 bg-cover bg-center opacity-30" style={{ backgroundImage: "url('/path-to-your-background-image.jpg')" }}></div>

      <div className="relative z-10 w-full max-w-6xl bg-white bg-opacity-80 p-8 rounded-lg shadow-lg text-center space-y-8">
        <h2 className="text-4xl font-semibold text-gray-700">Join or Create a Community</h2>
        <p className="text-xl text-gray-600">Choose your next step to connect with a community or create a new one.</p>

        <div className="flex flex-col md:flex-row justify-center space-y-8 md:space-y-0 md:space-x-8">
          {/* Create Community Button */}
          <div className="w-full md:w-1/2">
            <button
              onClick={handleCreateCommunity}
              className="w-full py-4 px-6 bg-[#5072A7] text-white text-2xl rounded-lg hover:bg-[#3a5a7d] focus:outline-none focus:ring-2 focus:ring-[#5072A7]"
            >
              Create a New Community
            </button>
          </div>

          {/* Join Community Button */}
          <div className="w-full md:w-1/2">
            <button
              onClick={handleJoinCommunity}
              className="w-full py-4 px-6 bg-[#2f4757] text-white text-2xl rounded-lg hover:bg-[#7eb6d4] hover:text-white focus:outline-none focus:ring-2 focus:ring-[#5072A7]"

            >
              Join an Existing Community
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default JoinOrCreateCommunity;