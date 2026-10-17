package org.example.individual7.book;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BookController {

    private final BookStore bookStore;

    public BookController(BookStore bookStore) {
        this.bookStore = bookStore;
    }

    @GetMapping("/books")
    public String getAllBooks(
            Model model,
            org.springframework.security.core.Authentication authentication) {

        model.addAttribute("books", bookStore.findAll());

        boolean isAdmin = authentication != null
                && org.springframework.security.core.authority.AuthorityUtils
                .authorityListToSet(authentication.getAuthorities())
                .contains("ROLE_ADMIN");

        model.addAttribute("isAdmin", isAdmin);

        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }

        return "books";
    }

    @GetMapping("/books/new")
    public String showBookForm(Model model) {
        model.addAttribute("bookRequest",
                new BookRequest("", 2026, "", ""));
        return "book-form";
    }

    @PostMapping("/books")
    public String createBook(
            @Valid @ModelAttribute("bookRequest") BookRequest bookRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "book-form";
        }

        Book book = new Book(
                null,
                bookRequest.title(),
                bookRequest.author(),
                bookRequest.genre(),
                bookRequest.year()
        );

        bookStore.save(book);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Книгу успішно додано!");

        return "redirect:/books";
    }
}
