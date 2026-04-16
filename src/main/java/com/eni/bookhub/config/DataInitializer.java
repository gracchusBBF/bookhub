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

        User regularUser2 = User.builder()
                .firstName("Ousmane")
                .lastName("Dembélé")
                .email("ousmane.dembele@bookhub.fr")
                .password(passwordEncoder.encode("User1234!"))
                .phoneNumber("0609101113")
                .userRole(roleUser)
                .build();

        User regularUser3 = User.builder()
                .firstName("Kylian")
                .lastName("Mbappé")
                .email("kylian.mbappe@bookhub.fr")
                .password(passwordEncoder.encode("User1234!"))
                .phoneNumber("0609101114")
                .userRole(roleUser)
                .build();

        userRepository.saveAll(List.of(admin, librarian, regularUser, regularUser2, regularUser3));
        log.info("5 utilisateurs insérés (ADMIN → Alice, LIBRARIAN → Bernard, USER → Claire, Ousmane, Kylian).");

        // ================================================================== //
        //  4. LIVRES – 40 livres                                               //
        //     • frontCoverImg identique pour tous                              //
        //     • statuts variés : AVAILABLE / UNAVAILABLE          //
        //     • catégories variées, copyNumber variable                        //
        // ================================================================== //
        final String COVER = "/images/cover.jpg";

        List<Book> books = List.of(
                // ---- Conte (3) ----
                Book.builder().title("Le Petit Prince").lastName("de Saint-Exupéry").firstName("Antoine").isbn("001-2070612759").category("Conte").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("Candide").lastName("Voltaire").firstName("François").isbn("014-2070413600").category("Conte").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("Zadig").lastName("Voltaire").firstName("François").isbn("041-2070413601").category("Conte").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),

                // ---- Science-Fiction (5) ----
                Book.builder().title("1984").lastName("Orwell").firstName("George").isbn("002-0451524934").category("Science-Fiction").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Fahrenheit 451").lastName("Bradbury").firstName("Ray").isbn("016-2070413800").category("Science-Fiction").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Le Meilleur des Mondes").lastName("Huxley").firstName("Aldous").isbn("017-2070413900").category("Science-Fiction").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Dune").lastName("Herbert").firstName("Frank").isbn("042-0441013597").category("Science-Fiction").status("AVAILABLE").frontCoverImg(COVER).copyNumber(4).build(),
                Book.builder().title("Fondation").lastName("Asimov").firstName("Isaac").isbn("043-0553293357").category("Science-Fiction").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),

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
                Book.builder().title("Le Rouge et le Noir").lastName("Stendhal").firstName("Henri").isbn("011-2070413300").category("Drame").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Voyage au bout de la nuit").lastName("Céline").firstName("Louis-Ferdinand").isbn("012-2070413400").category("Drame").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Notre-Dame de Paris").lastName("Hugo").firstName("Victor").isbn("019-2070414100").category("Drame").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Anna Karénine").lastName("Tolstoï").firstName("Léon").isbn("046-2253004922").category("Drame").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("L'Idiot").lastName("Dostoïevski").firstName("Fiodor").isbn("047-2070413000").category("Drame").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("La Condition humaine").lastName("Malraux").firstName("André").isbn("048-2070360113").category("Drame").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),

                // ---- Fantasy (4) ----
                Book.builder().title("Harry Potter").lastName("Rowling").firstName("J.K.").isbn("005-2070541274").category("Fantasy").status("AVAILABLE").frontCoverImg(COVER).copyNumber(5).build(),
                Book.builder().title("Le Seigneur des Anneaux").lastName("Tolkien").firstName("J.R.R.").isbn("006-2267013100").category("Fantasy").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Le Hobbit").lastName("Tolkien").firstName("J.R.R.").isbn("049-2267012103").category("Fantasy").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("Les Chroniques de Narnia").lastName("Lewis").firstName("C.S.").isbn("050-2070612803").category("Fantasy").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),

                // ---- Aventure (6) ----
                Book.builder().title("Don Quichotte").lastName("Cervantes").firstName("Miguel").isbn("007-2070412342").category("Aventure").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Vingt Mille Lieues sous les Mers").lastName("Verne").firstName("Jules").isbn("020-2070414200").category("Aventure").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("L'Alchimiste").lastName("Coelho").firstName("Paulo").isbn("018-2070414000").category("Aventure").status("AVAILABLE").frontCoverImg(COVER).copyNumber(4).build(),
                Book.builder().title("Le Tour du monde en 80 jours").lastName("Verne").firstName("Jules").isbn("051-2070413750").category("Aventure").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("L'Île mystérieuse").lastName("Verne").firstName("Jules").isbn("052-2070413760").category("Aventure").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Robinson Crusoé").lastName("Defoe").firstName("Daniel").isbn("053-2070413770").category("Aventure").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),

                // ---- Poésie (3) ----
                Book.builder().title("Les Fleurs du Mal").lastName("Baudelaire").firstName("Charles").isbn("015-2070413700").category("Poésie").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Alcools").lastName("Apollinaire").firstName("Guillaume").isbn("054-2070412500").category("Poésie").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),
                Book.builder().title("Les Contemplations").lastName("Hugo").firstName("Victor").isbn("055-2070412600").category("Poésie").status("UNAVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),

                // ---- Policier (3) ----
                Book.builder().title("Le Chien des Baskerville").lastName("Conan Doyle").firstName("Arthur").isbn("056-2070412700").category("Policier").status("AVAILABLE").frontCoverImg(COVER).copyNumber(3).build(),
                Book.builder().title("Dix Petits Nègres").lastName("Christie").firstName("Agatha").isbn("057-2070412800").category("Policier").status("AVAILABLE").frontCoverImg(COVER).copyNumber(2).build(),
                Book.builder().title("Le Meurtre de Roger Ackroyd").lastName("Christie").firstName("Agatha").isbn("058-2070412900").category("Policier").status("AVAILABLE").frontCoverImg(COVER).copyNumber(1).build(),

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
        Book gandhi        = books.get(38);  // Biographie  – AVAILABLE
        Book Sysiphe       = books.get(11);    // Philosophie – UNAVAILABLE
        Book mythe         = books.get(10);  // Philosophie – UNAVAILABLE

        log.info("Gandhi: " + gandhi.toString());

        // ================================================================== //
        //  5. EMPRUNTS – 6 emprunts pour Claire (USER)                        //
        // ================================================================== //

        // Emprunts retournés
        Loan loanRetourne1 = new Loan();
        loanRetourne1.setUser(regularUser);
        loanRetourne1.setBook(lesMiserables);
        loanRetourne1.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(60)));
        loanRetourne1.setReturnDate(Date.valueOf(LocalDate.now().minusDays(46)));
        loanRetourne1.setDeadline(Date.valueOf(LocalDate.now().minusDays(46)));

        Loan loanRetourne2 = new Loan();
        loanRetourne2.setUser(regularUser2);
        loanRetourne2.setBook(gandhi);
        loanRetourne2.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(90)));
        loanRetourne2.setReturnDate(Date.valueOf(LocalDate.now().minusDays(46)));
        loanRetourne2.setDeadline(Date.valueOf(LocalDate.now().minusDays(76)));

        Loan loanRetourne3 = new Loan();
        loanRetourne3.setUser(regularUser2);
        loanRetourne3.setBook(lePetitPrince);
        loanRetourne3.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(114)));
        loanRetourne3.setReturnDate(Date.valueOf(LocalDate.now().minusDays(100)));
        loanRetourne3.setDeadline(Date.valueOf(LocalDate.now().minusDays(100)));

        Loan loanRetourne4 = new Loan();
        loanRetourne4.setUser(regularUser3);
        loanRetourne4.setBook(Sysiphe);
        loanRetourne4.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(115)));
        loanRetourne4.setReturnDate(Date.valueOf(LocalDate.now().minusDays(101)));
        loanRetourne4.setDeadline(Date.valueOf(LocalDate.now().minusDays(101)));

        Loan loanRetourne5 = new Loan();
        loanRetourne5.setUser(regularUser3);
        loanRetourne5.setBook(dune);
        loanRetourne5.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(130)));
        loanRetourne5.setReturnDate(Date.valueOf(LocalDate.now().minusDays(116)));
        loanRetourne5.setDeadline(Date.valueOf(LocalDate.now().minusDays(116)));

        Loan loanRetourne6 = new Loan();
        loanRetourne6.setUser(regularUser3);
        loanRetourne6.setBook(dune);
        loanRetourne6.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(150)));
        loanRetourne6.setReturnDate(Date.valueOf(LocalDate.now().minusDays(135)));
        loanRetourne6.setDeadline(Date.valueOf(LocalDate.now().minusDays(135)));

        // ---------------- JANUARY 2026 ----------------

        Loan loanRetourne7 = new Loan();
        loanRetourne7.setUser(regularUser);
        loanRetourne7.setBook(harryPotter);
        loanRetourne7.setStartDate(Timestamp.valueOf(LocalDateTime.of(2025, 12, 20, 10, 0)));
        loanRetourne7.setReturnDate(Date.valueOf(LocalDate.of(2026, 1, 5)));
        loanRetourne7.setDeadline(Date.valueOf(LocalDate.of(2026, 1, 5)));

        Loan loanRetourne8 = new Loan();
        loanRetourne8.setUser(regularUser2);
        loanRetourne8.setBook(germinal);
        loanRetourne8.setStartDate(Timestamp.valueOf(LocalDateTime.of(2025, 12, 28, 10, 0)));
        loanRetourne8.setReturnDate(Date.valueOf(LocalDate.of(2026, 1, 10)));
        loanRetourne8.setDeadline(Date.valueOf(LocalDate.of(2026, 1, 10)));

        Loan loanRetourne9 = new Loan();
        loanRetourne9.setUser(regularUser3);
        loanRetourne9.setBook(anneFrank);
        loanRetourne9.setStartDate(Timestamp.valueOf(LocalDateTime.of(2025, 12, 30, 10, 0)));
        loanRetourne9.setReturnDate(Date.valueOf(LocalDate.of(2026, 1, 15)));
        loanRetourne9.setDeadline(Date.valueOf(LocalDate.of(2026, 1, 15)));

        Loan loanRetourne10 = new Loan();
        loanRetourne10.setUser(regularUser);
        loanRetourne10.setBook(fondation);
        loanRetourne10.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 1, 1, 10, 0)));
        loanRetourne10.setReturnDate(Date.valueOf(LocalDate.of(2026, 1, 18)));
        loanRetourne10.setDeadline(Date.valueOf(LocalDate.of(2026, 1, 18)));

        Loan loanRetourne11 = new Loan();
        loanRetourne11.setUser(regularUser2);
        loanRetourne11.setBook(lePetitPrince);
        loanRetourne11.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 1, 5, 10, 0)));
        loanRetourne11.setReturnDate(Date.valueOf(LocalDate.of(2026, 1, 25)));
        loanRetourne11.setDeadline(Date.valueOf(LocalDate.of(2026, 1, 25)));


