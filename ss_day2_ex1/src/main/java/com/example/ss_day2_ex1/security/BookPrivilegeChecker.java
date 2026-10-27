package com.example.ss_day2_ex1.security;

import com.example.ss_day2_ex1.dto.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class BookPrivilegeChecker {

    @Value("${flag:'abc'}")
    private String flag;

    public boolean validateBookAdd(Book book) {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails p = (CustomUserDetails) a.getPrincipal();

        return p.getUsername().equals(book.owner());
    }
}
