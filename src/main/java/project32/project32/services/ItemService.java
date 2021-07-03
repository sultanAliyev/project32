package project32.project32.services;

import project32.project32.entities.Items;

import java.util.List;

public interface ItemService {

    List<Items> getAllItems();
    Items getItem(Long id);
    Items addItem(Items item);
    Items saveItem(Items item);
    Items deleteItem(Long id);
    List<Items> getAllItemsById(List<Long> items);

}
