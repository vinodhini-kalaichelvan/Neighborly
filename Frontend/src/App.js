import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// import App from "./App";
import Register from "./pages/registration";
// import About from "./pages/about";
import Login from "./pages/login";
import AdminDashboard from "./pages/admin";
import "./index.css";
import Homepage from "./pages/Homepage";
import ForgotPassword from "./pages/forgot_password";
import ResetPassword from "./pages/reset_password";
import JoinOrCreateCommunity from "./pages/JoinOrCreate";

import CreateCommunity from "./pages/CreateCommunity";

function App() {
return(
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/" element={<Homepage/>} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/forgot_password" element={<ForgotPassword />} />
        <Route path="/reset_password" element={<ResetPassword />} />
        <Route path="/JoinOrCreate" element={<JoinOrCreateCommunity />} />    
        <Route path="/CreateCommunity" element={<CreateCommunity />} />
       
       

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