// ---------------- FEBRUARY 2026 ----------------

        Loan loanRetourne12 = new Loan();
        loanRetourne12.setUser(regularUser3);
        loanRetourne12.setBook(dune);
        loanRetourne12.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 1, 15, 10, 0)));
        loanRetourne12.setReturnDate(Date.valueOf(LocalDate.of(2026, 2, 1)));
        loanRetourne12.setDeadline(Date.valueOf(LocalDate.of(2026, 2, 1)));

        Loan loanRetourne13 = new Loan();
        loanRetourne13.setUser(regularUser);
        loanRetourne13.setBook(gandhi);
        loanRetourne13.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 1, 20, 10, 0)));
        loanRetourne13.setReturnDate(Date.valueOf(LocalDate.of(2026, 2, 5)));
        loanRetourne13.setDeadline(Date.valueOf(LocalDate.of(2026, 2, 5)));

        Loan loanRetourne14 = new Loan();
        loanRetourne14.setUser(regularUser2);
        loanRetourne14.setBook(lesMiserables);
        loanRetourne14.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 1, 25, 10, 0)));
        loanRetourne14.setReturnDate(Date.valueOf(LocalDate.of(2026, 2, 10)));
        loanRetourne14.setDeadline(Date.valueOf(LocalDate.of(2026, 2, 10)));

        Loan loanRetourne15 = new Loan();
        loanRetourne15.setUser(regularUser3);
        loanRetourne15.setBook(harryPotter);
        loanRetourne15.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 1, 28, 10, 0)));
        loanRetourne15.setReturnDate(Date.valueOf(LocalDate.of(2026, 2, 15)));
        loanRetourne15.setDeadline(Date.valueOf(LocalDate.of(2026, 2, 15)));

        Loan loanRetourne16 = new Loan();
        loanRetourne16.setUser(regularUser);
        loanRetourne16.setBook(anneFrank);
        loanRetourne16.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 2, 1, 10, 0)));
        loanRetourne16.setReturnDate(Date.valueOf(LocalDate.of(2026, 2, 20)));
        loanRetourne16.setDeadline(Date.valueOf(LocalDate.of(2026, 2, 20)));


