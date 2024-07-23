package com.nailSalon.service;

import com.nailSalon.model.dto.AddNailServiceDTO;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.repository.NailServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<NailService> getAllServices() {
        return nailServiceRepository.findAll();
    }
}
