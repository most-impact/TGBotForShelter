// config/DataInitConfig.java
package pro.dev.TGBotForShelter.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pro.dev.TGBotForShelter.model.*;
import pro.dev.TGBotForShelter.service.ShelterService;

@Configuration
public class DataInitConfig {
    @Bean CommandLineRunner seedShelters(ShelterService shelters) {
        return args -> {
            if (shelters.all().isEmpty()) {
                shelters.save(newShelter(ShelterType.DOG, "North Dogs",
                        "ул. Северная, 1","Пн-Пт 10:00–18:00","+7 900 000-00-01",
                        "Правила безопасности...","Приют для собак на севере."));
                shelters.save(newShelter(ShelterType.CAT, "South Cats",
                        "пр. Южный, 2","Ежедневно 11:00–17:00","+7 900 000-00-02",
                        "Не подносить животных к лицу...","Кошачий приют на юге."));
            }
        };
    }
    private Shelter newShelter(ShelterType t, String n, String addr, String sched,
                               String guard, String rules, String info) {
        Shelter s = new Shelter();
        s.setType(t); s.setName(n); s.setAddress(addr); s.setSchedule(sched);
        s.setSecurityContact(guard); s.setSafetyRules(rules); s.setInformation(info);
        return s;
    }
}
