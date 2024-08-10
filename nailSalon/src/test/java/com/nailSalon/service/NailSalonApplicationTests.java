package com.nailSalon.service;

import static org.mockito.Mockito.*;

import com.nailSalon.model.NailSalonUserDetails;
import com.nailSalon.model.dto.*;
import com.nailSalon.model.view.BannedEmployeeView;
import com.nailSalon.repository.*;
import org.junit.jupiter.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.nailSalon.model.entity.*;
import com.nailSalon.model.enums.RoleName;
import com.nailSalon.model.view.MyAppointmentView;
import com.nailSalon.model.view.PendingAppointmentView;
import com.nailSalon.model.view.TodaysAppointmentView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;


@SpringBootTest
class NailSalonApplicationTests {


    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private DesignRepository designRepository;

    @MockBean
    private DesignServiceClient designServiceClient;

    @Mock
    private MultipartFile mockMultipartFile;

    @Autowired
    private DesignService designService;

    @MockBean
    private UserRoleRepository userRoleRepository;

    @MockBean
    private CVRepository cvRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private NailServiceService nailServiceService;

    @MockBean
    private NailServiceRepository nailServiceRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ApplicantService applicantService;

    @Autowired
    private UserService userService;

    @Autowired
    private TodaysAppointmentService todaysAppointmentService;

    @Autowired
    private PendingAppointmentService pendingAppointmentService;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private BannedEmployeeService bannedEmployeeService;

    @Autowired
    private CVService cvService;

