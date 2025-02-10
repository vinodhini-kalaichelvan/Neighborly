import React from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { Search, UserPlus, LogIn, Users, HelpCircle, Building, Car } from 'lucide-react';

const MainLayout = () => {
  const navigate = useNavigate();

  const headerIcons = [
    { icon: HelpCircle, text: "Help Requests", link: "/login" },
    { icon: Building, text: "Book Spaces", link: "/login" },
    { icon: Car, text: "Parking", link: "/login" }
  ];

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
                  onClick={() => navigate('/login')}
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
                    onClick={() => navigate(item.link)}
                    className="flex flex-col items-center px-3 py-1 hover:bg-gray-100 rounded-lg transition-colors mx-1"
                    title={item.text}
                  >
                    <item.icon className="w-6.5 h-6.5 text-[#4873AB]" />
                    <span className="text-[10px] text-gray-600 mt-0.5 whitespace-nowrap">{item.text}</span>
                  </button>
                ))}
              </div>
            </div>

            {/* Auth Buttons */}
            <div className="flex items-center space-x-2">
              <button onClick={() => navigate('/register')}
                className="flex items-center space-x-1 h-10 px-3 rounded-lg border border-gray-300 hover:bg-gray-50 transition-colors text-sm">
                <UserPlus className="w-4 h-4" />
                <span>Register</span>
              </button>
              <button onClick={() => navigate('/login')}
                className="flex items-center space-x-1 h-10 px-3 bg-[#4873AB] text-white rounded-lg hover:bg-blue-600 transition-colors text-sm">
                <LogIn className="w-4 h-4" />
                <span>Login</span>
              </button>
            </div>
          </div>
        </div>
      </header>
      {/* Outlet for page content */}
      <main className='pt-6'>
        <Outlet />
      </main>
    </div>
  );
};
export default MainLayout;
