package com.example.ss_day2_ex1.controller;

import com.example.ss_day2_ex1.dto.Book;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/api/v1/product")
public class DemoController {

    @GetMapping("/write")
    public String getWright() {
        return "GET Write";
    }

    @GetMapping("/read")
    public String read() {
        return "Read";
    }

    @PostMapping("/write")
    public String postWrite() {
        return "POST Write";
    }

    @PostMapping("/book")
    @PreAuthorize("@bookPrivilegeChecker.validateBookAdd(#book)") // SpEL are access parametri
//    @PreFilter("hasRole('ADMIN')")
    public void addBook(@RequestBody Book book) {
        IO.println("Adding book");
    }

    @GetMapping("/book")
    @PostAuthorize("returnObject.owner() == authentication.principal.username")
    public Book findBook() {
        return new Book("Harry Potter", "john");
    }

    @PostMapping("/products")
    @PreFilter("filterObject.owner() == authentication.principal.username")
    public void addProducts(@RequestBody ArrayList<Book> products) {  // liste mutabile, arrays
        IO.println(products);
    }

    @GetMapping("/products")
    @PostFilter("filterObject.owner() == authentication.principal.username")
    public List<Book> addProducts() {  // liste mutabile, arrays
        ArrayList<Book> books = new ArrayList<>();
        books.add(new Book("A", "bob"));
        books.add(new Book("B", "john"));
        books.add(new Book("C", "bob"));
        return books;
    }
}
