package project32.project32.services.imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project32.project32.entities.Items;
import project32.project32.repositories.ItemRepository;
import project32.project32.services.ItemService;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Items> getAllItems() {
        return itemRepository.findAll();
    }

    @Override
    public Items getItem(Long id) {
        return itemRepository.findById(id).orElse(null);
    }

    @Override
    public Items addItem(Items item) {
        return itemRepository.save(item);
    }

    @Override
    public Items saveItem(Items item) {
        return itemRepository.save(item);
    }

    @Override
    public Items deleteItem(Long id) {
        itemRepository.delete(itemRepository.getById(id));
        return null;
    }

    @Override
    public List<Items> getAllItemsById(List<Long> items) {
        return itemRepository.findAllById(items);
    }
}
