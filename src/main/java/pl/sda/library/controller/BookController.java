package pl.sda.library.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sda.library.model.Book;
import pl.sda.library.service.OrderService;

import java.util.Optional;
import java.util.Set;

/*
kontroler restowy, ktory wystawia zdefiniowane edpointy na swiat zewnetrzny po adresie http://localhost:8080
kontrolery restowe w springu zawsze oznaczamy adnotacja @RestComponent, zeby spring wiedzial, ze tu sa ednpointy
 */
@RestController
public class BookController {

    private final OrderService orderService;

    /*
    wstrzykujemy przez konstruktor bean springowy OrderService deklaracja moglaby wygladac tak:
    @Autowired
    public BookController(OrderService orderService) {
        this.orderService = orderService;
    }
    ale od wersji Springa 4.3 nie trzeba przy wstrzykiwaniu przez konstruktor pisac @Autowired
     */
    public BookController(OrderService orderService) {
        this.orderService = orderService;
    }

    /*
    z wykorzystaniem adnotacji @GetMapping wystawiamy endpoint do uderzenia metoda HTTP GET
    @GetMapping(value = "/books", produces = "application/json")
    w tym konkretnym przypadku mowimy, ze przy uderzeniu metoda HTTP GET w url http://localhost:8080/books zwroc Set
    ksiazek w formacie JSON (Set<Book> -> produces = "application/json")
    Dodatkowym parametrem metody jest @RequestParam(required = false) String title, ktory jest nieobowiazkowy
    RequestParamy podajemy w url po znaku "?" w tym wypadku http://localhost:8080/books?title=Dziady
     */
    @GetMapping(value = "/books", produces = "application/json")
    public Set<Book> getBooks(@RequestParam(required = false) String title) {
        return (Set<Book>) orderService.getBooks(title);
    }

    /*
    w tym konktretnym wypadku wystawiamy endpoint do wypozyczenia ksiazki dostepny pod metoda HTTP GET pod urlem przykladowo:
    http://localhost:8080/book/order/1
    w argumencie metody przekazujemy @PathVariable Long id - nazwa zmiennej musi byc taka sama jak parametru w url: id ->  "/book/order/{id}"
    Podobnie jak w poprzednim przypadku wynik zwracamy w formacje JSON
     */
    @GetMapping(value = "/book/order/{id}", produces = "application/json")
    public ResponseEntity<Book> rentBook(@PathVariable Long id) {
        Optional<Book> book = orderService.rentBook(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());//jesli udalo sie wypozyczyc ksiazke to zwracamy jej detale wraz ze statusem sukcesu 200 OK
        }
        return ResponseEntity.notFound().build();//jesli nie ma ksiazki o danym id to zwracamy kod bledu 404 NOT_FOUND
    }

    /*
    w tym ednpoincie pod metoda HTTP POST pod url http://localhost:8080/book/add wystawiamy mozliwosc dodania ksiazki
    metoda konsumuje JSONa, ktorego podajemy w BODY requestu (consumes = "application/json")
    przykladowe BODY requestu:
    {
        "author": "Test",
        "title": "Title"
    }
    @RequestBody Book book - w ten sposob mowimy, ze w body requestu przyjdzie cos co Jackson bedzie probowal zmapowac na ksiazke
    pola w JSONie musza nazywac sie tak samo jak pola w klasie Book
     */
    @PostMapping(value = "/book/add", consumes = "application/json")
    public ResponseEntity<Long> addBook(@RequestBody Book book) {
        Book addedBook = orderService.save(book);
        return new ResponseEntity<>(addedBook.getId(), HttpStatus.CREATED); //jesli ksiazke udalo sie dodac to zwracamy w body jej ID i status sukcesu 201 CREATED mowiacy o utworzeniu zasobu
    }

    /*
    w tym endpoincie wystawiamy mozliwosc usuniecia ksiazki pod metoda HTTP DELETE pod urlem przykladowo:
     http://localhost:8080/book/remove/1
     w argumencie metody przekazujemy @PathVariable Long id - nazwa zmiennej musi byc taka sama jak parametru w url: id ->  "/book/order/{id}"
     */
    @DeleteMapping("/book/remove/{id}")
    public ResponseEntity<Void> removeBook(@PathVariable Long id) {
        orderService.removeBook(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);//jesli ksiazka zostala usunieta to zwracamy kod sukcesu 204 NO_CONTENT
    }
}