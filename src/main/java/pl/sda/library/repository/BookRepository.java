package pl.sda.library.repository;

import org.springframework.stereotype.Repository;
import pl.sda.library.model.Book;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/*
repozytorium springowe (zarzadzane przez springa dzieki adnotacji @Repository)
Warswa repozytoriow wg sztuki sluzy jako miejsce dla wykonywania operacji na bazie danych - w naszym wypadku na secie, ktory
imituje bazy danych
 */
@Repository
public class BookRepository {

    private Set<Book> books = initializeBookRepository();

    //inicjalizujemy sobie Set poczatkowy z ksiazkami
    private Set<Book> initializeBookRepository() {
        Book book = new Book(1L, "Tomasz Kaczanowski", "Testy");
        Book book2 = new Book(2L, "Henryk Sienkiewicz", "Potop");
        Book book3 = new Book(3L, "Adam Mickiewicz", "Dziady");
        Book book4 = new Book(4L, "JK Rowling", "Harry Potter");
        Book book5 = new Book(5L, "Adam Mickiewicz", "Pan Tadeusz");
        return new HashSet<>(Arrays.asList(book, book2, book3, book4, book5));
    }

    //metoda do zwracania setu ksiazek
    public Set<Book> getBooks(String title) {
        if (title == null) { //jesli tytul jest nullem to nie filtrujemy po nim i zwracamy liste wszystkich ksiazek w Secie
            return books;
        } else {
            return books.stream()
                    .filter(book -> book.getTitle().equals(title))//jesli tytul nie jest nullem to filtrujemy po nim
                    .collect(Collectors.toSet());//agregujemy do setu, poniewaz metoda zwraca Set<Book>
        }
    }

    //metoda do wypozyczenia ksiazki na podstawie id i z data zwrotu wyliczona przez serwis
    public Optional<Book> rentBook(Long id, LocalDate returnDate) {
        Optional<Book> bookOptional = books.stream()
                .filter(book -> book.getId().equals(id))//wyszukujemy ksiazke o przekazanym id
                .filter(book -> book.getReturnDate()==null)//sprawdzamy czy ksiazka dana nie jest juz wypozyczona. Jesli ma date do oddania tzn, ze aktualnie jest w wypozyczeniu.
                .findAny();
        bookOptional.ifPresent(book -> book.setReturnDate(returnDate));//jesli ksiazka sie znalazla to ustawiamy ja jako wypozyczona dodajac jej date do kiedy mamy ja oddac
        return bookOptional;//zwracamy Optional z tej ksiazki, poniewaz mozliwe, ze jej nie znalezlismy
    }

    //metoda do dodania ksiazki do seta
    public Book addBook(Book book) {
        book.setId(nextId());//ustawiamy dodawanej ksiazce nastepne wolne id
        books.add(book);//dodajemy do seta
        return book;//zwracamy ksiazke z ustawionym id
    }

    //metoda do generowania nastepnego, wolnego id
    private Long nextId() {
        return books.stream()
                .mapToLong(Book::getId)//mapujemy strumien ksiazek na strumien Longow z samymi idkami ksiazek
                .max().getAsLong()+ 1; //znajdujemy maxa i dodajemy numer
    }

    //metoda do usuniecia ksiazki na podstawie otrzymanego id
    public boolean removeBook(Long id) {
        Optional<Book> bookById = books.stream().filter(actualBook -> actualBook.getId().equals(id)).findAny();//szukamy ksiazki po id
        boolean present = bookById.isPresent();
        if (present) {
            Book bookToRemove = bookById.get();
            books.remove(bookToRemove);//jesli ksiazka o id jest na secie tu usuwamy
        }
        return present;//jesli nie znalezlismy ksiazki to zwrocimy false, a jak znalezlismy to true
    }
}
