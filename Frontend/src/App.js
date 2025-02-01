import React from "react";
import ReactDOM from "react-dom/client";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
// import App from "./App";
import Register from "./pages/registration";
// import About from "./pages/about";
import Login from "./pages/login";
import "./index.css";

function App() {
ReactDOM.createRoot(document.getElementById("root")).render(
  <React.StrictMode>
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/" element={<Login />} />
        <Route path="/register" element={<Register />} />
       

      </Routes>
    </Router>
  </React.StrictMode>

)}
 export default App;
//  <Route path="/about" element={<About />} />