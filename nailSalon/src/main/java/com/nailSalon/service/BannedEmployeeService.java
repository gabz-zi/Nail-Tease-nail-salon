package com.nailSalon.service;

import com.nailSalon.model.entity.User;
import com.nailSalon.model.enums.RoleName;
import com.nailSalon.model.view.BannedEmployeeView;
import com.nailSalon.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BannedEmployeeService {

    private final UserRepository userRepository;

    public BannedEmployeeService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<BannedEmployeeView> findAllBannedEmployees() {
       List<User> bannedEmployees = userRepository.findAllByIsBanned(true);

       return bannedEmployees.stream().map(this::userToBannedEmployeeView)
               .collect(Collectors.toList());
    }

    private BannedEmployeeView userToBannedEmployeeView(User user) {
        BannedEmployeeView bannedEmployeeView = new BannedEmployeeView();
        bannedEmployeeView.setId(user.getId());
        bannedEmployeeView.setUsername(user.getUsername());
        bannedEmployeeView.setEmail(user.getEmail());
        return bannedEmployeeView;
    }

    @Transactional
    public void fireEmployee(Long id) {
        User employee = userRepository.findById(id).get();
        employee.getRoles().removeIf(role -> role.getName() == RoleName.EMPLOYEE);
        employee.getRoles().removeIf(role -> role.getName() == RoleName.ADMIN);
        employee.setIsBanned(false);
        userRepository.save(employee);
    }
}
