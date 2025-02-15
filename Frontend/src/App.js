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
import ForgotPassword from "./pages/forgotPassword";
import ResetPassword from "./pages/resetPassword";
import JoinOrCreateCommunity from "./pages/JoinOrCreate";
import CreateCommunity from "./pages/CreateCommunity";
import Dashboard from "./pages/Dashboard";
import AdminLayout from "./pages/AdminLayout";
import ResidentLayout from "./pages/ResidentLayout";
import ManagerLayout from "./pages/ManagerLayout";



function App() {
return(
    <Router>
      <Routes>
      
      <Route path="/resident/*" element={<ResidentLayout />}>
          <Route index element={<Dashboard />} />
      </Route>

      <Route path="/admin/*" element={<AdminLayout />}>
          <Route index element={<Dashboard />} />
      </Route>

      <Route path="/manager/*" element={<ManagerLayout />}>
          <Route index element={<Dashboard />} />
      </Route>

       <Route path="/" element={<Homepage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/forgotPassword" element={<ForgotPassword />} />
        <Route path="/resetPassword" element={<ResetPassword />} />
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