import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// import App from "./App";
import Register from "./pages/registration";
import Login from "./pages/login";
import AdminDashboard from "./pages/admin";
import "./index.css";
import Homepage from "./pages/Homepage";
import ForgotPassword from "./pages/forgotPassword";
import ResetPassword from "./pages/resetPassword";
import JoinOrCreateCommunity from "./pages/JoinOrCreate";
import Communitymanager from "./pages/communitymanager";
import CreateCommunity from "./pages/CreateCommunity";
import JoinCommunity from "./pages/JoinCommunity";
import Resident from "./pages/resident";
import ViewPosts from "./pages/ViewPosts";
import CreatePost from "./pages/CreatePost";
import EditPost from "./pages/EditPost";
import BrowseParkingSpaces from './pages/BrowseParkingSpaces';
import ParkingHistoryPage from './pages/ParkingHistoryPage';

import ParkingSpaceListing from "./pages/ParkingSpaceListing";

function App() {
    return(
        <div>
            <Router>
                <Routes>
                    <Route path="/" element={<Homepage />} />
                    <Route path="/communitymanager" element={<Communitymanager />} />
                    <Route path="/login" element={<Login />} />
                    <Route path="/register" element={<Register />} />
                    <Route path="/admin" element={<AdminDashboard />} />
                    <Route path="/forgotPassword" element={<ForgotPassword />} />
                    <Route path="/resetPassword" element={<ResetPassword />} />
                    <Route path="/JoinOrCreate" element={<JoinOrCreateCommunity />} />
                    <Route path="/CreateCommunity" element={<CreateCommunity />} />
                    <Route path="/JoinCommunity" element={<JoinCommunity />} />
                    <Route path="/resident" element={<Resident />} />
                    <Route path="/viewpost" element={<ViewPosts />} />
                    <Route path="/post" element={<CreatePost />} />
                    <Route path="/listParking" element={<ParkingSpaceListing />}/>
                    <Route path="/browseSpaces" element={<BrowseParkingSpaces />}/>
                    <Route path="/parkingHistory" element={<ParkingHistoryPage />}/>
                    
                    <Route path="/editpost/:postId" element={<EditPost />} /> {/* Correct usage of 'element' */}
                </Routes>
            </Router>
        </div>
    );
}

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);

export default App;
