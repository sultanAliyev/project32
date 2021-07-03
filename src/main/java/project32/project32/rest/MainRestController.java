package project32.project32.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project32.project32.entities.Items;
import project32.project32.services.ItemService;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class MainRestController {

    @Autowired
    private ItemService itemService;

    @GetMapping(value = "/getallitems")
    public ResponseEntity<List<Items>> getAllItems(){
        return new ResponseEntity<List<Items>>(itemService.getAllItems(), HttpStatus.OK);
    }

    @PostMapping(value = "/additem")
    public ResponseEntity<Items> addItem(@RequestBody Items item){
        return new ResponseEntity<Items>(itemService.addItem(item), HttpStatus.OK);
    }

    @PostMapping(value = "/toadditem")
    public ResponseEntity<Items> toAddItem(@RequestParam(name = "name") String name,
                                           @RequestParam(name = "price") double price,
                                           @RequestParam(name = "amount") int amount){
        Items item = new Items();
        item.setName(name);
        item.setPrice(price);
        item.setAmount(amount);

        return new ResponseEntity<Items>(itemService.addItem(item), HttpStatus.OK);
    }

    @GetMapping(value = "/getitem/{id}")
    public ResponseEntity<Items> getItem(@PathVariable(name = "id") Long id){
        return new ResponseEntity<Items>(itemService.getItem(id), HttpStatus.OK);
    }

}
