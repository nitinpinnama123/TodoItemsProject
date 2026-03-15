import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
//import { AuthProvider } from './context/AuthContext';
//import PrivateRoute from './components/PrivateRoute';
//import Login from './components/Auth/Login';
//import Register from './components/Auth/Register';
//import TaskList from './components/Tasks/TaskList';
import './App.css';

function App() {
  return (
  /*

    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/tasks" element={
            <PrivateRoute>
              <TaskList />
            </PrivateRoute>
          } />
          <Route path="/" element={<Navigate to="/login" replace />} />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
    */
    <h1>React App</h1>
  );
}

export default App;
