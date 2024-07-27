package com.nailSalon.service;

import com.nailSalon.model.dto.AddNailServiceDTO;
import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.repository.NailServiceRepository;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NailServiceService {

    private final NailServiceRepository nailServiceRepository;

    public NailServiceService(NailServiceRepository nailServiceRepository) {
        this.nailServiceRepository = nailServiceRepository;
    }

    public void create(AddNailServiceDTO data) {

        NailService nailService = new NailService();
        nailService.setName(data.getName());
        nailService.setCategory(data.getCategory());
        nailService.setPrice(data.getPrice());
        nailService.setDuration(data.getDuration());
        nailService.setDescription(data.getDescription());

        nailServiceRepository.save(nailService);
    }


    public Map<Category, List<NailService>> findAllByCategory() {
        Map<Category, List<NailService>> result = new HashMap<>();

        for (Category cat : Category.values()) {
            List<NailService> services = nailServiceRepository.findAllByCategory(cat);

            result.put(cat, services);
        }
        return result;
    }

    public String getMinPriceForCertainCategory(List<NailService> services) {
        double minPrice = services.stream()
                .min(Comparator.comparingDouble(NailService::getPrice)).get().getPrice();
        boolean isWholeNumber = (minPrice == Math.round(minPrice));
        String pattern = isWholeNumber ? "#.##" : "#.00";
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(minPrice);
    }

    public void delete(Long id) {
        nailServiceRepository.deleteById(id);
    }
}
