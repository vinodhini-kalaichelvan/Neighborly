import React, { useState } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { Search, Users, HelpCircle, Building, Car, Bell, User, LogOut, X } from 'lucide-react';

const MainLayout = () => {
  const navigate = useNavigate();
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);

  const headerIcons = [
    { icon: HelpCircle, text: "Help Requests", link: "/" },
    { icon: Building, text: "Book Spaces", link: "/" },
    { icon: Car, text: "Parking", link: "/" },
    { icon: Bell, text: "Notifications", link: "/notifications" },
    { 
      icon: User, 
      text: "Profile", 
      onClick: () => setIsSidebarOpen(true),
      link: null 
    }
  ];

  const handleLogout = () => {
    // Add logout logic here
    navigate('/Homepage');
    setIsSidebarOpen(false);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header and Search Section */}
      <header className="bg-white shadow-md py-4 w-full">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex items-center justify-between">
            {/* User Icon, Logo, Search Bar, and Feature Icons */}
            <div className="flex items-center space-x-4 w-full">
              <div className="flex items-center space-x-2">
                <button 
                  onClick={() => navigate('/Dashboard')}
                  className="hover:bg-gray-100 p-1 rounded-lg">
                  <Users className="h-7 w-7 text-[#4873AB]" />
                </button>
                <h1 className="text-2xl font-bold text-[#4873AB] whitespace-nowrap">
                  Neighborly
                </h1>
              </div>
              
              <div className="relative w-full max-w-md">
                <input
                  type="text"
                  placeholder="Search..."
                  className="w-full pl-4 pr-12 h-10 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-[#4873AB] focus:border-transparent"
                />
                <button 
                  className="absolute right-1 top-1/2 -translate-y-1/2 h-8 w-8 p-0 flex items-center justify-center bg-[#4873AB] text-white rounded-md hover:bg-blue-600 transition-colors">
                  <Search className="w-4 h-4" />
                </button>
              </div>
              
              <div className="flex items-center space-x-4">
                {headerIcons.map((item, index) => (
                  <button
                    key={index}
                    onClick={item.onClick || (() => item.link && navigate(item.link))}
                    className="flex flex-col items-center px-3 py-1 hover:bg-gray-100 rounded-lg transition-colors mx-1"
                    title={item.text}
                  >
                    <item.icon className="w-6 h-6 text-[#4873AB]" />
                    <span className="text-[10px] text-gray-600 mt-0.5 whitespace-nowrap">{item.text}</span>
                  </button>
                ))}
              </div>
            </div>
          </div>
        </div>
      </header>

      {/* Overlay with fade animation */}
      <div 
        className={`fixed inset-0 bg-black transition-opacity duration-300 ${
          isSidebarOpen ? 'opacity-50 z-40' : 'opacity-0 -z-10'
        }`}
        onClick={() => setIsSidebarOpen(false)}
      />
          
      {/* Sidebar with slide animation */}
      <div 
        className={`fixed top-0 right-0 h-full w-64 bg-white shadow-lg transform transition-transform duration-300 ease-in-out z-50 ${
          isSidebarOpen ? 'translate-x-0' : 'translate-x-full'
        }`}
      >
        <div className="p-4">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-semibold">Profile</h2>
            <button 
              onClick={() => setIsSidebarOpen(false)}
              className="p-2 hover:bg-gray-100 rounded-full transition-colors"
            >
              <X className="w-5 h-5 text-gray-600" />
            </button>
          </div>
          
          <button
            onClick={handleLogout}
            className="flex items-center space-x-2 w-full p-3 hover:bg-gray-100 rounded-lg transition-colors"
          >
            <LogOut className="w-5 h-5 text-gray-600" />
            <span className="text-gray-600">Logout</span>
          </button>
        </div>
      </div>

      {/* Outlet for page content */}
      <main className="pt-6">
        <Outlet />
      </main>
    </div>
  );
};

export default MainLayout;
