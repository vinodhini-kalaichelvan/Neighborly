import React, { useState, useEffect } from 'react';
import { ChevronLeft, ChevronRight, Calendar, Share2, Users, HandHelping } from 'lucide-react';

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
    { title: "Help Requests", description: "Request or offer tools, skills, and services.", link: "/login" },
    { title: "Booking Public Spaces", description: "Easily book or rent spaces across neighborhoods.", link: "/login" },
    { title: "Parking Rentals", description: "Easily share or rent parking spaces across neighborhoods hassle-free.", link: "/login" }
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

  return (
    <div className="min-h-screen bg-gray-50">
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
                className={`w-3 h-1 rounded-full ${
                  currentSlide === index ? 'bg-[#4873AB]' : 'bg-white'
                }`}
              />
            ))}
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto mt-12 px-4 grid grid-cols-1 md:grid-cols-3 gap-6 pb-12">
        {features.map((feature, index) => (
          <div 
            key={index}
            onClick={() => window.location.href = feature.link}
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