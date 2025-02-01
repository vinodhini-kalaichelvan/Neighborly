import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    // setIsLoading(true);
    // try {
    //   const res = await axios.post("https://your-backend-api.com/login", { email, password });
    //   setMessage(res.data.message);
    // } catch (error) {
    //   setMessage("Login failed");
    // } finally {
    //   setIsLoading(false);
    // }
  };

  return (
    <div className="min-h-screen flex flex-col lg:flex-row">
      {/* Left Section - Image and Welcome Text */}
      <div className="lg:w-1/2 bg-blue-600">
        <div className="flex flex-col justify-center items-center h-full p-8">
          
          <div className="mt-8 text-center">
            <h1 className="text-4xl font-bold text-white">Welcome Back!</h1>
            <p className="text-blue-100 mt-4 text-lg">
              Sign in to access your account and continue your journey
            </p>
          </div>
        </div>
      </div>

      {/* Right Section - Login Form */}
      <div className="lg:w-1/2 bg-gray-50">
        <div className="flex flex-col justify-center min-h-screen p-8">
          <div className="max-w-md w-full mx-auto space-y-8">
            <div>
              <h2 className="text-3xl font-bold text-gray-900">Sign in to your account</h2>
              <p className="mt-2 text-sm text-gray-600">
                Or{' '}
                <Link to="/register" className="font-medium text-blue-600 hover:text-blue-500">
                  create a new account
                </Link>
              </p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Email address
                </label>
                <input
                  type="email"
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Enter your email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  Password
                </label>
                <input
                  type="password"
                  required
                  className="w-full px-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>

              <div className="flex items-center justify-between">
                <div className="flex items-center">
                  <input
                    id="remember-me"
                    type="checkbox"
                    className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                  />
                  <label htmlFor="remember-me" className="ml-2 block text-sm text-gray-700">
                    Remember me
                  </label>
                </div>

                
              </div>

              <button
                type="submit"
                disabled={isLoading}
                className="w-full bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? 'Signing in...' : 'Sign in'}
              </button>

              {message && (
                <div className={`p-4 rounded-lg ${
                  message.includes('failed') ? 'bg-red-50 text-red-500' : 'bg-green-50 text-green-500'
                }`}>
                  {message}
                </div>
              )}
            </form>

            

          </div>
        </div>
      </div>
    </div>
  );
};

export default Login;