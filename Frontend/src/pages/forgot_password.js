import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const ForgotPassword = () => {
  const [email, setEmail] = useState("");
  const [otp, setOtp] = useState("");
  const [emailMessage, setEmailMessage] = useState("");
  const [otpMessage, setOtpMessage] = useState("");
  const [isEmailLoading, setIsEmailLoading] = useState(false);
  const [isOtpLoading, setIsOtpLoading] = useState(false);

  const handleEmailSubmit = async (e) => {
    e.preventDefault();
    setIsEmailLoading(true);
    setEmailMessage("");

    try {
      // const res = await axios.post("your-api/forgot-password", { email });
      // Simulating API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      setEmailMessage("OTP has been sent to your email");
    } catch (error) {
      setEmailMessage("Failed to send OTP");
    } finally {
      setIsEmailLoading(false);
    }
  };

  const handleOtpSubmit = async (e) => {
    e.preventDefault();
    setIsOtpLoading(true);
    setOtpMessage("");

    try {
      // const res = await axios.post("your-api/verify-otp", { email, otp });
      // Simulating API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      setOtpMessage("OTP verified successfully. You can now reset your password.");
    } catch (error) {
      setOtpMessage("Invalid OTP. Please try again.");
    } finally {
      setIsOtpLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex flex-col lg:flex-row">
      {/* Left Section - Image and Welcome Text */}
      <div className="lg:w-1/2 bg-[#4873AB]">
        <div className="flex flex-col justify-center items-center h-full p-8">
          <div className="mt-8 text-center">
            <h1 className="text-5xl font-bold text-white">Forgot Password?</h1>
            <p className="text-blue-100 mt-4 text-2xl">
              Don't worry, we'll help you reset it
            </p>
          </div>
        </div>
      </div>

      {/* Right Section - Forgot Password Forms */}
      <div className="flex-1 flex items-center justify-center lg:w-1/2 bg-gray-50 border-gray-200 border-r-2">
        <div className="w-full max-w-md flex flex-col justify-center p-8 bg-white rounded-lg shadow-md border-2 border-gray-200 dark:border-gray-700">
          <div className="max-w-md w-full mx-auto space-y-8">
            <div>
              <h2 className="text-3xl font-bold text-gray-900">Forgot password</h2>
            </div>

            {/* Email Section */}
            <div className="space-y-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Enter Registered Email
                </label>
                <div className="flex gap-2">
                  <input
                    type="email"
                    required
                    className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    placeholder="Enter your email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                  />
                  <button
                    onClick={handleEmailSubmit}
                    disabled={isEmailLoading}
                    className="bg-[#4873AB] text-white py-2 px-4 rounded-lg hover:bg-[#1e40af] focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors disabled:opacity-50 disabled:cursor-not-allowed whitespace-nowrap"
                  >
                    {isEmailLoading ? 'Sending...' : 'Send OTP'}
                  </button>
                </div>
                {emailMessage && (
                  <div className={`mt-2 p-2 rounded-lg text-sm ${
                    emailMessage.includes('Failed') ? 'bg-red-50 text-red-500' : 'bg-green-50 text-green-500'
                  }`}>
                    {emailMessage}
                  </div>
                )}
              </div>
            </div>

            {/* OTP Section */}
            <div className="space-y-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Enter OTP
                </label>
                <div className="flex gap-2">
                  <input
                    type="text"
                    className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    placeholder="Enter OTP"
                    value={otp}
                    onChange={(e) => setOtp(e.target.value)}
                    maxLength={6}
                  />
                  <button
                    onClick={handleOtpSubmit}
                    disabled={isOtpLoading}
                    className="bg-[#4873AB] text-white py-2 px-4 rounded-lg hover:bg-[#1e40af] focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors disabled:opacity-50 disabled:cursor-not-allowed whitespace-nowrap"
                  >
                    {isOtpLoading ? 'Verifying...' : 'Verify OTP'}
                  </button>
                </div>
                {otpMessage && (
                  <div className={`mt-2 p-2 rounded-lg text-sm ${
                    otpMessage.includes('Invalid') ? 'bg-red-50 text-red-500' : 'bg-green-50 text-green-500'
                  }`}>
                    {otpMessage}
                  </div>
                )}
               
                
                
              </div>
            </div>

            <div className="text-center">
                <Link to="/login" className=" mt-2 text-sm text-gray-600 font-medium text-[#4873AB] hover:text-[#1e40af]">
                  Back to login
                </Link>
                </div>

          </div>
        </div>
      </div>
    </div>
  );
};

export default ForgotPassword;