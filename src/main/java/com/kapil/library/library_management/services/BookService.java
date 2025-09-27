package com.kapil.library.library_management.services;


import com.kapil.library.library_management.entities.Book;
import com.kapil.library.library_management.exceptions.ResourceNotFoundException;
import com.kapil.library.library_management.repositories.BookRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAllByOrderByIdAsc();
    }

    public Book createBook(Book inputBook) {
        return bookRepository.save(inputBook);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
    }
}
