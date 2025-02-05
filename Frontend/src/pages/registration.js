import React, { useState } from 'react';
import { UserPlus, Mail, Lock, Eye, EyeOff, Home, Upload, Users} from 'lucide-react';
import SquareImg from '../assets/community.jpeg';
const Register = () => {
  const [formData, setFormData] = useState({
    fullName: '',
    email: '',
    password: '',
    neighborhood: '',
    addressProof: null
    
  });
  
  const [errors, setErrors] = useState({});
  const [message, setMessage] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  
    
  
  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.fullName.trim()) {
      newErrors.fullName = 'Full name is required';
    }
    
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Invalid email address';
    }
    
    if (!formData.password) {
      newErrors.password = 'Password is required';
    } else if (formData.password.length < 6) {
      newErrors.password = 'Password must be at least 6 characters';
    }
    
    if (!formData.neighborhood) {
      newErrors.neighborhood = 'Please select a neighborhood';
    }
    
    if (!formData.addressProof) {
      newErrors.addressProof = 'Address proof is required';
    }

    if (!formData.role) {
      newErrors.role = 'Please select a role';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    setIsSubmitting(true);
    
    try {
      // Simulated API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      setMessage('Registration successful!');
    } catch (error) {
      setMessage('Error during registration');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFormData(prev => ({ ...prev, addressProof: file }));
    }
  };

  return (
    <div className="min-h-screen flex">
     
      {/* Left side - Illustration Section */}
      <div class="hidden lg:flex w-1/2 bg-[#4873AB] p-7 flex-col">
    
    <div class="flex items-center text-white space-x-2">
        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-users h-8 w-8">
            <path d="M16 21v-2a4 4 0 0 0-4-4H6a4 4 0 0 0-4 4v2"></path>
            <circle cx="9" cy="7" r="4"></circle>
            <path d="M22 21v-2a4 4 0 0 0-3-3.87"></path>
            <path d="M16 3.13a4 4 0 0 1 0 7.75"></path>
        </svg>
        <span class="text-2xl font-bold">Community Hub</span>
    </div>

    
    <div class="flex flex-1 flex-col justify-center items-center text-center space-y-8">
        <h1 class="text-4xl font-bold text-white">Join our neighborhood community</h1>
        <p class="text-blue-100 text-lg">Connect with your neighbors, stay updated with local events, and build a stronger community together.</p>
    </div>
</div>


      {/* Right side - Form Section */}
      <div className="flex-1 flex items-center justify-center p-1 bg-gray-50">
        <div className="w-full max-w-md bg-white rounded-lg shadow-md border-2 border-gray-200 dark:border-gray-700">
          <div className="p-6">
            <div className="mb-6">
              <h2 className="text-2xl font-bold text-gray-900">Create an Account</h2>
              <p className="text-gray-600 mt-2">Enter your details to register</p>
            </div>
            
            <form onSubmit={handleSubmit} className="space-y-4">
             
            
              <div>
                <label htmlFor="fullName" className="block text-sm font-medium text-gray-700 mb-2">
                  Full Name
                </label>
                <div className="relative">
                  <UserPlus className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                  <input
                    id="fullName"
                    type="text"
                    className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    placeholder="Name"
                    value={formData.fullName}
                    onChange={(e) => setFormData(prev => ({ ...prev, fullName: e.target.value }))}
                  />
                </div>
                {errors.fullName && (
                  <p className="mt-1 text-sm text-red-500">{errors.fullName}</p>
                )}
              </div>

              <div>
                <label htmlFor="email" className="block text-sm font-medium text-gray-700 mb-2">
                  Email
                </label>
                <div className="relative">
                  <Mail className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                  <input
                    id="email"
                    type="email"
                    className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    placeholder="you@example.com"
                    value={formData.email}
                    onChange={(e) => setFormData(prev => ({ ...prev, email: e.target.value }))}
                  />
                </div>
                {errors.email && (
                  <p className="mt-1 text-sm text-red-500">{errors.email}</p>
                )}
              </div>

              <div>
                <label htmlFor="password" className="block text-sm font-medium text-gray-700 mb-2">
                  Password
                </label>
                <div className="relative">
                  <Lock className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                  <input
                    id="password"
                    type={showPassword ? "text": "password"}
                    className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    placeholder="••••••"
                    value={formData.password}
                    onChange={(e) => setFormData(prev => ({ ...prev, password: e.target.value }))}
                  />
                  <button
                     type="button"
                      className="absolute right-3 top-1/2 -translate-y-1/2 text-gray-400"
                       onClick={() => setShowPassword(!showPassword)}
                  >
                    {showPassword ? (<Eye className="h-4 w-4" />) : (
                      <EyeOff className="h-4 w-4" /> )}          
                  </button>
                  </div>
              </div>
              <div>
              <label htmlFor="neighborhood" className="block text-sm font-medium text-gray-700 mb-2">
                Neighborhood
              </label>
              <div className="relative">
                <Home className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                <input
                  id="neighborhood"
                  type="text"
                  className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500 bg-white"
                  placeholder="Enter your neighborhood"
                  value={formData.neighborhood}
                  onChange={(e) => setFormData(prev => ({ ...prev, neighborhood: e.target.value }))}
                />
              </div>
           
                {errors.neighborhood && (
                  <p className="mt-1 text-sm text-red-500">{errors.neighborhood}</p>
                )}
              </div>

              <div>
                <label htmlFor="addressProof" className="block text-sm font-medium text-gray-700 mb-2">
                  Address Proof
                </label>
                <div className="relative">
                  <Upload className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-gray-400" />
                  <input
                    id="addressProof"
                    type="file"
                    className="w-full pl-10 pr-4 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
                    onChange={handleFileChange}
                  />
                </div>
                {errors.addressProof && (
                  <p className="mt-1 text-sm text-red-500">{errors.addressProof}</p>
                )}
              </div>

              <button
                type="submit"
                disabled={isSubmitting}
                className="w-full bg-[#4873AB] text-white py-2 px-4 rounded-lg hover:bg-[#1e40af] focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isSubmitting ? 'Registering...' : 'Register'}
              </button>
              <p class="mt-2 text-sm text-gray-600">Already have an account? <a class="font-medium text-[#4873AB] hover:text-[#4873AB]" href="/login" data-discover="true">Login</a></p>
              {message && (
                <div className={`p-4 rounded-lg ${message.includes('Error') ? 'bg-red-50 text-red-500' : 'bg-green-50 text-green-500'}`}>
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


export default Register;