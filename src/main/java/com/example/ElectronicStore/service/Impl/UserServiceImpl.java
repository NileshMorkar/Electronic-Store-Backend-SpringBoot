package com.example.ElectronicStore.service.Impl;

import com.example.ElectronicStore.Helper.Helper;
import com.example.ElectronicStore.dto.ApiResponseMessage;
import com.example.ElectronicStore.dto.PageableResponse;
import com.example.ElectronicStore.dto.UserRequest;
import com.example.ElectronicStore.dto.UserResponse;
import com.example.ElectronicStore.entity.Role;
import com.example.ElectronicStore.entity.UserEntity;
import com.example.ElectronicStore.enums.RoleName;
import com.example.ElectronicStore.exception.GlobalException;
import com.example.ElectronicStore.repository.RoleRepository;
import com.example.ElectronicStore.repository.UserRepository;
import com.example.ElectronicStore.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    RoleRepository roleRepository;


    public ResponseEntity<UserResponse> createNormalUser(UserRequest userRequest) {

        Optional<UserEntity> userEntity = userRepository.findByEmail(userRequest.getEmail());

        if (userEntity.isPresent()) {
            throw new GlobalException("User Is Already Present!", HttpStatus.OK);
        }

        //Set Password By Using Encode
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        UserEntity newUser = modelMapper.map(userRequest, UserEntity.class);

        Role role = roleRepository.findByRoleName(RoleName.USER.toString()).orElse(null);

        newUser.getRoles().add(role);

        userRepository.save(newUser);

        UserResponse userResponse = modelMapper.map(newUser, UserResponse.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @Override
    public ResponseEntity<ApiResponseMessage> delete(Long userId) throws GlobalException {

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new GlobalException("UserId Not Present!", HttpStatus.BAD_REQUEST));

        //First delete Image
        try {
            Path path = Paths.get(userEntity.getImage());
            Files.delete(path);
        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        userRepository.delete(userEntity);

        ApiResponseMessage message = ApiResponseMessage
                .builder()
                .message("User Deleted Successfully!")
                .httpStatus(HttpStatus.OK)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Override
    public ResponseEntity<PageableResponse<UserResponse>> getAll(String pageNumber, String pageSize, String sortBy, String sortDir) {

        try {
            Sort sort = (sortDir.equalsIgnoreCase("asc")) ? (Sort.by(sortBy).ascending()) : (Sort.by(sortBy).descending());

            PageRequest pageable = PageRequest.of(Integer.parseInt(pageNumber), Integer.parseInt(pageSize), sort);
            Page<UserEntity> page = userRepository.findAll(pageable);

            PageableResponse<UserResponse> pageableResponse = Helper.getPageableResponse(page, UserResponse.class);

            return ResponseEntity.status(HttpStatus.OK).body(pageableResponse);

        } catch (Exception e) {
            throw new GlobalException(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @Override
    public ResponseEntity<ApiResponseMessage> update(Long userId, UserRequest newUser) throws GlobalException {

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new GlobalException("UserId Not Present!", HttpStatus.BAD_REQUEST));

        userEntity.setGender(newUser.getGender());
        userEntity.setPassword(userEntity.getPassword());
        userEntity.setName(newUser.getName());
        userEntity.setImage(newUser.getImage());

        userRepository.save(userEntity);

        ApiResponseMessage message = ApiResponseMessage
                .builder()
                .message("User Updated!")
                .httpStatus(HttpStatus.OK)
                .success(true)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

    @Override
    public UserResponse getById(Long userId) throws GlobalException {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new GlobalException("UserId Not Present!", HttpStatus.BAD_REQUEST));

        return modelMapper.map(userEntity, UserResponse.class);
    }

    @Override
    public ResponseEntity<UserResponse> getByEmail(String userEmail) throws GlobalException {
        UserEntity userEntity = userRepository.findByEmail(userEmail).orElseThrow(() -> new GlobalException("Email Not Present!", HttpStatus.BAD_REQUEST));
        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @Override
    public ResponseEntity<List<UserResponse>> search(String keyword) {
        List<UserEntity> usersEntity = userRepository.findByNameContaining(keyword);

        List<UserResponse> usersResponse = usersEntity.stream().map(userEntity -> modelMapper.map(userEntity, UserResponse.class)).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(usersResponse);

    }
}
