import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import './App.css'

function App() {
  return (
    <Router>
      <div className="app">
        <Routes>
          <Route path="/" element={<div>Home Page</div>} />
          <Route path="/dashboard" element={<div>Dashboard</div>} />
          <Route path="/login" element={<div>Login</div>} />
          <Route path="/signup" element={<div>Sign Up</div>} />
          <Route path="*" element={<div>404 Not Found</div>} />
        </Routes>
      </div>
    </Router>
  )
}

export default App
