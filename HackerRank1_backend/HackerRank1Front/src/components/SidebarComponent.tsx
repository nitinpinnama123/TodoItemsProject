import React from 'react'
import type { Book } from '../App';

function SidebarComponent({setBooks}:any,books:Book[]) {
    const [author, setAuthor] = React.useState("");
        const [genre, setGenre] = React.useState("");

    async function fetchBooks(e: React.FormEvent<HTMLFormElement>
        ) {
            e.preventDefault();
            const params = new URLSearchParams();
            params.append('author', author);
            params.append('genre', genre);
            const response = await fetch(`http://localhost:8083/books?${params}`,{
                method: 'GET',

            });
            console.log("Response Status: ", response.status);
            const data = await response.json();
            console.log("Fetched Books: ", data);
            setBooks(data);
        }
  return (
    <div>
          <form onSubmit={fetchBooks}>
              <label> Author: <select value={author} onChange={(e) => setAuthor(e.target.value)} >
                    <option value="">All Authors</option>
                    {books.map((book: Book, index) => (
                      <option key={index} value={book.author}>
                        {book.author}
                      </option>
                    ))}
                  </select>
              </label>
              <label> Genre: <select value={genre} onChange={(e) => setGenre(e.target.value)} />

              </label>
              <label> Genre: <select value={genre} onChange={(e) => setGenre(e.target.value)} >
                    <option value="">All Genres</option>
                    {books.map((book: Book, index) => (
                      <option key={index} value={book.genre}>
                        {book.genre}
                        </option>
                    ))}
                    </select>
              </label>
                  <button type='submit'>Search</button>
          </form>
        </div>
  )
}

export default SidebarComponent