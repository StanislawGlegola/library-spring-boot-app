package pl.sda.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//adnotacja ta mowi, ze jest to aplikacja spring bootowa i wlaczamy autokonfiguracje (nie musimy dodawac ComponentScana itd.)
@SpringBootApplication
public class LibraryApplication {

    //metoda ta odpala aplikacje spring bootowa z wykorzystaniem SpringApplication.run(LibraryApplication.class, args);
    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

}
