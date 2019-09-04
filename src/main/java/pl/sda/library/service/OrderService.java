package pl.sda.library.service;

import org.springframework.stereotype.Service;
import pl.sda.library.model.Book;
import pl.sda.library.repository.BookRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/*
serwis springowy (zarzadzany przez springa dzieki adnotacji @Service)
Warswa serwisow wg sztuki sluzy jako miejsce dla logiki biznesowej (wymagan od klienta)
 */
@Service
public class OrderService {

    private final BookRepository bookRepository;


    /*
   wstrzykujemy przez konstruktor bean springowy BookRepository deklaracja moglaby wygladac tak:
   @Autowired
   public OrderService(BookRepository bookRepository) {
       this.bookRepository = bookRepository;
   }
   ale od wersji Springa 4.3 nie trzeba przy wstrzykiwaniu przez konstruktor pisac @Autowired
    */
    public OrderService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /*
    Metoda sluzaca do wypozyczenia ksiazki. Tutaj jest juz kawalek logiki biznesowej obliczajacy date oddania ksiazki ->
    LocalDate.now().plusDays(30)
    wyliczona wartosc oddania ksiazki wraz z id ksiazki od wypozyczenia przekazywana jest do repozytorium,
     aby zapisac date oddania "w bazie" i pobrac ksiazke. Jesli udalo sie wypozyczyc ksiazke to zwracamy ja do kontrolera owrapowana w Optional
     */
    public Optional<Book> rentBook(Long id) {

        Optional<Book> foundBook = bookRepository.findById(id);
        if (foundBook.isPresent() && foundBook.get().getReturnDate() == null) {
            Book book = foundBook.get();
            book.setReturnDate((LocalDate.now().plusDays(30)));
            bookRepository.save(book);
            return Optional.of(book);
        } else {
            return Optional.empty();
        }
    }

    /*
    metoda sluzaca do dodawania ksiazki - dodanie ksiazki do bazy rowniez lezy w gestii repozytorium, wiec jedyne co robi
    serwis to przepycha otrzymana ksiazke dalej do repozytorium. Jesli udalo sie dodac ksiazke "do bazy"
     to zwracamy ja z powrotem do kontrolera wraz z uzupelnianym id.
     */
    public Book save(Book book) {
        return bookRepository.save(book);
    }

    /*
    metoda sluzaca do usuwania ksiazki - usuniecie ksiazki z bazy (seta) lezy w gestii repozytorium, wiec jedyne co robi
    serwis to przepycha id ksiazki do usuniecia dalej do repozytorium. Jesli uda sie usunac ksiazke to zwracamy flage z wartoscia true,
    jesli nie to z false
     */
    public void removeBook(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getBooks(String title) {
        if (title == null) {
            return bookRepository.findAll();
        } else {
            return bookRepository.findByTitle(title);
        }
    }

}
