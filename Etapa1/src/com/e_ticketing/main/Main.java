package com.e_ticketing.main;

import com.e_ticketing.model.*;
import com.e_ticketing.service.TicketingService;

import java.time.LocalDate;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TicketingService ticketingService = new TicketingService();

        Locatie locatie1 = ticketingService.creeazaLocatie("Arena Nationala", "Bucuresti", 50000);
        Locatie locatie2 = ticketingService.creeazaLocatie("Muzeul de Arta", "Cluj-Napoca", 2000);

        Client client1 = ticketingService.creeazaClient("Ion Popescu", "ion.popescu@email.com");
        Client client2 = ticketingService.creeazaClient("Maria Ionescu", "maria.ionescu@email.com");

        Concert concert = ticketingService.creeazaConcert("Concert A", LocalDate.of(2025, 5, 15), locatie1, 5000, 50,"Artist X");
        Festival festival = ticketingService.creeazaFestival("Festival B", LocalDate.of(2025, 6, 20), locatie2, 3000,60, Arrays.asList("Rock", "Pop"));
        Club club = ticketingService.creeazaClub("Club C", LocalDate.of(2025, 7, 10), locatie1, 1000,30, "DJ Z", 18);

        while (true) {
            System.out.println("\n### Meniu ###");
            System.out.println("1. Afiseaza evenimente");
            System.out.println("2. Cumpara bilet");
            System.out.println("3. Afiseaza biletele unui client");
            System.out.println("4. Afiseaza clienti");
            System.out.println("5. Afiseaza evenimente dintr-un oras");
            System.out.println("6. Afiseaza locatii");
            System.out.println("7. Iesi");
            System.out.println("8. Adauga client");
            System.out.println("9. Adauga locatie");
            System.out.println("10. Adauga eveniment");
            System.out.print("Alege o optiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch (optiune) {
                case 1:
                    ticketingService.afiseazaEvenimente();
                    break;

                case 2:
                    System.out.print("Introduceți numele clientului: ");
                    String numeClient = scanner.nextLine();
                    Client client = null;
                    for (Client c : ticketingService.getClienti()) {
                        if (c.getNume().equalsIgnoreCase(numeClient)) {
                            client = c;
                            break;
                        }
                    }
                    if (client == null) {
                        System.out.println("Clientul nu a fost gasit!");
                        break;
                    }

                    System.out.print("Introduceți numele evenimentului: ");
                    String numeEveniment = scanner.nextLine();
                    Eveniment eveniment = null;
                    for (Eveniment e : ticketingService.getEvenimente()) {
                        if (e.getNume().equalsIgnoreCase(numeEveniment)) {
                            eveniment = e;
                            break;
                        }
                    }
                    if (eveniment == null) {
                        System.out.println("Evenimentul nu a fost gasit!");
                        break;
                    }

                    System.out.println(" Pretul biletului este: "+ ticketingService.getPret(eveniment));
                    System.out.println("\n Doriti sa-l cumparati? Da/Nu:");
                    String doriti = scanner.nextLine();
                    if(doriti.equalsIgnoreCase("da")){
                    ticketingService.cumparaBilet(client, eveniment);
                    }
                    else{
                        System.out.println("Tranzactie refuzata!");
                    }
                    break;


                case 3:
                    System.out.print("Introduceți numele clientului: ");
                    String numeClientBilete = scanner.nextLine();
                    Client clientBilete = null;
                    for (Client c : ticketingService.getClienti()) {
                        if (c.getNume().equalsIgnoreCase(numeClientBilete)) {
                            clientBilete = c;
                            break;
                        }
                    }
                    if (clientBilete != null) {
                        ticketingService.afiseazaBileteClient(clientBilete);
                    } else {
                        System.out.println("Clientul nu a fost gasit!");
                    }
                    break;

                case 4:
                    ticketingService.afiseazaClienti();
                    break;

                case 5:
                    System.out.print("Introduceți orasul: ");
                    String oras = scanner.nextLine();
                    ticketingService.afiseazaEvenimenteDinOras(oras);
                    break;

                case 6:
                    ticketingService.afiseazaLocatii();
                    break;

                case 7:
                    System.out.println("Iesire din aplicație...");
                    return;
                case 8:
                    System.out.print("Nume client: ");
                    String numeNou = scanner.nextLine();
                    System.out.print("Email client: ");
                    String emailNou = scanner.nextLine();
                    ticketingService.creeazaClient(numeNou, emailNou);
                    System.out.println("Client adaugat cu succes!");
                    break;

                case 9:
                    System.out.print("Nume locație: ");
                    String numeLocatie = scanner.nextLine();
                    System.out.print("Oraș: ");
                    String orasLocatie = scanner.nextLine();
                    System.out.print("Capacitate: ");
                    int capacitate = scanner.nextInt();
                    scanner.nextLine();
                    ticketingService.creeazaLocatie(numeLocatie, orasLocatie, capacitate);
                    System.out.println("Locatie adaugată cu succes!");
                    break;

                case 10:
                    System.out.println("Tip eveniment: 1 - Concert | 2 - Festival | 3 - Club");
                    int tipEveniment = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nume eveniment: ");
                    String numeEv = scanner.nextLine();
                    System.out.print("Data (yyyy-MM-dd): ");
                    String dataStr = scanner.nextLine();
                    LocalDate dataEv = LocalDate.parse(dataStr);

                    System.out.println("Locații disponibile:");
                    List<Locatie> locatii = ticketingService.getLocatii();
                    for (int i = 0; i < locatii.size(); i++) {
                        System.out.println((i + 1) + ". " + locatii.get(i));
                    }
                    System.out.print("Alege locația (index): ");
                    int indexLocatie = scanner.nextInt() - 1;
                    scanner.nextLine();

                    Locatie locatieSelectata = locatii.get(indexLocatie);

                    System.out.print("Numar total de bilete: ");
                    int totalBilete = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Pret Bilete: ");
                    int pret = scanner.nextInt();
                    scanner.nextLine();

                    switch (tipEveniment) {
                        case 1: // Concert
                            System.out.print("Artist: ");
                            String artist = scanner.nextLine();
                            ticketingService.creeazaConcert(numeEv, dataEv, locatieSelectata, totalBilete,pret, artist);
                            System.out.println("Concert adaugat cu succes!");
                            break;
                        case 2: // Festival
                            System.out.print("Genuri (separate prin virgula): ");
                            String genuriStr = scanner.nextLine();
                            List<String> genuri = Arrays.asList(genuriStr.split("\\s*,\\s*"));
                            ticketingService.creeazaFestival(numeEv, dataEv, locatieSelectata, totalBilete,pret, genuri);
                            System.out.println("Festival adăugat cu succes!");
                            break;
                        case 3: // Club
                            System.out.print("DJ: ");
                            String dj = scanner.nextLine();
                            System.out.print("Varsta minima: ");
                            int varstaMinima = scanner.nextInt();
                            scanner.nextLine();
                            ticketingService.creeazaClub(numeEv, dataEv, locatieSelectata, totalBilete,pret, dj, varstaMinima);
                            System.out.println("Club adaugat cu succes!");
                            break;
                        default:
                            System.out.println("Opțiune invalida!");
                    }
                    break;

                default:
                    System.out.println("Optiune invalida! Incearca din nou.");
            }
        }
    }
}
