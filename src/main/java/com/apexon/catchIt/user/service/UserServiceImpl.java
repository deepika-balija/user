package com.apexon.catchIt.user.service;

import com.apexon.catchIt.user.dto.*;
import com.apexon.catchIt.user.mapper.UserMapper;
import com.apexon.catchIt.user.model.Role;
import com.apexon.catchIt.user.model.Roles;
import com.apexon.catchIt.user.model.User;
import com.apexon.catchIt.user.repositroy.RolesRepo;
import com.apexon.catchIt.user.repositroy.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl {

    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserMapper userMapper;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    RolesRepo rolesRepo;

    public UserDto registerUser(UserRegisterDto user) {
        if (userRepo.existsByEmail(user.getEmail()))
            throw new RuntimeException("Email already exists");
        System.out.println("User pass bfr encoding is " + user.getPassword());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        System.out.println("User pass after encdeing is " + user.getPassword());
        User u=new User();
        u.setPassword(user.getPassword());
        u.setUserName(user.getUserName());
        u.setEmail(user.getEmail());
        u.setRole(user.getRole());


        return userMapper.convertUserToUserDto(userRepo.save(u));

    }

    public UserDto updateUser(UpdateUserDto user, Long id) {
        if (userRepo.existsByEmailAndIdNot(user.getEmail(), id)) {
            throw new RuntimeException("Email already exists");
        }
        Optional<User> optionalUser = userRepo.findById(id);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("user not found with " + id);
        }
        User exisitngUser = optionalUser.get();
        if(user.getUserName()!=null)
        {
        exisitngUser.setUserName(user.getUserName());
        }
        if(user.getEmail()!=null)
        exisitngUser.setEmail(user.getEmail());


        /*exisitngUser.setPassword(user.getPassword());
        exisitngUser.setRoles(user.getRoles());*/
        // userRepo.save(exisitngUser);
        return (userMapper.convertUserToUserDto(userRepo.save(exisitngUser)));


    }

    public UserAdminDto getUserById(Long id) {
        Optional<User> user = userRepo.findById(id);
        if (!user.isPresent()) {
            throw new RuntimeException("User not found with " + id);
        }
        User exisitngUser = user.get();

        return userMapper.convertUserToUserAdminDto(exisitngUser);

    }

    public boolean updateUserPassword(Long id, UpdatePasswordDto updatePasswordDto) {
        Optional<User>user=userRepo.findById(id);
        if(!user.isPresent()){
            throw new RuntimeException("User not found with "+ id);
        }
        User existingUser=user.get();
        if(bCryptPasswordEncoder.matches(updatePasswordDto.getPassword(),existingUser.getPassword()))
        {
            throw new RuntimeException("New password should not be same as old password,Try with different password");
        }
        existingUser.setPassword(bCryptPasswordEncoder.encode(updatePasswordDto.getPassword()));
        userRepo.save(existingUser);
        return true;

    }

    public List<UserAdminDto> fetchAllUsersByAdminId(Long id) {
        Optional<User> user=userRepo.findById(id);
        if(!user.isPresent())
        {
            throw new RuntimeException("User not found with "+id);
        }
       if(user.get().getRole()!= Roles.ADMIN)
       {
           throw new RuntimeException("you dont have enough permissions to access");
       }
       /*return userRepo.findAll().stream().filter(u->u.getRole()!= Roles.ADMIN).map(userMapper::convertUserToUserAdminDto)
               .collect(Collectors.toList());*/
        return userRepo.findAll().stream().map(userMapper::convertUserToUserAdminDto)
                .collect(Collectors.toList());
    }

    public UserAdminDto updateUserAccountDetails(Long adminId, ManageUserAccountDto manageUserAccountDto) {
        Optional<User> adminUser=userRepo.findById(adminId);
        if(!adminUser.isPresent())
        {
            throw new RuntimeException("User not found with "+adminId);
        }
        if(adminUser.get().getRole()!=Roles.ADMIN)
        {
            throw new RuntimeException("you dont have enough permissions to access");
        }
        Optional<User> normalUser=userRepo.findById(manageUserAccountDto.getId());
        if(!normalUser.isPresent())
        {
            throw new RuntimeException("User not found with "+normalUser);
        }
        User actualUser=normalUser.get();
        actualUser.setAccountExpired(manageUserAccountDto.isAccountExpired());
        actualUser.setAccountLocked(manageUserAccountDto.isAccountLocked());
        actualUser.setCredentialsExpired(manageUserAccountDto.isCredentialsExpired());
        return userMapper.convertUserToUserAdminDto(userRepo.save(actualUser));

    }

    @Transactional
    public boolean assignRolesToUser(Long userId, Roles roleTypes, Long id) {
        // Check if the user exists
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // Check if the admin has permission (only ADMIN can assign roles)
        Optional<User> isAdmin=userRepo.findById(id);
        if(isAdmin.isEmpty())
        {
            throw new RuntimeException("User not found with "+id);
        }
        User adminUser=isAdmin.get();
        if (!adminUser.getRole().equals(Roles.ADMIN)) {
            throw new RuntimeException("Access Denied: Only Admins can assign roles.");
        }



       // user.setRoles(newRoles);
       /* Role role=new Role();
        role.setRoleName(Roles.ADMIN);
        user.setRole(Roles.ADMIN);*/
        user.setRole(roleTypes);

        //rolesRepo.save(role);

        // Save the updated user
        userRepo.save(user);

        Optional<Role> optionalRole=rolesRepo.findByRoleName(roleTypes);
        if(optionalRole.isEmpty())
        {
            Role role=new Role();
            role.setRoleName(roleTypes);

        }

        return true;
    }

    public UserAdminDto getUserByName(String uname) {


            Optional<User> user = userRepo.findByUserName(uname);
            if (!user.isPresent()) {
                throw new RuntimeException("User not found with " + uname);
            }
            User exisitngUser = user.get();

            return userMapper.convertUserToUserAdminDto(exisitngUser);


    }

    public List<UserDto> fetchAllUsers() {
        List<User> user = userRepo.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for (User u : user)
            userDtoList.add(userMapper.convertUserToUserDto(u));
        System.out.println("fetch All users " + userDtoList.get(0));
        return userDtoList;
    }

}
