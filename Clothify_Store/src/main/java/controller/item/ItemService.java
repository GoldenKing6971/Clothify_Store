package controller.item;

import model.Item;

import java.util.List;

public interface ItemService {
    boolean addItem();
    boolean updateItem();
    boolean deletetem();
    Item searchItem(String code);
    List<Item> getAll();
}
