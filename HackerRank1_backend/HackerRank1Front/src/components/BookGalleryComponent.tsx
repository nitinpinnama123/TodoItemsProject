import React, { use, useEffect } from 'react'
import type { Book } from '../App';
import ReactPaginate from 'react-paginate';

function BookGalleryComponent() {
  const [books, setBooks] = React.useState<Book[]>([]);

  const [page, setPage] = React.useState(0);
  const [pageContent, setPageContent] = React.useState<Book[]>([]);
  const itemsPerPage = 10;
    async function fetchBooks(e: React.FormEvent<HTMLFormElement>)
    {
        e.preventDefault();
        const params = new URLSearchParams();
        params.append('author', "");
        params.append('genre', "");
        const response = await fetch(`http://localhost:8083/books?${params}`,{
            method: 'GET',

        });
        console.log("Response Status: ", response.status);
        const data = await response.json();
        console.log("Fetched Books: ", data);
        setBooks(data);
    }
    setPageContent(books.slice(page * itemsPerPage, (page + 1) * itemsPerPage));

    return (
      <div>
        {(books.length) > 0 ? (
            <div>
          <ul>
            {pageContent.map((book: Book, index) => (
              <li key={index}>
                <h3>{book.title}</h3>
                <p>Author: {book.author}</p>
                <p>Genre: {book.genre}</p>
                <p>Published Year: {book.publishedYear}</p>
                <p>Pages: {book.pages}</p>
              </li>
            ))}
          </ul>
          <button onClick={() => setPage(page - 1)} disabled={page === 0}>
            Previous
          </button>

            <button onClick={() => setPage(page + 1)} disabled={(page + 1) * itemsPerPage >= books.length}>
              Next
            </button>

            </div>
        ) : (
          <p>No books available.</p>
        )}

      </div>
    )
}

export default BookGalleryComponent