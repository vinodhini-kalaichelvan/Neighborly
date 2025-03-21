import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Bell, Users, Search, HandHelping, ParkingSquare, Building2, UserCircle, X } from "lucide-react";
import axios from "axios";

const CommunityManager = () => {
    const navigate = useNavigate();
    const [isNotificationsOpen, setIsNotificationsOpen] = useState(false);
    const [isProfileMenuOpen, setIsProfileMenuOpen] = useState(false);
    const [notifications, setNotifications] = useState([]);
    const [postNotifications, setPostNotifications] = useState([]);
    const [actionMessage, setActionMessage] = useState("");
    const [unreadCount, setUnreadCount] = useState(0);
    const [selectedTab, setSelectedTab] = useState("join");

    const token = localStorage.getItem('token');
    const neighbourhoodId = localStorage.getItem("neighbourhoodId");

    useEffect(() => {
        // Fetch notifications and post notifications
        const fetchData = async () => {
            try {
                // Fetch notifications
                const notificationsResponse = await axios.get(
                    `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_NOTIFICATIONS_ENDPOINT}/${neighbourhoodId}`,
                    {
                        headers: { 
                            Authorization: `Bearer ${token}`
                        }
                    }
                );
                setNotifications(notificationsResponse.data);

                // Fetch post notifications
                const postNotificationsResponse = await axios.get(
                    `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_DISAPPROVED_POSTS_ENDPOINT}/${neighbourhoodId}`,
                    {
                        headers: { 
                            Authorization: `Bearer ${token}`
                        }
                    }
                );
                const posts = postNotificationsResponse.data.data;
                setPostNotifications(posts);

                // Update unread count
                const newUnreadCount = notificationsResponse.data.length + posts.length;
                setUnreadCount(newUnreadCount);

                // Store the unread count in localStorage
                localStorage.setItem("unreadCount", newUnreadCount);
            } catch (error) {
                console.error("Error fetching notifications:", error);
            }
        };

        if (neighbourhoodId) {
            fetchData();
        } else {
            console.log("Neighbourhood ID is missing.");
        }
    }, [neighbourhoodId]);

    useEffect(() => {
        // Retrieve the stored unread count from localStorage
        const storedUnreadCount = localStorage.getItem("unreadCount");
        if (storedUnreadCount) {
            setUnreadCount(Number(storedUnreadCount));
        }
    }, []);

    const handleNotificationAction = async (id, action) => {
        try {
            const endpoint = action === 'approve'
                ? `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_JOIN_COMMUNITY_APPROVE_ENDPOINT}/${id}`
                : `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_JOIN_COMMUNITY_DENY_ENDPOINT}/${id}`;

            await axios.post(endpoint, {}, {
                headers: { 
                    Authorization: `Bearer ${token}`
                }
            });
            setActionMessage(`${action.charAt(0).toUpperCase() + action.slice(1)} joining successfully`);
            setNotifications(notifications.filter(notification => notification.requestId !== id));
            setUnreadCount(prev => Math.max(0, prev - 1));
            setTimeout(() => setActionMessage(""), 3000);
        } catch (error) {
            console.error(`Error ${action} request:`, error);
        }
    };

    const handlePostAction = async (id, action) => {
        try {
            let endpoint;
            if (action === 'approve') {
                endpoint = `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_APPROVE_POST_ENDPOINT}/${id}`;
            } else if (action === 'deny') {
                endpoint = `${process.env.REACT_APP_API_BASE_URL}${process.env.REACT_APP_DELETE_POST_ENDPOINT}/${id}`;
            }

            const response = await axios({
                method: action === 'approve' ? 'put' : 'delete', // Use PUT for approve, DELETE for deny
                url: endpoint,
                headers: { 
                    Authorization: `Bearer ${token}`
                }
            });

            if (response.data.result === "SUCCESS") {
                setActionMessage(`${action.charAt(0).toUpperCase() + action.slice(1)} post successfully`);
                setPostNotifications(postNotifications.filter(post => post.postId !== id)); // Remove the post from the list
                setUnreadCount(prev => Math.max(0, prev - 1)); // Decrease unread count
            } else {
                setActionMessage(`Failed to ${action} post. Please try again.`);
            }
        } catch (error) {
            console.error(`Error ${action} post:`, error);
            setActionMessage(`Failed to ${action} post. Please try again.`);
        } finally {
            setTimeout(() => setActionMessage(""), 3000); // Clear the message after 3 seconds
        }
    };

    const handleLogout = () => {
        localStorage.clear();
        navigate("/login");
    };

    return (
        <div className="min-h-screen bg-gray-50">
            <header className="bg-white shadow-md py-4 w-full">
                <div className="max-w-7xl mx-auto px-4 flex items-center justify-between">
                    <div className="flex items-center space-x-4 w-full">
                        <button onClick={() => navigate('/')} className="hover:bg-gray-100 p-1 rounded-lg">
                            <Users className="h-7 w-7 text-[#4873AB]" />
                        </button>

                        <h1 className="text-2xl font-bold text-[#4873AB] cursor-pointer" onClick={() => navigate('/')}>
                            Neighborly
                        </h1>

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

                        <div className="flex items-center space-x-6">
                            <button onClick={() => navigate("/help-requests")} className="hover:bg-gray-100 p-2 rounded-lg flex items-center space-x-2">
                                <HandHelping className="w-6 h-6 text-[#4873AB]" />
                                <span className="text-sm font-medium text-gray-700">Help Requests</span>
                            </button>

                            <button onClick={() => navigate("/parking-rentals")} className="hover:bg-gray-100 p-2 rounded-lg flex items-center space-x-2">
                                <ParkingSquare className="w-6 h-6 text-[#4873AB]" />
                                <span className="text-sm font-medium text-gray-700">Parking</span>
                            </button>

                            <button onClick={() => navigate("/public-bookings")} className="hover:bg-gray-100 p-2 rounded-lg flex items-center space-x-2">
                                <Building2 className="w-6 h-6 text-[#4873AB]" />
                                <span className="text-sm font-medium text-gray-700">Public Places</span>
                            </button>

                            <button
                                onClick={() => setIsNotificationsOpen(!isNotificationsOpen)}
                                className="relative hover:bg-gray-100 p-2 rounded-lg flex items-center space-x-2 group"
                            >
                                <div className="relative">
                                    <Bell className="w-6 h-6 text-[#4873AB]" />
                                    {unreadCount > 0 && (
                                        <div className="absolute -top-2 -right-2 bg-red-500 text-white text-xs rounded-full min-w-[20px] h-5 flex items-center justify-center px-1 transform transition-transform group-hover:scale-110">
                                            {unreadCount}
                                        </div>
                                    )}
                                </div>
                                <span className="text-sm font-medium text-gray-700">Notifications</span>
                            </button>

                            <div className="relative">
                                <button
                                    onClick={() => setIsProfileMenuOpen(!isProfileMenuOpen)}
                                    className="hover:bg-gray-100 p-2 rounded-lg flex items-center space-x-2"
                                    title="Profile"
                                >
                                    <UserCircle className="w-7 h-7 text-[#4873AB]" />
                                </button>

                                {isProfileMenuOpen && (
                                    <div className="absolute right-0 mt-2 w-40 bg-white shadow-md rounded-lg py-2 z-50">
                                        <button
                                            onClick={handleLogout}
                                            className="block w-full text-left px-4 py-2 text-sm text-red-600 hover:bg-gray-100"
                                        >
                                            Logout
                                        </button>
                                    </div>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="flex justify-center items-center min-h-screen bg-blue-50">
                <div className="text-center -mt-20">
                    <h2 className="text-4xl font-bold text-gray-800">Welcome, Community Manager! </h2>
                    <p className="text-gray-600 mt-4 text-lg">Manage your neighborhood effortlessly with ease and efficiency.</p>
                </div>
            </main>

            {isNotificationsOpen && (
                <div className="fixed inset-0 bg-black/30 backdrop-blur-sm z-40" onClick={() => setIsNotificationsOpen(false)} />
            )}

            <div
                className={`fixed top-0 right-0 h-full w-80 bg-white shadow-lg transform transition-transform duration-300 ease-in-out z-50 ${
                    isNotificationsOpen ? "translate-x-0" : "translate-x-full"
                }`}
            >
                <div className="flex items-center justify-between p-4 border-b border-gray-200">
                    <h3 className="text-lg font-semibold text-gray-800">Notifications</h3>
                    <button
                        onClick={() => setIsNotificationsOpen(false)}
                        className="p-1 hover:bg-gray-100 rounded-full"
                    >
                        <X className="w-5 h-5 text-gray-500" />
                    </button>
                </div>

                <div className="p-4 max-h-[calc(100vh-5rem)] overflow-y-auto">
                    <div className="flex space-x-4 mb-4">
                        <button
                            onClick={() => setSelectedTab("join")}
                            className={`px-4 py-2 rounded-lg ${selectedTab === "join" ? "bg-[#4873AB] text-white" : "text-[#4873AB]"}`}
                        >
                            Join Requests
                        </button>
                        <button
                            onClick={() => setSelectedTab("post")}
                            className={`px-4 py-2 rounded-lg ${selectedTab === "post" ? "bg-[#4873AB] text-white" : "text-[#4873AB]"}`}
                        >
                            Post Approvals
                        </button>
                    </div>

                    {selectedTab === "join" && (
                        <div className="space-y-4">
                            {notifications.length === 0 ? (
                                <p>No new join requests.</p>
                            ) : (
                                notifications.map((notification) => (
                                    <div key={notification.requestId} className="p-4 border border-gray-200 rounded-lg">
                                        <div className="flex items-center justify-between">
                                            <div className="font-medium text-gray-700">
                                                {notification.user.name} wants to join your neighborhood.
                                            </div>
                                        </div>
                                        <div className="mt-2 flex space-x-4">
                                            <button
                                                onClick={() => handleNotificationAction(notification.requestId, "approve")}
                                                className="flex-1 bg-[#4873AB] text-white px-4 py-2 rounded-lg hover:bg-[#3b5d89] transition-colors"
                                            >
                                                Approve
                                            </button>
                                            <button
                                                onClick={() => handleNotificationAction(notification.requestId, "deny")}
                                                className="flex-1 border border-red-500 text-red-500 px-4 py-2 rounded-lg hover:bg-red-50 transition-colors"
                                            >
                                                Deny
                                            </button>
                                        </div>
                                    </div>
                                ))
                            )}
                        </div>
                    )}

                    {selectedTab === "post" && (
                        <div className="space-y-4">
                            {postNotifications.length === 0 ? (
                                <p>No new posts to approve.</p>
                            ) : (
                                postNotifications.map((post) => (
                                    <div key={post.postId} className="p-4 border border-gray-200 rounded-lg">
                                        <div className="flex items-center justify-between">
                                            <div className="font-medium text-gray-700">
                                                {post.postType} - {post.postContent}
                                            </div>
                                        </div>
                                        <div className="mt-2 flex space-x-4">
                                            <button
                                                onClick={() => handlePostAction(post.postId, "approve")}
                                                className="flex-1 bg-[#4873AB] text-white px-4 py-2 rounded-lg hover:bg-[#3b5d89] transition-colors"
                                            >
                                                Approve
                                            </button>
                                            <button
                                                onClick={() => handlePostAction(post.postId, "deny")}
                                                className="flex-1 border border-red-500 text-red-500 px-4 py-2 rounded-lg hover:bg-red-50 transition-colors"
                                            >
                                                Deny
                                            </button>
                                        </div>
                                    </div>
                                ))
                            )}
                        </div>
                    )}

                    {actionMessage && (
                        <div
                            className={`fixed top-4 right-4 text-white px-4 py-2 rounded-lg shadow-md ${
                                actionMessage.includes("successfully") ? "bg-green-500" : "bg-red-500"
                            }`}
                        >
                            {actionMessage}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};

export default CommunityManager;