// ---------------- MARCH 2026 ----------------

        Loan loanRetourne17 = new Loan();
        loanRetourne17.setUser(regularUser2);
        loanRetourne17.setBook(germinal);
        loanRetourne17.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 2, 10, 10, 0)));
        loanRetourne17.setReturnDate(Date.valueOf(LocalDate.of(2026, 3, 1)));
        loanRetourne17.setDeadline(Date.valueOf(LocalDate.of(2026, 3, 1)));

        Loan loanRetourne18 = new Loan();
        loanRetourne18.setUser(regularUser3);
        loanRetourne18.setBook(fondation);
        loanRetourne18.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 2, 12, 10, 0)));
        loanRetourne18.setReturnDate(Date.valueOf(LocalDate.of(2026, 3, 5)));
        loanRetourne18.setDeadline(Date.valueOf(LocalDate.of(2026, 3, 5)));

        Loan loanRetourne19 = new Loan();
        loanRetourne19.setUser(regularUser);
        loanRetourne19.setBook(dune);
        loanRetourne19.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 2, 15, 10, 0)));
        loanRetourne19.setReturnDate(Date.valueOf(LocalDate.of(2026, 3, 10)));
        loanRetourne19.setDeadline(Date.valueOf(LocalDate.of(2026, 3, 10)));

        Loan loanRetourne20 = new Loan();
        loanRetourne20.setUser(regularUser2);
        loanRetourne20.setBook(lePetitPrince);
        loanRetourne20.setStartDate(Timestamp.valueOf(LocalDateTime.of(2026, 2, 20, 10, 0)));
        loanRetourne20.setReturnDate(Date.valueOf(LocalDate.of(2026, 3, 15)));
        loanRetourne20.setDeadline(Date.valueOf(LocalDate.of(2026, 3, 15)));


        // Emprunts actifs

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

        // Retards
        Loan loanRetard1 = new Loan();
        loanRetard1.setUser(regularUser);
        loanRetard1.setBook(dune);
        loanRetard1.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(20)));
        loanRetard1.setDeadline(Date.valueOf(LocalDate.now().minusDays(6)));

        Loan loanRetard2 = new Loan();
        loanRetard2.setUser(regularUser);
        loanRetard2.setBook(harryPotter);
        loanRetard2.setStartDate(Timestamp.valueOf(LocalDateTime.now().minusDays(22)));
        loanRetard2.setDeadline(Date.valueOf(LocalDate.now().minusDays(8)));

        loanRepository.saveAll(List.of(
                loanRetourne1, loanRetourne2, loanRetourne3,
                loanRetourne4, loanRetourne5, loanRetourne6,
                loanRetourne7, loanRetourne8, loanRetourne9, loanRetourne10,
                loanRetourne11, loanRetourne12, loanRetourne13, loanRetourne14,
                loanRetourne15, loanRetourne16, loanRetourne17, loanRetourne18,
                loanRetourne19, loanRetourne20,
                loan1, loan2, loan3,
                loanRetard1, loanRetard2
        ));
        log.info("Des emprunts insérés pour Claire (USER), Kylian (USER) et pour Ousmane (USER).");

        // Mise à jour du statut des livres faisant l'objet d'un emprunt actif
        lePetitPrince.setStatus("UNAVAILABLE");
        germinal.setStatus("UNAVAILABLE");
        anneFrank.setStatus("UNAVAILABLE");
        dune.setStatus("UNAVAILABLE");
        harryPotter.setStatus("UNAVAILABLE");
        bookRepository.saveAll(List.of(lePetitPrince, germinal, anneFrank, dune, harryPotter));
        log.info("Statut UNAVAILABLE appliqué aux 5 livres empruntés activement.");

        // ================================================================== //
        //  6. RESERVATIONS – 5 réservations pour Claire (USER)               //
        // ================================================================== //
        Reservation r1 = new Reservation();
        r1.setUser(regularUser);
        r1.setBook(lesMiserables);
        r1.setStatus("INACTIVE");

        Reservation r2 = new Reservation();
        r2.setUser(regularUser);
        r2.setBook(harryPotter);
        r2.setStatus("ACTIVE");

        Reservation r3 = new Reservation();
        r3.setUser(regularUser);
        r3.setBook(dune);
        r3.setStatus("INACTIVE");

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