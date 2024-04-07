package com.example.ElectronicStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebSecurity
@EnableWebMvc
public class ElectronicStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectronicStoreApplication.class, args);
    }


//    @Autowired
//    private RoleRepository roleRepository;


//    @Override
//    public void run(String... args) throws Exception {
//
//        Role role2 = Role.builder()
//                .roleName(RoleName.USER.toString())
//                .build();
//
//
//        Role role1 = Role.builder()
//                .roleName(RoleName.ADMIN.toString())
//                .build();
//
//        roleRepository.save(role2);
//        roleRepository.save(role1);
//    }
}
