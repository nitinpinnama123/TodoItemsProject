import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './App.css' // Import your themes and layout styles

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)