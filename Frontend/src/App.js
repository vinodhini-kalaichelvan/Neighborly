import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// import App from "./App";
import Register from "./pages/registration";
// import About from "./pages/about";
import Login from "./pages/login";
import AdminDashboard from "./pages/admin";
import "./index.css";
import ForgotPassword from "./pages/forgot_password";
import ResetPassword from "./pages/reset_password";

function App() {
ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/forgot_password" element={<ForgotPassword />} />
        <Route path="/reset_password" element={<ResetPassword />} />
       

      </Routes>
    </Router>
  </React.StrictMode>

)}
 export default App;
//  <Route path="/about" element={<About />} />