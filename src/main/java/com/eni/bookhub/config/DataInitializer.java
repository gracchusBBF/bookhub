package com.eni.bookhub.config;

import com.eni.bookhub.BO.*;
import com.eni.bookhub.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@AllArgsConstructor
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final BookRepository        bookRepository;
    private final UserRepository        userRepository;
    private final UserRoleRepository    userRoleRepository;
    private final PermissionRepository  permissionRepository;
    private final LoanRepository        loanRepository;
    private final CommentRepository     commentRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder       passwordEncoder;

    @Override
    public void run(String @NonNull ... args) {

        if (bookRepository.count() > 0) {
            log.info("Base déjà initialisée, insertion ignorée.");
            return;
        }

        // ================================================================== //
        //  1. PERMISSIONS                                                      //
        // ================================================================== //
        Permission pRead = new Permission();
        pRead.setPermissionName("READ");

        Permission pWrite = new Permission();
        pWrite.setPermissionName("WRITE");

        permissionRepository.saveAll(List.of(pRead, pWrite));
        log.info("2 permissions insérées (READ, WRITE).");

        // ================================================================== //
        //  2. ROLES                                                            //
        //     ADMIN     → READ + WRITE                                         //
        //     LIBRARIAN → READ + WRITE                                         //
        //     USER      → READ uniquement                                      //
        // ================================================================== //
        UserRole roleAdmin = UserRole.builder()
                .roleName("ADMIN")
                .listPermission(List.of(pRead, pWrite))
                .build();

        UserRole roleLibrarian = UserRole.builder()
                .roleName("LIBRARIAN")
                .listPermission(List.of(pRead, pWrite))
                .build();

        UserRole roleUser = UserRole.builder()
                .roleName("USER")
                .listPermission(List.of(pRead))
                .build();

        userRoleRepository.saveAll(List.of(roleAdmin, roleLibrarian, roleUser));
        log.info("3 rôles insérés (ADMIN, LIBRARIAN, USER).");

        // ================================================================== //
        //  3. UTILISATEURS                                                     //
        // ================================================================== //
        User admin = User.builder()
                .firstName("Alice")
                .lastName("Dupont")
                .email("alice.admin@bookhub.fr")
                .password(passwordEncoder.encode("Admin1234!"))
                .phoneNumber("0601020304")
                .userRole(roleAdmin)
                .build();

        User librarian = User.builder()
                .firstName("Bernard")
                .lastName("Martin")
                .email("bernard.librarian@bookhub.fr")
                .password(passwordEncoder.encode("Libra1234!"))
                .phoneNumber("0605060708")
                .userRole(roleLibrarian)
                .build();

        User regularUser = User.builder()
                .firstName("Claire")
                .lastName("Morel")
                .email("claire.user@bookhub.fr")
                .password(passwordEncoder.encode("User1234!"))
                .phoneNumber("0609101112")
                .userRole(roleUser)
                .build();

        userRepository.saveAll(List.of(admin, librarian, regularUser));
        log.info("3 utilisateurs insérés (ADMIN → Alice, LIBRARIAN → Bernard, USER → Claire).");

        // ================================================================== //
        //  4. LIVRES – 40 livres                                               //
        //     • frontCoverImg identique pour tous                              //
        //     • statuts variés : AVAILABLE / UNAVAILABLE / PENDING             //
        //     • catégories variées, copyNumber variable                        //
        // ================================================================== //
        final String COVER = "https://example.com/covers/default.jpg";

        List<Book> books = List.of(
                // ---- Conte (3) ----
                Book.builder().title("Le Petit Prince").lastName("de Saint-Exupéry").firstName("Antoine").isbn("001-2070612759").category("Conte").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("Candide").lastName("Voltaire").firstName("François").isbn("014-2070413600").category("Conte").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("Zadig").lastName("Voltaire").firstName("François").isbn("041-2070413601").category("Conte").status("PENDING").frontCoverImg(COVER).copyNumber(1).build(),

                // ---- Science-Fiction (5) ----
                Book.builder().title("1984").lastName("Orwell").firstName("George").isbn("002-0451524934").category("Science-Fiction").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Fahrenheit 451").lastName("Bradbury").firstName("Ray").isbn("016-2070413800").category("Science-Fiction").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Le Meilleur des Mondes").lastName("Huxley").firstName("Aldous").isbn("017-2070413900").category("Science-Fiction").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Dune").lastName("Herbert").firstName("Frank").isbn("042-0441013597").category("Science-Fiction").status("AVAILABLE").frontCoverImg(COVER).copyNumber(4).build(),
                Book.builder().title("Fondation").lastName("Asimov").firstName("Isaac").isbn("043-0553293357").category("Science-Fiction").status("PENDING").frontCoverImg(COVER).copyNumber(2).build(),

                // ---- Philosophie (4) ----
                Book.builder().title("L'Étranger").lastName("Camus").firstName("Albert").isbn("003-2070360024").category("Philosophie").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("La Nausée").lastName("Sartre").firstName("Jean-Paul").isbn("013-2070413500").category("Philosophie").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Le Mythe de Sisyphe").lastName("Camus").firstName("Albert").isbn("044-2070322882").category("Philosophie").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("L'Être et le Néant").lastName("Sartre").firstName("Jean-Paul").isbn("045-2070319008").category("Philosophie").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),

                // ---- Drame (10) ----
                Book.builder().title("Les Misérables").lastName("Hugo").firstName("Victor").isbn("004-2070409228").category("Drame").status("AVAILABLE").frontCoverImg(COVER).copyNumber(4).build(),
                Book.builder().title("Crime et Châtiment").lastName("Dostoïevski").firstName("Fiodor").isbn("008-2070412783").category("Drame").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Madame Bovary").lastName("Flaubert").firstName("Gustave").isbn("009-2070413149").category("Drame").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("Germinal").lastName("Zola").firstName("Émile").isbn("010-2070413200").category("Drame").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Le Rouge et le Noir").lastName("Stendhal").firstName("Henri").isbn("011-2070413300").category("Drame").status("PENDING").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Voyage au bout de la nuit").lastName("Céline").firstName("Louis-Ferdinand").isbn("012-2070413400").category("Drame").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Notre-Dame de Paris").lastName("Hugo").firstName("Victor").isbn("019-2070414100").category("Drame").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Anna Karénine").lastName("Tolstoï").firstName("Léon").isbn("046-2253004922").category("Drame").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("L'Idiot").lastName("Dostoïevski").firstName("Fiodor").isbn("047-2070413000").category("Drame").status("PENDING").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("La Condition humaine").lastName("Malraux").firstName("André").isbn("048-2070360113").category("Drame").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),

                // ---- Fantasy (4) ----
                Book.builder().title("Harry Potter").lastName("Rowling").firstName("J.K.").isbn("005-2070541274").category("Fantasy").status("AVAILABLE").frontCoverImg(COVER).copyNumber(5).build(),
                Book.builder().title("Le Seigneur des Anneaux").lastName("Tolkien").firstName("J.R.R.").isbn("006-2267013100").category("Fantasy").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Le Hobbit").lastName("Tolkien").firstName("J.R.R.").isbn("049-2267012103").category("Fantasy").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("Les Chroniques de Narnia").lastName("Lewis").firstName("C.S.").isbn("050-2070612803").category("Fantasy").status("PENDING").frontCoverImg(COVER).copyNumber(2).build(),

                // ---- Aventure (6) ----
                Book.builder().title("Don Quichotte").lastName("Cervantes").firstName("Miguel").isbn("007-2070412342").category("Aventure").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Vingt Mille Lieues sous les Mers").lastName("Verne").firstName("Jules").isbn("020-2070414200").category("Aventure").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("L'Alchimiste").lastName("Coelho").firstName("Paulo").isbn("018-2070414000").category("Aventure").status("AVAILABLE").frontCoverImg(COVER).copyNumber(4).build(),
                Book.builder().title("Le Tour du monde en 80 jours").lastName("Verne").firstName("Jules").isbn("051-2070413750").category("Aventure").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("L'Île mystérieuse").lastName("Verne").firstName("Jules").isbn("052-2070413760").category("Aventure").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Robinson Crusoé").lastName("Defoe").firstName("Daniel").isbn("053-2070413770").category("Aventure").status("PENDING").frontCoverImg(COVER).copyNumber(2).build(),

                // ---- Poésie (3) ----
                Book.builder().title("Les Fleurs du Mal").lastName("Baudelaire").firstName("Charles").isbn("015-2070413700").category("Poésie").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Alcools").lastName("Apollinaire").firstName("Guillaume").isbn("054-2070412500").category("Poésie").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Les Contemplations").lastName("Hugo").firstName("Victor").isbn("055-2070412600").category("Poésie").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),

                // ---- Policier (3) ----
                Book.builder().title("Le Chien des Baskerville").lastName("Conan Doyle").firstName("Arthur").isbn("056-2070412700").category("Policier").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("Dix Petits Nègres").lastName("Christie").firstName("Agatha").isbn("057-2070412800").category("Policier").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Le Meurtre de Roger Ackroyd").lastName("Christie").firstName("Agatha").isbn("058-2070412900").category("Policier").status("PENDING").frontCoverImg(COVER).copyNumber(1).build(),

                // ---- Biographie (2) ----
                Book.builder().title("Ma vie").lastName("Gandhi").firstName("Mohandas").isbn("059-2070413050").category("Biographie").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Journal").lastName("Frank").firstName("Anne").isbn("060-2070413060").category("Biographie").status("AVAILABLE").frontCoverImg(COVER).copyNumber(4).build()
        );

        bookRepository.saveAll(books);
        log.info("{} livres insérés.", books.size());

        // Raccourcis pour les sections suivantes
        Book lePetitPrince = books.get(0);   // Conte       – AVAILABLE
        Book fondation     = books.get(7);   // SF          – PENDING
        Book lesMiserables = books.get(12);  // Drame       – AVAILABLE
        Book germinal      = books.get(15);  // Drame       – AVAILABLE
        Book harryPotter   = books.get(22);  // Fantasy     – AVAILABLE
        Book dune          = books.get(6);   // SF          – AVAILABLE
        Book anneFrank     = books.get(39);  // Biographie  – AVAILABLE
        Book mythe         = books.get(10);  // Philosophie – UNAVAILABLE

        // ================================================================== //
        //  5. EMPRUNTS – 3 emprunts pour Claire (USER)                        //
        // ================================================================== //
        Loan loan1 = new Loan();
        loan1.setUser(regularUser);
        loan1.setBook(lePetitPrince);
        loan1.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(12)));
        loan1.setDeadline(Date.valueOf(LocalDate.now().plusDays(2)));

        Loan loan2 = new Loan();
        loan2.setUser(regularUser);
        loan2.setBook(germinal);
        loan2.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(5)));
        loan2.setDeadline(Date.valueOf(LocalDate.now().plusDays(9)));

        Loan loan3 = new Loan();
        loan3.setUser(regularUser);
        loan3.setBook(anneFrank);
        loan3.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(1)));
        loan3.setDeadline(Date.valueOf(LocalDate.now().plusDays(13)));

        loanRepository.saveAll(List.of(loan1, loan2, loan3));
        log.info("3 emprunts insérés pour Claire (USER).");

        // ================================================================== //
        //  6. RESERVATIONS – 5 réservations pour Claire (USER)               //
        // ================================================================== //
        Reservation r1 = new Reservation();
        r1.setUser(regularUser);
        r1.setBook(lesMiserables);
        r1.setStatus("PENDING");

        Reservation r2 = new Reservation();
        r2.setUser(regularUser);
        r2.setBook(harryPotter);
        r2.setStatus("ACTIVE");

        Reservation r3 = new Reservation();
        r3.setUser(regularUser);
        r3.setBook(dune);
        r3.setStatus("PENDING");

        Reservation r4 = new Reservation();
        r4.setUser(regularUser);
        r4.setBook(fondation);
        r4.setStatus("INACTIVE");

        Reservation r5 = new Reservation();
        r5.setUser(regularUser);
        r5.setBook(mythe);
        r5.setStatus("ACTIVE");

        reservationRepository.saveAll(List.of(r1, r2, r3, r4, r5));
        log.info("5 réservations insérées pour Claire (USER).");

        // ================================================================== //
        //  7. COMMENTAIRES – 2 commentaires de Claire sur 2 livres différents //
        //     ⚠️  Comment.id n'a pas de @GeneratedValue → on le sette         //
        //         manuellement. Corrige l'entité si tu veux l'auto-incrémente. //
        // ================================================================== //
        Comment c1 = new Comment();
        c1.setUser(regularUser);
        c1.setBook(lesMiserables);
        c1.setRate(5);
        c1.setComment("Un chef-d'œuvre absolu. Victor Hugo plonge le lecteur dans la France du XIXe siècle avec une intensité rare.");
        c1.setStatus("APPROUVED");

        Comment c2 = new Comment();
        c2.setUser(regularUser);
        c2.setBook(harryPotter);
        c2.setRate(4);
        c2.setComment("Lecture magique et addictive, idéale pour tous les âges. Le monde de Poudlard ne déçoit jamais !");
        c2.setStatus("APPROUVED");

        commentRepository.saveAll(List.of(c1, c2));
        log.info("2 commentaires insérés pour Claire (USER).");

        log.info("=== Init terminée : 2 permissions | 3 rôles | 3 utilisateurs | {} livres | 3 emprunts | 5 réservations | 2 commentaires ===",
                books.size());
    }
}
