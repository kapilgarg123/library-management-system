package com.kapil.library.library_management.controllers;

import com.kapil.library.library_management.entities.Book;
import com.kapil.library.library_management.services.BookService;
import lombok.Data;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@RequestMapping(path = "/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping()
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping()
    public Book createBook(@RequestBody Book inputBook) {
        return bookService.createBook(inputBook);
    }

    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.getBookById(id);
    }

}