    @Value("${file.upload-dir}")
    private String uploadDir = "uploads/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void contextLoads() {
    }

    /*  ~~~~~~~~~~~~  START OF PENDING_APPOINTMENT_SERVICE TESTING  ~~~~~~~~~~~~*/
    @Test
    void testFindAllPendingAppointments() {
        // Arrange
        String username = "user1";
        Appointment appointment1 = createMockAppointment("user2", Category.GEL, false);
        Appointment appointment2 = createMockAppointment("user3", Category.GELISH, false);

        when(appointmentRepository.findAllByStatusAndUserUsernameNotAndCancelled(0, username, false))
                .thenReturn(List.of(appointment1, appointment2));

        // Act
        List<PendingAppointmentView> result = pendingAppointmentService.findAllPendingAppointments(username);

        // Assert
        assertEquals(2, result.size());
        assertEquals("user2", result.get(0).getCreateBy());
        assertEquals("user3", result.get(1).getCreateBy());
        verify(appointmentRepository, times(1)).findAllByStatusAndUserUsernameNotAndCancelled(0, username, false);
    }

    private Appointment createMockAppointment(String username, Category category, boolean cancelled) {
        Appointment appointment = new Appointment();
        appointment.setCreateOn(LocalDateTime.now().minusDays(1));
        appointment.setMadeFor(LocalDateTime.now().plusDays(1));

        User user = new User();
        user.setUsername(username);
        appointment.setUser(user);

        NailService service = new NailService();
        service.setCategory(category);
        service.setPrice(50.00);
        appointment.setService(service);
        appointment.setCancelled(cancelled);

        return appointment;
    }

    @Test
    void testAcceptAppointment() {
        // Arrange
        Long appointmentId = 1L;
        String username = "employee1";

        User employee = new User();
        employee.setUsername(username);

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStatus(0);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(employee));

        // Act
        pendingAppointmentService.acceptAppointment(appointmentId, username);

        // Assert
        assertEquals(1, appointment.getStatus());
        assertEquals(employee, appointment.getTakenBy());
        assertTrue(employee.getAcceptedAppointments().contains(appointment));

        verify(appointmentRepository, times(1)).save(appointment);
        verify(userRepository, times(1)).save(employee);
    }

    @Test
    void testDeclineAppointment() {
        // Arrange
        Long appointmentId = 1L;
        String username = "employee1";

        User employee = new User();
        employee.setUsername(username);

        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        appointment.setStatus(0);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(employee));

        // Act
        pendingAppointmentService.declineAppointment(appointmentId, username);

        // Assert
        assertEquals(3, appointment.getStatus());
        assertEquals(employee, appointment.getTakenBy());

        verify(appointmentRepository, times(1)).save(appointment);
    }

    /*  ~~~~~~~~~~~~  END OF PENDING_APPOINTMENT_SERVICE TESTING  ~~~~~~~~~~~~*/


    /*  ~~~~~~~~~~~~  START OF TODAYS_APPOINTMENT_SERVICE TESTING  ~~~~~~~~~~~~*/
    @Test
    void testFindAllAppointmentsForToday() {
        String username = "testUser";
        LocalDate today = LocalDate.now();
        LocalDateTime appointmentTime1 = LocalDateTime.of(today, LocalTime.of(10, 0));
        LocalDateTime appointmentTime2 = LocalDateTime.of(today, LocalTime.of(14, 0));

        User user = new User();
        user.setUsername(username);

        NailService service = new NailService();
        service.setDuration("30");
        service.setCategory(Category.PEDICURE);
        service.setPrice(20);

        Appointment appointment1 = new Appointment();
        appointment1.setUser(user);
        appointment1.setMadeFor(appointmentTime1);
        appointment1.setService(service);

        Appointment appointment2 = new Appointment();
        appointment2.setUser(user);
        appointment2.setMadeFor(appointmentTime2);
        appointment2.setService(service);

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment1);
        appointments.add(appointment2);

        when(appointmentRepository.findAllByTakenBy_UsernameAndStatus(username, 1)).thenReturn(appointments);

        List<TodaysAppointmentView> result = todaysAppointmentService.findAllAppointmentsForToday(username);

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM");
        String formattedDate = currentDate.format(formatter);

        assertEquals(2, result.size());
        assertEquals(String.format("at 10:00 on %s", formattedDate), result.get(0).getTime());
        assertEquals(String.format("at 14:00 on %s", formattedDate), result.get(1).getTime());
        assertEquals("testUser", result.get(0).getCreateBy());
        assertEquals("testUser", result.get(1).getCreateBy());
        assertEquals("PEDICURE", result.get(0).getService());
        assertEquals("PEDICURE", result.get(1).getService());
        assertEquals("30", result.get(0).getDuration());
        assertEquals("30", result.get(1).getDuration());
        assertEquals("20", result.get(0).getPrice());
        assertEquals("20", result.get(1).getPrice());
    }
    /*  ~~~~~~~~~~~~  END OF TODAYS_APPOINTMENT_SERVICE TESTING  ~~~~~~~~~~~~*/

    /*  ~~~~~~~~~~~~  START OF APPOINTMENT_SERVICE TESTING  ~~~~~~~~~~~~*/
    @Test
    void addAppointmentToUserWithUsername() {
        // Arrange
        String username = "user1";
        AddAppointmentDTO dto = new AddAppointmentDTO();
        dto.setMadeFor(LocalDateTime.now().plusDays(1));
        dto.setService("manicure");

        User user = new User();
        user.setUsername(username);
        user.setAppointments(new ArrayList<>());

        NailService nailService = new NailService();
        nailService.setName("manicure");
        nailService.setAppointments(new ArrayList<>());

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(nailServiceService.getByName("manicure")).thenReturn(nailService);

        // Act
        appointmentService.addAppointmentToUserWithUsername(dto, username);

        // Assert
        Appointment savedAppointment = user.getAppointments().get(0);
        assertNotNull(savedAppointment);
        assertEquals(dto.getMadeFor(), savedAppointment.getMadeFor());
        assertEquals(nailService, savedAppointment.getService());
        assertTrue(nailService.getAppointments().contains(savedAppointment));
        assertTrue(user.getAppointments().contains(savedAppointment));

        verify(appointmentRepository, times(1)).save(savedAppointment);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetAppointmentsOfUser() {
        // Arrange
        String username = "user1";
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setMadeFor(LocalDateTime.now().plusDays(1));
        appointment.setCreateOn(LocalDateTime.now());
        NailService nailService = new NailService();
        nailService.setCategory(Category.GEL);
        appointment.setService(nailService);
        appointment.setStatus(0);

        when(appointmentRepository.findAllByUserUsernameAndCancelled(username, false))
                .thenReturn(List.of(appointment));

        // Act
        List<MyAppointmentView> appointments = appointmentService.getAppointmentsOfUser(username);

        // Assert
        assertEquals(1, appointments.size());
        MyAppointmentView view = appointments.get(0);
        assertEquals("PENDING", view.getStatus());
        assertEquals("€ " + appointment.getService().getPriceFormatted(), view.getPrice());
        assertEquals(appointment.getId(), view.getId());
        assertEquals(appointment.getMadeFor().format(DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm")), view.getMadeFor());
        assertEquals(appointment.getCreateOn().format(DateTimeFormatter.ofPattern("dd.MM.yyyy/HH:mm")), view.getCreateOn());

        verify(appointmentRepository, times(1)).findAllByUserUsernameAndCancelled(username, false);
    }

    @Test
    void testDeleteWithTakenByNotNull() {
        // Arrange
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        User user = new User();
        user.setAppointments(new ArrayList<>());
        NailService service = new NailService();
        service.setAppointments(new ArrayList<>());

        // Setup user and service with the appointment
        appointment.setUser(user);
        appointment.setService(service);

        User employee = new User();
        employee.setId(2L);
        employee.setAcceptedAppointments(new ArrayList<>());
        employee.getAcceptedAppointments().add(appointment);

        appointment.setTakenBy(employee);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // Act
        appointmentService.delete(appointmentId);

        // Assert
        assertFalse(user.getAppointments().contains(appointment));
        assertFalse(service.getAppointments().contains(appointment));
        assertNull(appointment.getTakenBy());
        assertFalse(employee.getAcceptedAppointments().contains(appointment));

        verify(userRepository, times(1)).save(user);
        verify(nailServiceRepository, times(1)).save(service);
        verify(userRepository, times(1)).save(employee);
        verify(appointmentRepository, times(1)).deleteById(appointmentId);
    }

    @Test
    void testDeleteWithTakenByNull() {
        // Arrange
        Long appointmentId = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(appointmentId);
        User user = new User();
        user.setAppointments(new ArrayList<>());
        NailService service = new NailService();
        service.setAppointments(new ArrayList<>());

        appointment.setUser(user);
        appointment.setService(service);

        appointment.setTakenBy(null);

        when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

        // Act
        appointmentService.delete(appointmentId);

        // Assert
        assertFalse(user.getAppointments().contains(appointment));
        assertFalse(service.getAppointments().contains(appointment));
        assertNull(appointment.getTakenBy()); // Should still be null
        verify(userRepository, times(1)).save(user);
        verify(nailServiceRepository, times(1)).save(service);
        verify(appointmentRepository, times(1)).deleteById(appointmentId);
    }

    @Test
    void testFindById() {
        // Arrange
        Long id = 1L;
        Appointment appointment = new Appointment();
        appointment.setId(id);

        when(appointmentRepository.findById(id)).thenReturn(Optional.of(appointment));

        // Act
        Appointment found = appointmentService.findById(id);

        // Assert
        assertNotNull(found);
        assertEquals(id, found.getId());
        verify(appointmentRepository, times(1)).findById(id);
    }

    @Test
    void testSave() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);

        appointmentService.save(appointment);

        verify(appointmentRepository, times(1)).save(appointment);
    }
    /*  ~~~~~~~~~~~~  END OF APPOINTMENT_SERVICE TESTING  ~~~~~~~~~~~~*/

    /*  ~~~~~~~~~~~~  START OF USER_SERVICE TESTING  ~~~~~~~~~~~~*/
    @Test
    void testRegister_UserAlreadyExists() {
        // Arrange
        UserRegisterDTO data = new UserRegisterDTO();
        data.setUsername("existingUser");
        data.setEmail("email@example.com");
        data.setPassword("password");


        when(userRepository.findByUsernameOrEmail(data.getUsername(), data.getEmail()))
                .thenReturn(Optional.of(new User()));

        // Act
        boolean result = userService.register(data);

        // Assert
        assertFalse(result);
        Mockito.verify(userRepository, times(0)).save(any(User.class));
    }


    @Test
    void testRegister_NewUser() {
        // Arrange
        UserRegisterDTO data = new UserRegisterDTO("newUser", "newEmail@example.com", "password");


        when(userRepository.findByUsernameOrEmail(data.getUsername(), data.getEmail()))
                .thenReturn(Optional.empty());


        UserRoleEntity userRole = new UserRoleEntity(1L, RoleName.USER);
        when(roleRepository.findByName(RoleName.USER))
                .thenReturn(userRole);

        when(passwordEncoder.encode(data.getPassword()))
                .thenReturn("encodedPassword");

        // Act
        boolean result = userService.register(data);

        // Debugging output
        System.out.println("Result: " + result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void testFindUserById_Success() {
        // Arrange
        long userId = 1L;
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername("testuser");

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        User foundUser = userService.findUserById(userId);

        // Assert
        assertNotNull("User should not be null", foundUser);
        assertEquals(userId, foundUser.getId(), "User ID should match");
        assertEquals("testuser", foundUser.getUsername(), "Username should match");
    }

    @Test
    public void testFindUserById_UserNotFound() {
        // Arrange
        long userId = 2L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> userService.findUserById(userId));
    }
    /*  ~~~~~~~~~~~~  END OF USER_SERVICE TESTING  ~~~~~~~~~~~~*/

    /*  ~~~~~~~~~~~~  START OF NAIL_SERVICE_SERVICE TESTING  ~~~~~~~~~~~~*/

    @Test
    void createNailServiceSuccessfully() {
        // Arrange
        AddNailServiceDTO dto = new AddNailServiceDTO();
        dto.setName("Manicure");
        dto.setCategory(Category.GEL);
        dto.setPrice(20.0);
        dto.setDuration("30 minutes");
        dto.setDescription("Basic manicure");

        // Act
        nailServiceService.create(dto);

        // Assert
        verify(nailServiceRepository, times(1)).save(any(NailService.class));
    }

    @Test
    void findAllByCategory_ReturnsMapOfNailServices() {
        // Arrange
        NailService service1 = new NailService();
        service1.setCategory(Category.GEL);
        NailService service2 = new NailService();
        service2.setCategory(Category.PEDICURE);

        when(nailServiceRepository.findAllByCategory(Category.GEL)).thenReturn(Collections.singletonList(service1));
        when(nailServiceRepository.findAllByCategory(Category.PEDICURE)).thenReturn(Collections.singletonList(service2));

        // Act
        Map<Category, List<NailService>> result = nailServiceService.findAllByCategory();

        // Assert
        assertNotNull(result);
        assertTrue(result.containsKey(Category.GEL));
        assertTrue(result.containsKey(Category.PEDICURE));
        assertEquals(1, result.get(Category.GEL).size());
        assertEquals(1, result.get(Category.PEDICURE).size());
    }

    @Test
    void getMinPriceForCertainCategory_ReturnsFormattedPrice() {
        // Arrange
        NailService service1 = new NailService();
        service1.setPrice(20.0);
        NailService service2 = new NailService();
        service2.setPrice(25.5);

        List<NailService> services = Arrays.asList(service1, service2);

        // Act
        String minPrice = nailServiceService.getMinPriceForCertainCategory(services);

        // Assert
        assertEquals("20", minPrice);
    }

    @Test
    void delete_NoAppointments_DeletesNailService() {
        // Arrange
        Long id = 1L;
        when(appointmentRepository.findAllByServiceId(id)).thenReturn(Collections.emptyList());

        // Act
        nailServiceService.delete(id);

        // Assert
        verify(nailServiceRepository, times(1)).deleteById(id);
    }

    @Test
    void delete_WithAppointments_ThrowsException() {
        // Arrange
        Long id = 1L;
        Appointment appointment = new Appointment();
        when(appointmentRepository.findAllByServiceId(id)).thenReturn(Collections.singletonList(appointment));

        // Act & Assert
        RuntimeException thrown = assertThrows(
                RuntimeException.class,
                () -> nailServiceService.delete(id)
        );
        assertEquals("Cannot delete nail service with existing appointments!", thrown.getMessage());
    }

    @Test
    void getByName_ExistingName_ReturnsNailService() {
        // Arrange
        String name = "Manicure";
        NailService service = new NailService();
        when(nailServiceRepository.getByName(name)).thenReturn(service);

        // Act
        NailService result = nailServiceService.getByName(name);

        // Assert
        assertNotNull(result);
    }

    @Test
    void getAllServicesForAppointmentPage_ReturnsAppointmentServiceDTOList() {
        // Arrange
        NailService service = new NailService();
        service.setName("Manicure");
        service.setPrice(20.0);

        when(nailServiceRepository.findAll()).thenReturn(Collections.singletonList(service));

        // Act
        List<AppointmentServiceDTO> result = nailServiceService.getAllServicesForAppointmentPage();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Manicure", result.get(0).getName());
        assertEquals("20", result.get(0).getPrice());
    }
    /*  ~~~~~~~~~~~~  END OF NAIL_SERVICE_SERVICE TESTING  ~~~~~~~~~~~~*/

    /*  ~~~~~~~~~~~~  START OF APPLICANT_SERVICE TESTING  ~~~~~~~~~~~~*/
    @Test
    public void testAddApplicationFormToUserWithUsername_Success() {
        // Arrange
        String username = "testuser";
        ApplicationFormDTO formDTO = new ApplicationFormDTO();
        formDTO.setExperienceLevel(ExperienceLevel.EXPERT);
        formDTO.setPersonalMotivation("Passionate about nails");

        User user = new User();
        user.setCv(null); // No CV initially

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cvRepository.save(any(CV.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved CV
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved User

        // Act
        boolean result = applicantService.addApplicationFormToUserWithUsername(formDTO, username);

        // Assert
        assertTrue(result, "Application form should be successfully added");
        assertNotNull("User should have a CV assigned", user.getCv());
    }

    @Test
    public void testAddApplicationFormToUserWithUsername_Failure() {
        // Arrange
        String username = "testuser";
        ApplicationFormDTO formDTO = new ApplicationFormDTO();
        formDTO.setExperienceLevel(ExperienceLevel.EXPERT);
        formDTO.setPersonalMotivation("Passionate about nails");

        User user = new User();
        CV existingCv = new CV(); // User already has a CV
        user.setCv(existingCv);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        boolean result = applicantService.addApplicationFormToUserWithUsername(formDTO, username);

        // Assert
        assertFalse(result, "Application form should not be added if user already has a CV");
        assertEquals(existingCv, user.getCv(), "CV should not be updated");
    }

    @Test
    public void testFindAllApplicants() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("applicant1");
        user1.setEmail("applicant1@example.com");
        CV cv1 = new CV();
        user1.setCv(cv1);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("applicant2");
        user2.setEmail("applicant2@example.com");
        CV cv2 = new CV();
        user2.setCv(cv2);

        List<User> usersWithCv = List.of(user1, user2);
        when(userRepository.findAllByCvNotNull()).thenReturn(usersWithCv);

        // Act
        List<ApplicantDTO> applicants = applicantService.findAllApplicants();

        // Assert
        assertEquals(2, applicants.size(), "There should be two applicants");
        assertEquals("applicant1", applicants.get(0).getUsername(), "First applicant's username should be applicant1");
        assertEquals("applicant2", applicants.get(1).getUsername(), "Second applicant's username should be applicant2");
    }

    @Test
    public void testHireUser_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setCv(new CV()); // User has a CV

        UserRoleEntity employeeRole = new UserRoleEntity();
        employeeRole.setName(RoleName.EMPLOYEE);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRoleRepository.findByName(RoleName.EMPLOYEE)).thenReturn(Optional.of(employeeRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved User

        // Act
        applicantService.hireUser(userId);

        // Assert
        assertTrue(user.getRoles().contains(employeeRole), "User should have the EMPLOYEE role");
        assertNull("User's CV should be removed", user.getCv());
    }

    @Test
    public void testHireUser_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> applicantService.hireUser(userId));
    }

    @Test
    public void testRejectUser_Success() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setCv(new CV()); // User has a CV

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved User

        // Act
        applicantService.rejectUser(userId);

        // Assert
        assertNull("User's CV should be removed", user.getCv());
    }

    @Test
    public void testRejectUser_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> applicantService.rejectUser(userId));
    }
    /*  ~~~~~~~~~~~~  END OF APPLICANT_SERVICE_SERVICE TESTING  ~~~~~~~~~~~~*/

    /*  ~~~~~~~~~~~~  START OF BANNED_EMPLOYEE_SERVICE TESTING  ~~~~~~~~~~~~*/
    @Test
    public void testFindAllBannedEmployees() {
        // Arrange
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("employee1");
        user1.setEmail("employee1@example.com");
        user1.setIsBanned(true);

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("employee2");
        user2.setEmail("employee2@example.com");
        user2.setIsBanned(true);

        List<User> bannedUsers = List.of(user1, user2);
        when(userRepository.findAllByIsBanned(true)).thenReturn(bannedUsers);

        // Act
        List<BannedEmployeeView> bannedEmployeeViews = bannedEmployeeService.findAllBannedEmployees();

        // Assert
        assertEquals(2, bannedEmployeeViews.size(), "There should be two banned employees");
        assertEquals("employee1", bannedEmployeeViews.get(0).getUsername(), "First banned employee's username should be employee1");
        assertEquals("employee2", bannedEmployeeViews.get(1).getUsername(), "Second banned employee's username should be employee2");
    }

    @Test
    public void testFireEmployee() {
        // Arrange
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setIsBanned(true);

        UserRoleEntity employeeRole = new UserRoleEntity();
        employeeRole.setName(RoleName.EMPLOYEE);

        UserRoleEntity adminRole = new UserRoleEntity();
        adminRole.setName(RoleName.ADMIN);

        List<UserRoleEntity> roles = new ArrayList<>();
        roles.add(employeeRole);
        roles.add(adminRole);
        user.setRoles(roles);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0)); // Return the saved User

        // Act
        bannedEmployeeService.fireEmployee(userId);

        // Assert
        assertFalse(user.getRoles().stream().anyMatch(role -> role.getName() == RoleName.EMPLOYEE), "User should not have EMPLOYEE role after firing");
        assertFalse(user.getRoles().stream().anyMatch(role -> role.getName() == RoleName.ADMIN), "User should not have ADMIN role after firing");
        assertFalse(user.getIsBanned(), "User should not be banned after being fired");
    }

    @Test
    public void testFireEmployee_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> bannedEmployeeService.fireEmployee(userId));
    }
    /*  ~~~~~~~~~~~~  END OF BANNED_EMPLOYEE_SERVICE TESTING  ~~~~~~~~~~~~*/

    /*  ~~~~~~~~~~~~  START OF CV_SERVICE TESTING  ~~~~~~~~~~~~*/
    @Test
    public void testAddApplicationFormToUserWithUsername() {
        // Arrange
        String username = "testuser";
        ApplicationFormDTO applicationFormDTO = new ApplicationFormDTO();
        applicationFormDTO.setExperienceLevel(ExperienceLevel.INTERMEDIATE);
        applicationFormDTO.setPersonalMotivation("I love nails!");

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(cvRepository.save(any(CV.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        cvService.addApplicationFormToUserWithUsername(applicationFormDTO, username);

        // Assert
        verify(cvRepository, times(1)).save(any(CV.class));
        verify(userRepository, times(1)).save(user);

        assertEquals(applicationFormDTO.getExperienceLevel(), user.getCv().getExperienceLevel(), "CV experience level should match the input");
        assertEquals(applicationFormDTO.getPersonalMotivation(), user.getCv().getPersonalMotivation(), "CV personal motivation should match the input");
        assertEquals(user, user.getCv().getUser(), "The user in the CV should match the user being tested");
    }

    @Test
    public void testAddApplicationFormToUserWithUsername_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        ApplicationFormDTO applicationFormDTO = new ApplicationFormDTO();

        when(userService.findByUsername(username)).thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            cvService.addApplicationFormToUserWithUsername(applicationFormDTO, username));
    }
    /*  ~~~~~~~~~~~~  END OF CV_SERVICE TESTING  ~~~~~~~~~~~~*/

    /*  ~~~~~~~~~~~~  START OF DESIGN_SERVICE TESTING  ~~~~~~~~~~~~*/
    @Test
    public void testCreateDesign_Successful() throws IOException {
        // Arrange
        String username = "testuser";
        String fileName = "design.png";
        String filePath = "/uploads/" + fileName;

        AddDesignDTO data = new AddDesignDTO();
        data.setName("Test Design");
        data.setCategory("GELISH");
        data.setImageUrl(mockMultipartFile);

        when(mockMultipartFile.getOriginalFilename()).thenReturn(fileName);
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getBytes()).thenReturn("dummy content".getBytes());

        doNothing().when(designServiceClient).createDesign(anyString(), any(String.class), any(MultipartFile.class), eq(username));

        // Act
        boolean result = designService.create(data, username);

        // Assert
        assertTrue(result, "The design creation should return true");
        verify(designServiceClient, times(1)).createDesign(data.getName(), data.getCategory(), data.getImageUrl(), username);

        // Ensure file save was attempted
        Path path = Paths.get(uploadDir, fileName);
        assertTrue(Files.exists(path), "The file should have been saved at the expected path");

        // Clean up the created file for the test (optional, but good practice)
        Files.deleteIfExists(path);
    }

    @Test
    public void testCreateDesign_FileIsEmpty() {
        // Arrange
        AddDesignDTO data = new AddDesignDTO();
        data.setName("Test Design");
        data.setCategory("GELISH");
        data.setImageUrl(mockMultipartFile);

        when(mockMultipartFile.isEmpty()).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
            designService.create(data, "testuser"));

        assertEquals("File must not be empty", exception.getMessage(), "Exception message should be 'File must not be empty'");
    }

    @Test
    public void testCreateDesign_FileSaveThrowsIOException() throws IOException {
        // Arrange
        AddDesignDTO data = new AddDesignDTO();
        data.setName("Test Design");
        data.setCategory("GELISH");
        data.setImageUrl(mockMultipartFile);

        when(mockMultipartFile.getOriginalFilename()).thenReturn("design.png");
        when(mockMultipartFile.isEmpty()).thenReturn(false);
        when(mockMultipartFile.getBytes()).thenThrow(new IOException("Test IO Exception"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            designService.create(data, "testuser"));

        assertEquals("Could not save file: design.png", exception.getMessage(), "Exception message should reflect the file save error");
    }

    @Test
    public void testAddToFavourites_Successful() {
        // Arrange
        String username = "testuser";
        long designId = 1L;

        User user = new User();
        user.setUsername(username);
        Design design = new Design();
        design.setId(designId);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(designRepository.findById(designId)).thenReturn(Optional.of(design));

        // Act
        designService.addToFavourites(username, designId);

        // Assert
        verify(userRepository, times(1)).save(user);
        assertTrue(user.getFavouriteDesigns().contains(design), "Design should be added to user's favourites");
    }

    @Test
    public void testAddToFavourites_UserNotFound() {
        // Arrange
        String username = "nonexistentuser";
        long designId = 1L;

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        designService.addToFavourites(username, designId);

        // Assert
        verify(userRepository, never()).save(any());
        verify(designRepository, never()).findById(anyLong());
    }

    @Test
    public void testAddToFavourites_DesignNotFound() {
        // Arrange
        String username = "testuser";
        long designId = 1L;

        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(designRepository.findById(designId)).thenReturn(Optional.empty());

        // Act
        designService.addToFavourites(username, designId);

        // Assert
        verify(userRepository, never()).save(any());
    }
}