package project32.project32.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project32.project32.entities.Roles;
import project32.project32.entities.Users;
import project32.project32.repositories.RoleRepository;
import project32.project32.repositories.UserRepository;
import project32.project32.services.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        Users user = userRepository.findByEmail(s);
        if (user!=null){
            return user;
        }else {
            throw new UsernameNotFoundException("USER NOT FOUND");
        }

    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users checkUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users deleteUser(Long id) {
       userRepository.delete(userRepository.getById(id));
        return null;
    }


    @Override
    public Roles findRoleByName(String role) {
       return roleRepository.findByRole(role);
    }

    @Override
    public Roles addRole(Long id) {
        return roleRepository.getById(id);
    }

    @Override
    public Users addUser(Users users) {
        return userRepository.save(users);
    }

    @Override
    public Users getUser(Long id) {
        return userRepository.getById(id);
    }

    @Override
    public Users saveUser(Users users) {
        return userRepository.save(users);
    }
}
