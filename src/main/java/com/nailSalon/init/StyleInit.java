package com.nailSalon.init;

import com.nailSalon.model.entity.Style;
import com.nailSalon.model.entity.StyleName;
import com.nailSalon.repository.StyleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class StyleInit implements CommandLineRunner {
    private final StyleRepository styleRepository;

    public StyleInit(StyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if(styleRepository.count() == 0) {
            List<Style> styles = new ArrayList<>();

            Arrays.stream(StyleName.values())
                    .forEach(styleName -> {
                        Style style = new Style();
                        style.setName(styleName);
                        styles.add(style);
                    });
            styleRepository.saveAll(styles);
        }
    }
}
