import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// import App from "./App";
import Register from "./pages/registration";
import Login from "./pages/login";
import AdminDashboard from "./pages/admin";
import "./index.css";
import MainLayout from "./pages/Mainlayout";
import Homepage from "./pages/Homepage";
import ForgotPassword from "./pages/forgot_password";
import ResetPassword from "./pages/reset_password";
import JoinOrCreateCommunity from "./pages/JoinOrCreate";
import Communitymanager from "./pages/communitymanager";
import CreateCommunity from "./pages/CreateCommunity";
import JoinCommunity from "./pages/JoinCommunity";




function App() {
    return(
        <Router>
            <Routes>

                <Route path= "/" element={<MainLayout />}>

                    <Route path="/communitymanager" element={<Communitymanager />} />
                </Route>
                <Route path="/MainLayout" element={<MainLayout />} />
                <Route path="/Homepage" element={<Homepage />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/admin" element={<AdminDashboard />} />
                <Route path="/forgot_password" element={<ForgotPassword />} />
                <Route path="/reset_password" element={<ResetPassword />} />
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