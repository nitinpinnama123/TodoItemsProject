import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'

import BookGalleryComponent from './components/BookGalleryComponent'
function App() {


  return (
    <>
      <BookGalleryComponent />
    </>
  )
}

export interface Book {
  title: string;
  author: string;
  genre: string;
  publishedYear: number;
  pages: number;
}


export default App