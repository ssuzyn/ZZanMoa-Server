package zzandori.zzanmoa.savingplace.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.savingplace.dto.CategoryPriceDTO;
import zzandori.zzanmoa.savingplace.dto.ItemInfoDTO;
import zzandori.zzanmoa.savingplace.dto.StoreInfoDTO;
import zzandori.zzanmoa.savingplace.repository.SavingItemRepository;
import zzandori.zzanmoa.savingplace.repository.SavingStoreRepository;
import zzandori.zzanmoa.thriftstore.service.ThriftStoreService;

@RequiredArgsConstructor
@Service
public class SavingPlaceService {

    private final SavingStoreRepository savingStoreRepository;
    private final SavingItemRepository savingItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(ThriftStoreService.class);


    public List<CategoryPriceDTO> getCategoryPrice(){
        List<CategoryPriceDTO> results = savingItemRepository.findCategoryPrices();
        logger.info("Fetched category prices size: {}", results.size());
        return results;
    }

    public List<StoreInfoDTO> getAllStoresWithItems() {
        return savingStoreRepository.findAllStoreWithItems().stream()
            .map(store -> new StoreInfoDTO(
                store.getStoreId(),
                store.getStoreName(),
                store.getPhoneNumber(),
                store.getAddress(),
                store.getItems().stream()
                    .map(item -> new ItemInfoDTO(item.getItemId(), item.getItemName(), item.getCategory(), item.getPrice()))
                    .collect(Collectors.toList())))
            .collect(Collectors.toList());
    }

}