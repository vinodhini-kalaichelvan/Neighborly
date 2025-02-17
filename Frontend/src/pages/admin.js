import React from 'react';
import { Link } from 'react-router-dom';

const AdminDashboard = () => {
  return (
      <div className="min-h-screen flex flex-col lg:flex-row">
        {/* Left Section - Image and Welcome Text */}
        <div className="lg:w-1/2 bg-[#4873AB]">
          <div className="flex flex-col justify-center items-center h-full p-8">
            <div className="mt-8 text-center">
              <h1 className="text-5xl font-bold text-white">Welcome, Admin!</h1>
              <p className="text-blue-100 mt-4 text-2xl">
                Your control center for managing the platform
              </p>
            </div>
          </div>
        </div>

        {/* Right Section - Admin Dashboard */}
        <div className="flex-1 flex items-center justify-center lg:w-1/2 bg-gray-50 border-gray-200 border-r-2">
          <div className="w-full max-w-md flex flex-col justify-center p-8 bg-white rounded-lg shadow-md border-2 border-gray-200 dark:border-gray-700">
            <div className="max-w-md w-full mx-auto space-y-8">
              <div>
                <h2 className="text-3xl font-bold text-gray-900">Admin Dashboard</h2>
                <p className="mt-2 text-sm text-gray-600">
                  Manage your platform with comprehensive tools
                </p>
              </div>

              <div className="space-y-6">
                <div className="grid grid-cols-2 gap-4">
                  <button className="bg-[#4873AB] text-white py-2 px-4 rounded-lg hover:bg-[#1e40af] transition-colors">
                    User Management
                  </button>
                  <button className="bg-[#4873AB] text-white py-2 px-4 rounded-lg hover:bg-[#1e40af] transition-colors">
                    Settings
                  </button>
                </div>

                <Link
                    to="/logout"
                    className="block w-full text-center bg-red-500 text-white py-2 px-4 rounded-lg hover:bg-[#dc2626] transition-colors"
                >
                  Logout
                </Link>
              </div>
            </div>
          </div>
        </div>
      </div>
  );
};

export default AdminDashboard;