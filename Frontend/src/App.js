import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// import App from "./App";
import Register from "./pages/registration";
// import About from "./pages/about";
import Login from "./pages/login";
import AdminDashboard from "./pages/admin";
import "./index.css";
import Dashboard from "./pages/dashboard";
import ForgotPassword from "./pages/forgot_password";
import ResetPassword from "./pages/reset_password";

function App() {
ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Dashboard/>} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />
        <Route path="/reset-password" element={<ResetPassword />} />
       

      </Routes>
    </Router>
  </React.StrictMode>

)}
 export default App;
//  <Route path="/about" element={<About />} />