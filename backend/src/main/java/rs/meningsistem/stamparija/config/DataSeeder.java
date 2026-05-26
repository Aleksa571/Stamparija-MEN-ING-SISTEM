package rs.meningsistem.stamparija.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import rs.meningsistem.stamparija.model.*;
import rs.meningsistem.stamparija.model.enums.RoleName;
import rs.meningsistem.stamparija.repository.*;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final BlogPostRepository blogPostRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (roleRepository.count() > 0) {
            log.info("Baza je vec inicijalizovana, preskacem seed.");
            return;
        }

        log.info("Inicijalizujem pocetne podatke (Stamparija MEN-ING SISTEM DOO)...");

        Role adminRole = roleRepository.save(Role.builder().name(RoleName.ROLE_ADMIN).build());
        Role userRole = roleRepository.save(Role.builder().name(RoleName.ROLE_USER).build());

        User admin = User.builder()
                .username("admin")
                .email("admin@meningsistem.rs")
                .password(passwordEncoder.encode("admin123"))
                .firstName("Vlasnik")
                .lastName("Stamparije")
                .phone("026/123-456")
                .address("Smederevo")
                .roles(Set.of(adminRole, userRole))
                .build();
        userRepository.save(admin);

        User testUser = User.builder()
                .username("kupac")
                .email("kupac@example.com")
                .password(passwordEncoder.encode("kupac123"))
                .firstName("Petar")
                .lastName("Petrovic")
                .phone("064/123-456")
                .address("Beograd, Knez Mihailova 1")
                .roles(Set.of(userRole))
                .build();
        userRepository.save(testUser);

        Category rostilj = categoryRepository.save(Category.builder()
                .name("Kutije za roštilj")
                .description("Kartonske kutije sa štampom za pakovanje pečenog mesa i roštilja. Idealne za mesare, restorane i prodavnice gotove hrane.")
                .imageUrl("/uploads/IMG_1062.JPG")
                .build());

        Category pomfrit = categoryRepository.save(Category.builder()
                .name("Kutije za pomfrit i hranu")
                .description("Praktične srebrne kutije za pomfrit, čevape i drugu toplu hranu sa unutrašnjom aluminijumskom oblogom.")
                .imageUrl("/uploads/IMG_1064.JPG")
                .build());

        Category kolaci = categoryRepository.save(Category.builder()
                .name("Kutije za kolače")
                .description("Bele kutije za kolače sa providnim prozorom – pravougaone i kvadratne, različitih dimenzija.")
                .imageUrl("/uploads/IMG_1065.JPG")
                .build());

        Category podmetaci = categoryRepository.save(Category.builder()
                .name("Podmetači za piće")
                .description("Papirni i kartonski podmetači za piće sa štampom po želji.")
                .imageUrl("/uploads/IMG_1065.JPG")
                .build());

        Category kalendari = categoryRepository.save(Category.builder()
                .name("Kalendari")
                .description("Zidni i stoni kalendari ofset štampe.")
                .imageUrl("/uploads/IMG_1065.JPG")
                .build());

        Category blokovska = categoryRepository.save(Category.builder()
                .name("Blokovska roba")
                .description("Otpremnice, računi, reversi – sve vrste blokovske robe za firme.")
                .imageUrl("/uploads/IMG_1066.JPG")
                .build());

        productRepository.save(Product.builder()
                .name("Kartonska kutija za roštilj - velika (Jonas)")
                .description("Štampana kartonska kutija za roštilj sa motivom mesa, dimenzije 30x20x5cm. Praktična za pakovanje roštilja, kao na primer Prerada mesa Jonas iz Kovačice.")
                .price(new BigDecimal("45.00"))
                .dimensions("30x20x5cm")
                .stock(500)
                .imageUrl("/uploads/IMG_1062.JPG")
                .category(rostilj)
                .build());

        productRepository.save(Product.builder()
                .name("Kartonska kutija za roštilj - srednja")
                .description("Štampana kartonska kutija za pojedinačne porcije roštilja, dimenzije 25x15x5cm.")
                .price(new BigDecimal("35.00"))
                .dimensions("25x15x5cm")
                .stock(800)
                .imageUrl("/uploads/IMG_1068.JPG")
                .category(rostilj)
                .build());

        productRepository.save(Product.builder()
                .name("Srebrna kutija za pomfrit/hranu")
                .description("Kartonska kutija sa srebrnom aluminijumskom unutrašnjom oblogom, idealna za pakovanje pomfrita, čevapa i tople hrane. Zadržava temperaturu.")
                .price(new BigDecimal("28.00"))
                .dimensions("20x12x5cm")
                .stock(1000)
                .imageUrl("/uploads/IMG_1064.JPG")
                .category(pomfrit)
                .build());

        productRepository.save(Product.builder()
                .name("Srebrna kutija - velika porcija")
                .description("Veća srebrna kutija za veće porcije hrane, sa unutrašnjom aluminijumskom oblogom.")
                .price(new BigDecimal("38.00"))
                .dimensions("25x15x6cm")
                .stock(600)
                .imageUrl("/uploads/IMG_1067.JPG")
                .category(pomfrit)
                .build());

        productRepository.save(Product.builder()
                .name("Kvadratna kutija za kolače sa prozorom")
                .description("Bela kartonska kutija za kolače sa providnim PVC prozorom i ukrasnom ivicom, dimenzije 22x22x6cm.")
                .price(new BigDecimal("65.00"))
                .dimensions("22x22x6cm")
                .stock(400)
                .imageUrl("/uploads/IMG_1065.JPG")
                .category(kolaci)
                .build());

        productRepository.save(Product.builder()
                .name("Pravougaona kutija za kolače sa prozorom")
                .description("Manja pravougaona kutija za sitnije kolače, sa providnim PVC prozorom, dimenzije 18x12x5cm.")
                .price(new BigDecimal("45.00"))
                .dimensions("18x12x5cm")
                .stock(500)
                .imageUrl("/uploads/IMG_1066.JPG")
                .category(kolaci)
                .build());

        productRepository.save(Product.builder()
                .name("Kvadratna kutija za kolače - veliki format")
                .description("Veća kvadratna kutija za torte i veće porcije kolača, sa providnim prozorom.")
                .price(new BigDecimal("85.00"))
                .dimensions("28x28x8cm")
                .stock(300)
                .imageUrl("/uploads/IMG_1070.JPG")
                .category(kolaci)
                .build());

        productRepository.save(Product.builder()
                .name("Papirni podmetač za piće - okrugli")
                .description("Apsorbujući papirni podmetač za piće, prečnik 9cm, štampa po želji. Pakovanje od 100 komada.")
                .price(new BigDecimal("450.00"))
                .dimensions("Ø9cm / 100kom")
                .stock(200)
                .imageUrl("/uploads/IMG_1065.JPG")
                .category(podmetaci)
                .build());

        productRepository.save(Product.builder()
                .name("Zidni kalendar A3 (12 listova)")
                .description("Zidni kalendar formata A3, 12 listova, ofset štampa u punom koloru, sa spiralom.")
                .price(new BigDecimal("550.00"))
                .dimensions("A3 / 12 listova")
                .stock(150)
                .imageUrl("/uploads/IMG_1065.JPG")
                .category(kalendari)
                .build());

        productRepository.save(Product.builder()
                .name("Stoni kalendar")
                .description("Stoni kalendar sa postoljem, 12 mesečnih listova, ofset štampa.")
                .price(new BigDecimal("380.00"))
                .dimensions("21x15cm")
                .stock(200)
                .imageUrl("/uploads/IMG_1065.JPG")
                .category(kalendari)
                .build());

        productRepository.save(Product.builder()
                .name("Blok računa A5 (NCR papir)")
                .description("Blok računa, NCR papir (samokopirajući), 50 listova u dva primerka (original + kopija).")
                .price(new BigDecimal("220.00"))
                .dimensions("A5 / 50x2")
                .stock(300)
                .imageUrl("/uploads/IMG_1066.JPG")
                .category(blokovska)
                .build());

        productRepository.save(Product.builder()
                .name("Otpremnica A4")
                .description("Blok otpremnica A4 format, NCR papir, 100 listova u tri primerka.")
                .price(new BigDecimal("320.00"))
                .dimensions("A4 / 100x3")
                .stock(250)
                .imageUrl("/uploads/IMG_1066.JPG")
                .category(blokovska)
                .build());

        productRepository.save(Product.builder()
                .name("Revers A5")
                .description("Blok reversa A5, 50 listova u dva primerka.")
                .price(new BigDecimal("200.00"))
                .dimensions("A5 / 50x2")
                .stock(280)
                .imageUrl("/uploads/IMG_1066.JPG")
                .category(blokovska)
                .build());

        blogPostRepository.save(BlogPost.builder()
                .title("Dobrodošli na zvaničnu stranicu Štamparije MEN-ING SISTEM DOO")
                .content("Smederevo, već dugi niz godina ponosno postoji 12 štamparija, a mi smo jedna od dve koje su preostale na tržištu. " +
                        "Naša štamparija MEN-ING SISTEM DOO specijalizovana je za proizvodnju kartonske ambalaže – kutija za roštilj, " +
                        "kutija za pomfrit i toplu hranu, kutija za kolače, podmetača za piće, kalendara, kao i blokovske robe " +
                        "(otpremnice, računi, reversi). Posvećenost kvalitetu, brzina isporuke i prilagodljivost potrebama klijenata " +
                        "su naše glavne karakteristike. Hvala vam što ste uz nas!")
                .imageUrl("/uploads/IMG_1062.JPG")
                .author(admin)
                .build());

        blogPostRepository.save(BlogPost.builder()
                .title("Nova ponuda kutija za kolače")
                .content("Sa zadovoljstvom predstavljamo proširenu ponudu kutija za kolače sa providnim prozorom. " +
                        "Sada u ponudi imamo kvadratne i pravougaone kutije u tri različite veličine. " +
                        "Idealne su za poslastičarnice, kafiće i privatne narudžbine. Dostupne su odmah na zalihama.")
                .imageUrl("/uploads/IMG_1070.JPG")
                .author(admin)
                .build());

        blogPostRepository.save(BlogPost.builder()
                .title("Načini dostave – sada i kurirska služba")
                .content("Pored ličnog preuzimanja u našem pogonu u Smederevu i dostave na vašu adresu, " +
                        "od ove godine omogućili smo i slanje robe putem kurirskih službi širom Srbije. " +
                        "Pri kreiranju porudžbine možete izabrati željeni način dostave.")
                .author(admin)
                .build());

        log.info("Seed gotov. Kreirano: 2 role, 2 korisnika (admin/admin123, kupac/kupac123), 6 kategorija, 13 proizvoda, 3 blog posta.");
    }
}
