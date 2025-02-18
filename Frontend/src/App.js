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
import MainLayout from "./pages/Mainlayout";



function App() {
    return(
        <Router>
            <Routes>
                <Route path="/" element={<Homepage />} />
                <Route path= "/MainLayout" element={<MainLayout />}/>

                <Route path="/communitymanager" element={<Communitymanager />} />

                <Route path="/MainLayout" element={<MainLayout />} />

                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/admin" element={<AdminDashboard />} />
                <Route path="/forgotPassword" element={<ForgotPassword />} />
                <Route path="/resetPassword" element={<ResetPassword />} />
                <Route path="/JoinOrCreate" element={<JoinOrCreateCommunity />} />
                <Route path="/CreateCommunity" element={<CreateCommunity />} />
                <Route path="/JoinCommunity" element={<JoinCommunity />} />

            </Routes>
        </Router>
    );
}

ReactDOM.createRoot(document.getElementById("root")).render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);

export default App;
//  <Route path="/about" element={<About />} />