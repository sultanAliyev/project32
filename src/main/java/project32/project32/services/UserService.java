package project32.project32.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import project32.project32.entities.Items;
import project32.project32.entities.Roles;
import project32.project32.entities.Users;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<Users> getAllUsers();
    Users checkUser(String email);
    Users deleteUser(Long id);
    Roles findRoleByName(String role);
    Roles addRole(Long id);
    Users addUser(Users users);
    Users getUser(Long id);
    Users saveUser(Users users);

}
