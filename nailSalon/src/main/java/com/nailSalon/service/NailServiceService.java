package com.nailSalon.service;

import com.nailSalon.model.dto.AddNailServiceDTO;
import com.nailSalon.model.dto.AppointmentServiceDTO;
import com.nailSalon.model.entity.Appointment;
import com.nailSalon.model.entity.Category;
import com.nailSalon.model.entity.NailService;
import com.nailSalon.repository.AppointmentRepository;
import com.nailSalon.repository.NailServiceRepository;
import com.nailSalon.service.exception.RemoveServiceWithPresentAppointments;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NailServiceService {

    private final NailServiceRepository nailServiceRepository;
    private final AppointmentRepository appointmentRepository;

    public NailServiceService(NailServiceRepository nailServiceRepository, AppointmentRepository appointmentRepository) {
        this.nailServiceRepository = nailServiceRepository;
        this.appointmentRepository = appointmentRepository;
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

    public void delete(Long id) { //TODO Handle exception gracefully
        List<Appointment> appointments = appointmentRepository.findAllByServiceId(id);
        if (appointments.isEmpty()) {
            nailServiceRepository.deleteById(id);
        } else {
            String message;
            if (appointments.size() == 1) {
                message = String.format(
                        "Cannot delete nail service with existing appointments! There already is 1 appointment for service with name - %s."
                        ,appointments.get(0).getService().getName());
            } else {
                message = String.format(
                        "Cannot delete nail service with existing appointments! There already are %d appointments for service - %s."
                        , appointments.size(), appointments.get(0).getService().getName());
            }
            throw new RemoveServiceWithPresentAppointments(message);
        }
    }

    public NailService getByName(String name) {
        return nailServiceRepository.getByName(name);
    }

    public List<AppointmentServiceDTO> getAllServicesForAppointmentPage() {
        return nailServiceRepository.findAll()
                .stream()
                .map(this::toAppointmentServiceDTO)
                .collect(Collectors.toList());
    }

    private AppointmentServiceDTO toAppointmentServiceDTO(NailService nailService) {
        AppointmentServiceDTO appointmentServiceDTO = new AppointmentServiceDTO();
        appointmentServiceDTO.setName(nailService.getName());
        appointmentServiceDTO.setPrice(nailService.getPriceFormatted());
        return appointmentServiceDTO;
    }
}
