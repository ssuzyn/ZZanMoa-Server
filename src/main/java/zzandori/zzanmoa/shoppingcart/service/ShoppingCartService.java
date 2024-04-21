package zzandori.zzanmoa.shoppingcart.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.grocery.entity.Grocery;
import zzandori.zzanmoa.grocery.service.GroceryService;
import zzandori.zzanmoa.shoppingcart.dto.ShoppingCartResponseDto;

@RequiredArgsConstructor
@Service
public class ShoppingCartService {

    private final GroceryService groceryService;

    public ShoppingCartResponseDto shoppingCartAllList() {
        List<Grocery> groceries = groceryService.getGroceries();

        return new ShoppingCartResponseDto(groceries.stream()
                    .map(Grocery::getItemName)
                    .collect(Collectors.toList()));
    }
}
