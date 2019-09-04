package pl.sda.library.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.sda.library.model.Book;
import java.util.*;

/*
repozytorium springowe (zarzadzane przez springa dzieki adnotacji @Repository)
Warswa repozytoriow wg sztuki sluzy jako miejsce dla wykonywania operacji na bazie danych - w naszym wypadku na secie, ktory
imituje bazy danych
 */

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book>findByTitle(String title);
}
