package com.e_ticketing.main;

import com.e_ticketing.model.*;
import com.e_ticketing.service.TicketingService;
import com.e_ticketing.dao.DatabaseInitializer;

import java.time.LocalDate;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TicketingService ticketingService = new TicketingService();
        DatabaseInitializer.createTables();
        DatabaseInitializer initialize = new DatabaseInitializer();
        //initialize.initializeDatabase();


//        Locatie locatie1 = ticketingService.creeazaLocatie("Arena Nationala", "Bucuresti", 50000);
//        Locatie locatie2 = ticketingService.creeazaLocatie("Muzeul de Arta", "Cluj-Napoca", 2000);
//
//        Concert concert = ticketingService.creeazaConcert("Concert A", LocalDate.of(2025, 5, 15), locatie1, 5000, 50,"Artist X");
//        Festival festival = ticketingService.creeazaFestival("Festival B", LocalDate.of(2025, 6, 20), locatie2, 3000,60, Arrays.asList("Rock", "Pop"));
//        Club club = ticketingService.creeazaClub("Club C", LocalDate.of(2025, 7, 10), locatie1, 1000,30, "DJ Z", 18);

        ticketingService.incarcaDateDinBazaDeDate();
        boolean Islogged = false;
        User actualuser = new User();
        while (true) {
            System.out.println("\n### Meniu ###");
            if(Islogged){
                System.out.println("3. Afiseaza evenimente");
                System.out.println("4. Cumpara bilet");
                System.out.println("5. Afiseaza biletele detinute");
                System.out.println("6. Afiseaza evenimente dintr-un oras");
                System.out.println("7. Afiseaza locatii");
                System.out.println("8. Iesi");
                if(actualuser.getRol() == Rol.ADMIN || actualuser.getRol() == Rol.ORGANIZATOR){
                    System.out.println("9. Adauga locatie");
                    System.out.println("10. Adauga eveniment");
                    if(actualuser.getRol() == Rol.ADMIN){
                        System.out.println("11. Afiseaza clienti");
                        System.out.println("12. Sterge client");
                        System.out.println("13. Sterge locatie");
                        System.out.println("14. Actualizeaza locatie");
                    }
                }
            }else{
                System.out.println("1. Logare");
                System.out.println("2. Creare cont");
            }

            System.out.print("Alege o optiune: ");

            int optiune = scanner.nextInt();
            scanner.nextLine();

            switch (optiune) {
                case 1:
                    actualuser = ticketingService.logareClient();
                    if(actualuser == null){
                        System.exit(1);
                    }
                    Islogged = true;
                    break;

                case 2:
                    System.out.print("Nume client: ");
                    String numeNou = scanner.nextLine();
                    System.out.print("Email client: ");
                    String emailNou = scanner.nextLine();
                    System.out.print("Parola client: ");
                    String parolaNoua = scanner.nextLine();
                    ticketingService.creeazaClient(numeNou, emailNou, parolaNoua);
                    System.out.println("Client adaugat cu succes!");
                    break;


                case 3:
                    ticketingService.afiseazaEvenimente();
                    break;

                case 4:
                    if (actualuser == null) {
                        System.out.println("Clientul nu a fost gasit!");
                        break;
                    }

                    System.out.print("Introduceti numele evenimentului: ");
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
                        ticketingService.cumparaBilet(actualuser, eveniment);
                    }
                    else{
                        System.out.println("Tranzactie refuzata!");
                    }
                    break;



                case 5:
                    if (actualuser != null) {
                        ticketingService.afiseazaBileteClient(actualuser);
                    } else {
                        System.out.println("Clientul nu a fost gasit!");
                    }
                    break;


                case 6:
                    System.out.print("Introduceti orasul: ");
                    String oras = scanner.nextLine();
                    ticketingService.afiseazaEvenimenteDinOras(oras);
                    break;


                case 7:
                    ticketingService.afiseazaLocatii();
                    break;

                case 8:
                    System.out.println("Iesire din aplicație...");
                    return;


                case 9:
                    System.out.print("Nume locație: ");
                    String numeLocatie = scanner.nextLine();
                    System.out.print("Oraș: ");
                    String orasLocatie = scanner.nextLine();
                    System.out.print("Capacitate: ");
                    int capacitate = scanner.nextInt();
                    scanner.nextLine();
                    ticketingService.creeazaLocatie(numeLocatie, orasLocatie, capacitate);
                    System.out.println("Locatie adaugata cu succes!");
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

                    System.out.println("Locatii disponibile:");
                    List<Locatie> locatii = ticketingService.getLocatii();
                    for (int i = 0; i < locatii.size(); i++) {
                        System.out.println((i + 1) + ". " + locatii.get(i));
                    }
                    System.out.print("Alege locatia (index): ");
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
                            System.out.println("Festival adaugat cu succes!");
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
                case 11:
                    ticketingService.afiseazaClienti();
                    break;
                case 13:
                    ticketingService.afiseazaLocatii();
                    System.out.print("Introdu ID-ul locatiei de șters: ");
                    int idDeSters = scanner.nextInt();
                    scanner.nextLine();
                    ticketingService.stergeLocatie(idDeSters);
                    System.out.println("Locatia a fost ștearsa cu succes!");
                    break;

                case 14:
                    ticketingService.afiseazaLocatii();
                    System.out.print("Introdu ID-ul locației de actualizat: ");
                    int idDeActualizat = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Nume nou: ");
                    String numeNouLoc = scanner.nextLine();
                    System.out.print("Adresa noua: ");
                    String adresaNouaLoc = scanner.nextLine();
                    System.out.print("Capacitate noua: ");
                    int capacitateNoua = scanner.nextInt();
                    scanner.nextLine();

                    ticketingService.actualizeazaLocatie(idDeActualizat, numeNouLoc, adresaNouaLoc, capacitateNoua);
                    System.out.println("Locatia a fost actualizata cu succes!");
                    break;


                default:
                    System.out.println("Optiune invalida! Incearca din nou.");
            }
        }
    }
}
