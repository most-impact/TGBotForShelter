// model/Shelter.java
package pro.dev.TGBotForShelter.model;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter @ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity @Table(name = "shelters")
public class Shelter {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private ShelterType type;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(length = 1000)
    private String schedule;

    private String securityContact;

    @Column(length = 2000)
    private String safetyRules;

    @Column(length = 2000)
    private String information;
}
