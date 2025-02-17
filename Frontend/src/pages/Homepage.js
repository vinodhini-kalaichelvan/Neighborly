import React, { useState, useEffect } from 'react';
import { ChevronLeft, ChevronRight, Calendar, Share2, Users, HandHelping, Search, UserPlus, LogIn, HelpCircle, Building, Car } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

const Homepage = () => {
  const [currentSlide, setCurrentSlide] = useState(0);
  const carouselImages = [
    "/poster_05.jpg",
    "/poster_06.jpg",
    "/poster_07.jpg"
  ];

  const welcomeFeatures = [
    { icon: Calendar, text: "Local Events" },
    { icon: Share2, text: "Resource Sharing" },
    { icon: Users, text: "Community Building" },
    { icon: HandHelping, text: "Help Exchange" }
  ];

  const features = [
    { title: "Help Requests", description: "Request or offer tools, skills, and services." },
    { title: "Booking Public Spaces", description: "Easily book or rent spaces across neighborhoods." },
    { title: "Parking Rentals", description: "Easily share or rent parking spaces across neighborhoods hassle-free."}
  ];

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % carouselImages.length);
    }, 60000);
    return () => clearInterval(timer);
  }, [carouselImages.length]);

  const nextSlide = () => {
    setCurrentSlide((prev) => (prev + 1) % carouselImages.length);
  };

  const prevSlide = () => {
    setCurrentSlide((prev) => (prev - 1 + carouselImages.length) % carouselImages.length);
  };

  const navigate = useNavigate();

  const headerIcons = [
    { icon: HelpCircle, text: "Help Requests" },
    { icon: Building, text: "Book Spaces" },
    { icon: Car, text: "Parking"}
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
                      className="hover:bg-gray-100 p-1 rounded-lg">
                    <Users className="h-7 w-7 text-[#4873AB]" />
                  </button >
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

        {/* Homepage Content */}
        <div className="bg-blue-50 py-8">
          <div className="max-w-7xl mx-auto px-4 text-center">
            <h2 className="text-4xl font-bold text-blue-800 mb-4">Welcome to Neighborly!</h2>
            <p className="text-gray-700 max-w-2xl mx-auto mb-8">
              Your community hub for local events, resource sharing, and building stronger neighborhood connections.
              Join us to discover local activities, share resources, and connect with neighbors.
            </p>

            <div className="flex justify-center flex-wrap gap-8 mt-6">
              {welcomeFeatures.map((feature, index) => (
                  <div key={index} className="flex flex-col items-center">
                    <div className="w-12 h-12 rounded-full bg-blue-100 flex items-center justify-center mb-2">
                      <feature.icon className="w-6 h-6 text-[#4873AB]" />
                    </div>
                    <span className="text-sm font-medium text-gray-700">{feature.text}</span>
                  </div>
              ))}
            </div>
          </div>
        </div>

        <div className="relative max-w-7xl mx-auto mt-6">
          <div className="relative h-[400px] overflow-hidden rounded-xl">
            <div className="absolute flex transition-transform duration-500 ease-in-out h-full"
                 style={{ width: `${carouselImages.length * 100}%`, transform: `translateX(-${currentSlide * (100 / carouselImages.length)}%)` }}>
              {carouselImages.map((image, index) => (
                  <div key={index} style={{ width: `${100 / carouselImages.length}%` }} className="h-full flex-shrink-0">
                    <img
                        src={image}
                        alt={`Slide ${index + 1}`}
                        className="w-full h-full object-cover"
                    />
                  </div>
              ))}
            </div>

            <button
                onClick={prevSlide}
                className="absolute left-4 top-1/2 -translate-y-1/2 p-2 bg-white/80 rounded-full shadow-lg hover:bg-white transition-colors"
            >
              <ChevronLeft className="w-6 h-6" />
            </button>

            <button
                onClick={nextSlide}
                className="absolute right-4 top-1/2 -translate-y-1/2 p-2 bg-white/80 rounded-full shadow-lg hover:bg-white transition-colors"
            >
              <ChevronRight className="w-6 h-6" />
            </button>

            <div className="absolute bottom-4 left-1/2 -translate-x-1/2 flex space-x-2">
              {carouselImages.map((_, index) => (
                  <button
                      key={index}
                      onClick={() => setCurrentSlide(index)}
                      className={`w-3 h-1 rounded-full ${currentSlide === index ? 'bg-[#4873AB]' : 'bg-white'}`}
                  />
              ))}
            </div>
          </div>
        </div>

        <div className="max-w-7xl mx-auto mt-12 px-4 grid grid-cols-1 md:grid-cols-3 gap-6 pb-12">
          {features.map((feature, index) => (
              <div
                  key={index}

                  className="bg-white rounded-lg shadow-md hover:shadow-lg transition-shadow cursor-pointer overflow-hidden"
              >
                <div className="p-6">
                  <h3 className="text-lg font-semibold text-gray-900 mb-2">{feature.title}</h3>
                  <p className="text-gray-600">{feature.description}</p>
                </div>
              </div>
          ))}
        </div>
      </div>
  );
};

export default Homepage;

