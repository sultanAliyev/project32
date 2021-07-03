package project32.project32.controllers;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project32.project32.entities.Items;
import project32.project32.entities.Roles;
import project32.project32.entities.Users;
import project32.project32.services.ItemService;
import project32.project32.services.UserService;


import javax.imageio.IIOException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ItemService itemService;

    @Value("${loadURL}")
    private String loadURL;

    @Value("${targetURL}")
    private String targetURL;

    @GetMapping(value = "/index")
    public String index(Model model){
        List<Items> items = itemService.getAllItems();
        model.addAttribute("items", items);
        return "index";
    }

    @PostMapping(value = "/additem")
    public String addItem(@RequestParam(name = "name")String name,
                          @RequestParam(name = "price") double price,
                          @RequestParam(name = "amount") int amount,
                          @RequestParam(name="pictureURL") String pictureURL){

        Items it = new Items();
        it.setName(name);
        it.setAmount(amount);
        it.setPrice(price);
        it.setPictureURL(pictureURL);


        itemService.addItem(it);

        return "redirect:/index";

    }

    @GetMapping(value = "/deleteuser/{itemId}")
    public String deleteUser(Model model, @PathVariable(name = "itemId") Long id){

        Users user = userService.deleteUser(id);
        model.addAttribute("item", user);

        return "redirect:/adminpage";
    }

    @GetMapping(value = "/upgradeuser/{itemId}")
    public String upgradeUser(Model model, @PathVariable(name = "itemId") Long id){

        Roles userRole_1 = userService.findRoleByName("ROLE_REDAKTOR");
        Roles userRole_2 = userService.findRoleByName("ROLE_USER");
        List<Roles> roles = new ArrayList<>();
        roles.add(userRole_1);
        roles.add(userRole_2);


        Users choosenUser = userService.getUser(id);

        choosenUser.setRoles(roles);

        userService.addUser(choosenUser);

        return "redirect:/adminpage";
    }

    @GetMapping(value = "/downgradeuser/{itemId}")
    public String downgradeUser(Model model, @PathVariable(name = "itemId") Long id){


        Roles userRole_1 = userService.findRoleByName("ROLE_USER");
        List<Roles> roles = new ArrayList<>();
        roles.add(userRole_1);



        Users choosenUser = userService.getUser(id);

        choosenUser.setRoles(roles);

        userService.addUser(choosenUser);

        return "redirect:/adminpage";
    }

    @GetMapping(value = "/details/{itemId}")
    public String getItem(Model model, @PathVariable(name = "itemId") Long id){

        Items item = itemService.getItem(id);
        model.addAttribute("item", item);

        return "details";
    }

    @PostMapping(value = "/savetask", params = "action=save")
    public String saveTask(@RequestParam(name = "id") Long id,
                           @RequestParam(name="name") String name_,
                           @RequestParam(name="price") double price,
                           @RequestParam(name="amount") int amount,
                           @RequestParam(name="pictureURL") String pictureURL){

        Items item  = new Items(id, name_, price, amount,pictureURL);
        itemService.saveItem(item);


        return "redirect:/index";
    }

    @PostMapping(value = "/savetask", params = "action=delete")
    public String deleteTask(@RequestParam(name = "id") Long id){

        itemService.deleteItem(id);

        return "redirect:/index";

    }

    @GetMapping(value = "/loginpage")
    public String login(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "profile";
    }

    @PostMapping(value = "/updatepassword")
    @PreAuthorize("isAuthenticated()")
    public String updatePassword(@RequestParam(name = "old_password") String oldPassword,
                                 @RequestParam(name = "new_password") String newPassword,
                                 @RequestParam(name = "re_new_password") String reNewPassword){

        Users currentUser = getUser();

        if (newPassword.trim().equals(reNewPassword.trim())){

            if (passwordEncoder.matches(oldPassword, currentUser.getPassword())){

                currentUser.setPassword(passwordEncoder.encode(newPassword));
                userService.saveUser(currentUser);

                return "redirect:/profile?success";
            }

        }
        return "redirect:/profile?error";

    }

    @PostMapping(value = "/updateinfo")
    @PreAuthorize("isAuthenticated()")
    public String updatePassword(@RequestParam(name = "address") String address,
                                 @RequestParam(name = "phone_number") String phone_number){
        Users currentUser = getUser();
        currentUser.setAddress(address);
        currentUser.setPhone_number(phone_number);

        userService.saveUser(currentUser);

        return "redirect:/profile";

    }

    @GetMapping(value = "adminpage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String admin(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        List<Users> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "admin";
    }



    @GetMapping(value = "/accessdenied")
    public String accessdenied(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "accessdenied";
    }

    @GetMapping(value = "/registerpage")
    public String registerPage(Model model){
        model.addAttribute("CURRENT_USER", getUser());
        return "register";
    }

    @PostMapping(value = "/register")
    public String register(@RequestParam(name = "user_email") String userEmail,
                           @RequestParam(name = "user_password") String userPassword,
                           @RequestParam(name = "re_user_password")String reUserPassword,
                           @RequestParam(name = "user_full_name") String fullName){

        Users users = userService.checkUser(userEmail);

        if (users==null && userPassword.trim().equals(reUserPassword.trim())){

            Roles userRole = userService.findRoleByName("ROLE_USER");
            List<Roles> roles = new ArrayList<>();
            roles.add(userRole);

            Users newUser = new Users();
            newUser.setEmail(userEmail);
            newUser.setRoles(roles);
            newUser.setFullName(fullName);
            newUser.setPassword(passwordEncoder.encode(userPassword));

            userService.addUser(newUser);

            return "redirect:/registerpage?success";

        }

        return "redirect:/registerpage?error";
    }

    @PostMapping(value = "/uploadava")
    @PreAuthorize("isAuthenticated()")
    public String upload(@RequestParam(name = "user_ava")MultipartFile file){

        try {

            if (file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")){

                String fileName = DigestUtils.sha1Hex("avatar_"+getUser().getId()) +".jpg";
                byte bytes[] = file.getBytes();

                Path path = Paths.get(targetURL+fileName);
                Files.write(path, bytes);

                Users currentUser = getUser();
                currentUser.setAvatars(fileName);
                userService.saveUser(currentUser);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return "redirect:/profile";

    }

    @GetMapping(value = "/viewava/{avaURL}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody byte[] viewava(@PathVariable(name = "avaURL") String avaURL) throws IOException {

        String picURL = loadURL + "default.png";

        if (avaURL!=null){
            picURL = loadURL + avaURL;
        }

        InputStream in;

        try {
            ClassPathResource classPathResource = new ClassPathResource(picURL);
            in = classPathResource.getInputStream();
        }catch (Exception e){
            picURL = loadURL + "default.png";
            ClassPathResource classPathResource = new ClassPathResource(picURL);
            in = classPathResource.getInputStream();
        }

        return IOUtils.toByteArray(in);
    }

    private Users getUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(!(authentication instanceof AnonymousAuthenticationToken)){
            Users users = (Users) authentication.getPrincipal();
            return users;
        }
        return null;
    }
}
