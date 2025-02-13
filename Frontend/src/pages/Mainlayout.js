import React, { useState, useEffect } from 'react';
import { Outlet, useNavigate } from 'react-router-dom';
import { Search, Users, HelpCircle, Building, Car, Bell, User, LogOut, X } from 'lucide-react';
import axios from 'axios';

const MainLayout = () => {
  const navigate = useNavigate();
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const neighbourhoodId = localStorage.getItem("neighbourhoodId");
    if (neighbourhoodId) {
      fetchNotifications(neighbourhoodId);
    }
  }, []);

  const fetchNotifications = async (neighbourhoodId) => {
    try {
      const response = await axios.get(`http://localhost:8081/api/help-requests/${neighbourhoodId}`);
      setNotifications(response.data.data);
    } catch (error) {
      console.error("Error fetching notifications:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleNotificationAction = async (id, action) => {
    try {
      const endpoint = action === 'approve'
          ? `http://localhost:8081/api/help-requests/approve/${id}`
      : `http://localhost:8081/api/help-requests/deny/${id}`;
          await axios.post(endpoint);
      setNotifications(notifications.filter(notification => notification.id !== id));
    } catch (error) {
      console.error(`Error ${action} request:`, error);
    }
  };

  return (
      <div className="min-h-screen bg-gray-50">
        <header className="bg-white shadow-md py-4 w-full">
          <div className="max-w-7xl mx-auto px-4">
            <div className="flex items-center justify-between">
              <div className="flex items-center space-x-4 w-full">
                <div className="flex items-center space-x-2">
                  <button onClick={() => navigate('/Dashboard')} className="hover:bg-gray-100 p-1 rounded-lg">
                    <Users className="h-7 w-7 text-[#4873AB]" />
                  </button>
                  <h1 className="text-2xl font-bold text-[#4873AB]">Neighborly</h1>
                </div>

                <div className="relative w-full max-w-md">
                  <input
                      type="text"
                      placeholder="Search..."
                      className="w-full pl-4 pr-12 h-10 rounded-lg border border-gray-300 focus:outline-none focus:ring-2 focus:ring-[#4873AB] focus:border-transparent"
                  />
                  <button className="absolute right-1 top-1/2 -translate-y-1/2 h-8 w-8 p-0 flex items-center justify-center bg-[#4873AB] text-white rounded-md hover:bg-blue-600 transition-colors">
                    <Search className="w-4 h-4" />
                  </button>
                </div>

                <div className="flex items-center space-x-4">
                  <button
                      onClick={() => setIsNotificationsOpen(!isNotificationsOpen)}
                      className="flex flex-col items-center px-3 py-1 hover:bg-gray-100 rounded-lg transition-colors mx-1"
                      title="Notifications"
                  >
                    <Bell className="w-6 h-6 text-[#4873AB]" />
                    <span className="text-[10px] text-gray-600 mt-0.5 whitespace-nowrap">Notifications</span>
                  </button>
                </div>
              </div>
            </div>
          </div>
        </header>

        {isNotificationsOpen && (
            <div className="fixed inset-0 bg-black opacity-50 z-40" onClick={() => setIsNotificationsOpen(false)} />
        )}

        <div className={`fixed top-0 right-0 h-full w-64 bg-white shadow-lg transform transition-transform duration-300 ease-in-out z-50 ${isNotificationsOpen ? 'translate-x-0' : 'translate-x-full'}`}>
          <div className="p-4 max-h-full overflow-y-auto">
            <h3 className="text-lg font-semibold">Notifications</h3>
            {loading ? (
                <p>Loading...</p>
            ) : notifications.length === 0 ? (
                <p>No pending requests.</p>
            ) : (
                notifications.map(notification => (
                    <div key={notification.id} className="flex justify-between items-center p-2 hover:bg-gray-100 rounded-lg">
                      <div>
                        <p className="font-semibold">{notification.requestType}</p>
                        <p className="text-sm text-gray-600">{notification.user.name} wants to join the community.</p>
                      </div>
                      <div className="flex space-x-2">
                        <button
                            onClick={() => handleNotificationAction(notification.id, 'approve')}
                            className="text-green-600 hover:bg-green-100 px-3 py-1 rounded-lg"
                        >
                          Approve
                        </button>
                        <button
                            onClick={() => handleNotificationAction(notification.id, 'deny')}
                            className="text-red-600 hover:bg-red-100 px-3 py-1 rounded-lg"
                        >
                          Deny
                        </button>
                      </div>
                    </div>
                ))
            )}
          </div>
        </div>

        <main className="pt-6">
          <Outlet />
        </main>
      </div>
  );
};

export default MainLayout;