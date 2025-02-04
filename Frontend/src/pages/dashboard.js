import React, { useState, useEffect } from 'react';
import { ChevronLeft, ChevronRight, Search, UserPlus, LogIn, Calendar, Share2, Users, HandHelping } from 'lucide-react';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';

const Dashboard = () => {
  const [currentSlide, setCurrentSlide] = useState(0);
  
  const carouselImages = [
    "/api/placeholder/1200/400",
    "/api/placeholder/1200/400",
    "/api/placeholder/1200/400"
  ];

  const features = [
    {
      title: "Event Announcements",
      description: "Find and join upcoming events in your neighborhood.",
      link: "/events"
    },
    {
      title: "Rentals & Bookings",
      description: "Easily book or rent spaces and items locally.",
      link: "/rentals"
    },
    {
      title: "Resource Sharing",
      description: "Share or borrow tools, skills, and services.",
      link: "/resources"
    }
  ];

  const welcomeFeatures = [
    { icon: Calendar, text: "Local Events" },
    { icon: Share2, text: "Resource Sharing" },
    { icon: Users, text: "Community Building" },
    { icon: HandHelping, text: "Help Exchange" }
  ];

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % carouselImages.length);
    }, 60000);
    return () => clearInterval(timer);
  }, []);

  const nextSlide = () => {
    setCurrentSlide((prev) => (prev + 1) % carouselImages.length);
  };

  const prevSlide = () => {
    setCurrentSlide((prev) => (prev - 1 + carouselImages.length) % carouselImages.length);
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white shadow-md py-4">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex items-center justify-between space-x-4">
            <h1 className="text-2xl font-bold text-blue-600 whitespace-nowrap">
              Neighborly
            </h1>
            
            <div className="relative max-w-md w-full">
              <Input
                type="text"
                placeholder="Search..."
                className="w-full pl-4 pr-12 h-10"
              />
              <Button 
                className="absolute right-1 top-1/2 -translate-y-1/2 h-8 w-8 p-0"
                size="sm"
              >
                <Search className="w-4 h-4" />
              </Button>
            </div>
            
            <div className="flex items-center space-x-2">
              <Button 
                variant="outline" 
                className="flex items-center space-x-1 h-10"
                size="sm"
              >
                <UserPlus className="w-4 h-4" />
                <span>New User?</span>
              </Button>
              <Button 
                className="flex items-center space-x-1 h-10 bg-blue-600 hover:bg-blue-700"
                size="sm"
              >
                <LogIn className="w-4 h-4" />
                <span>Already a Neighbour?</span>
              </Button>
            </div>
          </div>
        </div>
      </header>

      <div className="bg-blue-50 py-8">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <h2 className="text-2xl font-bold text-blue-800 mb-4">Welcome to Neighborly</h2>
          <p className="text-gray-700 max-w-2xl mx-auto mb-8">
            Your community hub for local events, resource sharing, and building stronger neighborhood connections. 
            Join us to discover local activities, share resources, and connect with neighbors.
          </p>
          
          <div className="flex justify-center flex-wrap gap-8 mt-6">
            {welcomeFeatures.map((feature, index) => (
              <div key={index} className="flex flex-col items-center">
                <div className="w-12 h-12 rounded-full bg-blue-100 flex items-center justify-center mb-2">
                  <feature.icon className="w-6 h-6 text-blue-600" />
                </div>
                <span className="text-sm font-medium text-gray-700">{feature.text}</span>
              </div>
            ))}
          </div>
        </div>
      </div>

      <div className="relative max-w-7xl mx-auto mt-6">
        <div className="relative h-[400px] overflow-hidden rounded-xl">
          <img
            src={carouselImages[currentSlide]}
            alt={`Slide ${currentSlide + 1}`}
            className="w-full h-full object-cover"
          />
          
          <button
            onClick={prevSlide}
            className="absolute left-4 top-1/2 -translate-y-1/2 p-2 bg-white/80 rounded-full shadow-lg"
          >
            <ChevronLeft className="w-6 h-6" />
          </button>
          
          <button
            onClick={nextSlide}
            className="absolute right-4 top-1/2 -translate-y-1/2 p-2 bg-white/80 rounded-full shadow-lg"
          >
            <ChevronRight className="w-6 h-6" />
          </button>

          <div className="absolute bottom-4 left-1/2 -translate-x-1/2 flex space-x-2">
            {carouselImages.map((_, index) => (
              <button
                key={index}
                onClick={() => setCurrentSlide(index)}
                className={`w-3 h-3 rounded-full ${
                  currentSlide === index ? 'bg-blue-600' : 'bg-white'
                }`}
              />
            ))}
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto mt-12 px-4 grid grid-cols-1 md:grid-cols-3 gap-6 pb-12">
        {features.map((feature, index) => (
          <Card 
            key={index}
            className="hover:shadow-lg transition-shadow cursor-pointer"
            onClick={() => window.location.href = feature.link}
          >
            <CardHeader>
              <CardTitle>{feature.title}</CardTitle>
            </CardHeader>
            <CardContent>
              <p className="text-gray-600">{feature.description}</p>
            </CardContent>
          </Card>
        ))}
      </div>
    </div>
  );
};

export default Dashboard;