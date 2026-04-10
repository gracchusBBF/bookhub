package com.eni.bookhub.config;

import com.eni.bookhub.BO.Book;
import com.eni.bookhub.repository.BookRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private BookRepository bookRepository;

    @Override
    public void run(String @NonNull ... args) {
        if (bookRepository.count() > 0) {
            log.info("Base déjà initialisée, insertion ignorée.");
            return;
        }

        List<Book> books = List.of(
                Book.builder().title("Le Petit Prince").lastName("de Saint-Exupéry").firstName("Antoine").isbn("001-2070612759").category("Conte").status("AVAILABLE").frontCoverImg("https://example.com/covers/petit-prince.jpg").copyNumber(3).build(),
                Book.builder().title("1984").lastName("Orwell").firstName("George").isbn("002-0451524934").category("Science-Fiction").status("AVAILABLE").frontCoverImg("https://example.com/covers/1984.jpg").copyNumber(2).build(),
                Book.builder().title("L'Étranger").lastName("Camus").firstName("Albert").isbn("003-2070360024").category("Philosophie").status("AVAILABLE").frontCoverImg("https://example.com/covers/etranger.jpg").copyNumber(1).build(),
                Book.builder().title("Les Misérables").lastName("Hugo").firstName("Victor").isbn("004-2070409228").category("Drame").status("AVAILABLE").frontCoverImg("https://example.com/covers/miserables.jpg").copyNumber(4).build(),
                Book.builder().title("Harry Potter").lastName("Rowling").firstName("J.K.").isbn("005-2070541274").category("Fantasy").status("AVAILABLE").frontCoverImg("https://example.com/covers/harry-potter.jpg").copyNumber(5).build(),
                Book.builder().title("Le Seigneur des Anneaux").lastName("Tolkien").firstName("J.R.R.").isbn("006-2267013100").category("Fantasy").status("AVAILABLE").frontCoverImg("https://example.com/covers/sda.jpg").copyNumber(2).build(),
                Book.builder().title("Don Quichotte").lastName("Cervantes").firstName("Miguel").isbn("007-2070412342").category("Aventure").status("AVAILABLE").frontCoverImg("https://example.com/covers/don-quichotte.jpg").copyNumber(1).build(),
                Book.builder().title("Crime et Châtiment").lastName("Dostoïevski").firstName("Fiodor").isbn("008-2070412783").category("Drame").status("UNAVAILABLE").frontCoverImg("https://example.com/covers/crime-chatiment.jpg").copyNumber(2).build(),
                Book.builder().title("Madame Bovary").lastName("Flaubert").firstName("Gustave").isbn("009-2070413149").category("Drame").status("AVAILABLE").frontCoverImg("https://example.com/covers/madame-bovary.jpg").copyNumber(3).build(),
                Book.builder().title("Germinal").lastName("Zola").firstName("Émile").isbn("010-2070413200").category("Drame").status("AVAILABLE").frontCoverImg("https://example.com/covers/germinal.jpg").copyNumber(2).build(),
                Book.builder().title("Le Rouge et le Noir").lastName("Stendhal").firstName("Henri").isbn("011-2070413300").category("Drame").status("MISSING").frontCoverImg("https://example.com/covers/rouge-noir.jpg").copyNumber(1).build(),
                Book.builder().title("Voyage au bout de la nuit").lastName("Céline").firstName("Louis-Ferdinand").isbn("012-2070413400").category("Drame").status("AVAILABLE").frontCoverImg("https://example.com/covers/voyage.jpg").copyNumber(2).build(),
                Book.builder().title("La Nausée").lastName("Sartre").firstName("Jean-Paul").isbn("013-2070413500").category("Philosophie").status("AVAILABLE").frontCoverImg("https://example.com/covers/nausee.jpg").copyNumber(1).build(),
                Book.builder().title("Candide").lastName("Voltaire").firstName("François").isbn("014-2070413600").category("Conte").status("AVAILABLE").frontCoverImg("https://example.com/covers/candide.jpg").copyNumber(3).build(),
                Book.builder().title("Les Fleurs du Mal").lastName("Baudelaire").firstName("Charles").isbn("015-2070413700").category("Poésie").status("AVAILABLE").frontCoverImg("https://example.com/covers/fleurs-mal.jpg").copyNumber(2).build(),
                Book.builder().title("Fahrenheit 451").lastName("Bradbury").firstName("Ray").isbn("016-2070413800").category("Science-Fiction").status("UNAVAILABLE").frontCoverImg("https://example.com/covers/fahrenheit.jpg").copyNumber(1).build(),
                Book.builder().title("Le Meilleur des Mondes").lastName("Huxley").firstName("Aldous").isbn("017-2070413900").category("Science-Fiction").status("AVAILABLE").frontCoverImg("https://example.com/covers/meilleur-mondes.jpg").copyNumber(2).build(),
                Book.builder().title("L'Alchimiste").lastName("Coelho").firstName("Paulo").isbn("018-2070414000").category("Aventure").status("AVAILABLE").frontCoverImg("https://example.com/covers/alchimiste.jpg").copyNumber(4).build(),
                Book.builder().title("Notre-Dame de Paris").lastName("Hugo").firstName("Victor").isbn("019-2070414100").category("Drame").status("AVAILABLE").frontCoverImg("https://example.com/covers/notre-dame.jpg").copyNumber(2).build(),
                Book.builder().title("Vingt Mille Lieues").lastName("Verne").firstName("Jules").isbn("020-2070414200").category("Aventure").status("AVAILABLE").frontCoverImg("https://example.com/covers/vingt-mille.jpg").copyNumber(3).build()
        );

        bookRepository.saveAll(books);
        log.info("{} livres insérés avec succès.", books.size());
    }
}