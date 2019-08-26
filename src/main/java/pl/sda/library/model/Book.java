package pl.sda.library.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.Objects;

/*
model ksiazki (id, autor, tytul, data do oddania, jesli jest wypozyczona)
 */
public class Book {

    private Long id;
    private String author;
    private String title;
    private LocalDate returnDate;

    public Book(Long id, String author, String title) {
        this.id = id;
        this.author = author;
        this.title = title;
    }

    public Book() {
    }

    @JsonGetter //pole te jest ignorowane przy dodawaniu ksiazki przez kontroler, wiec musimy tutaj zaakcentowac, ze
    //przy wyswietlaniu chcemy to id wyswietlic
    public Long getId() {
        return id;
    }

    @JsonIgnore//nie chcemy podawac w jsonie id wymyslonego przez usera, wiec ignorujemy to pole
    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @JsonGetter//pole te jest ignorowane przy dodawaniu ksiazki przez kontroler, wiec musimy tutaj zaakcentowac, ze
    //przy wyswietlaniu chcemy date oddania ksiazki wyswietlic
    public LocalDate getReturnDate() {
        return returnDate;
    }

    @JsonIgnore//nie chcemy podawac w jsonie daty oddania przy dodawaniu ksiazki, wiec ignorujemy to pole
    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    /*
    wystarczy, ze equals i hashCode wygenerujemy tylko na podstawie id, poniewaz tylko to jest unikalne
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
