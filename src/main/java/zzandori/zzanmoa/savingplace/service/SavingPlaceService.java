package zzandori.zzanmoa.savingplace.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import zzandori.zzanmoa.googleapi.dto.Location;
import zzandori.zzanmoa.googleapi.service.GoogleMapApiService;
import zzandori.zzanmoa.savingplace.dto.CategoryPriceDTO;
import zzandori.zzanmoa.savingplace.dto.ItemInfoDTO;
import zzandori.zzanmoa.savingplace.dto.StoreInfoDTO;
import zzandori.zzanmoa.savingplace.entity.SavingItem;
import zzandori.zzanmoa.savingplace.entity.SavingStore;
import zzandori.zzanmoa.savingplace.repository.SavingItemRepository;
import zzandori.zzanmoa.savingplace.repository.SavingStoreRepository;
import zzandori.zzanmoa.thriftstore.entity.ThriftStore;
import zzandori.zzanmoa.thriftstore.repository.ThriftStoreRepository;
import zzandori.zzanmoa.thriftstore.service.ThriftStoreService;

@RequiredArgsConstructor
@Service
public class SavingPlaceService {

    private final SavingStoreRepository savingStoreRepository;
    private final SavingItemRepository savingItemRepository;

    private static final Logger logger = LoggerFactory.getLogger(ThriftStoreService.class);

    public List<CategoryPriceDTO> getCategoryPrice() {
        List<CategoryPriceDTO> results = savingItemRepository.findCategoryPrices();
        logger.info("Fetched category prices size: {}", results.size());
        return results;
    }

    public List<StoreInfoDTO> getAllStoresWithItems() {
        return savingStoreRepository.findAllStoreWithItems().stream()
            .map(store -> new StoreInfoDTO(
                store.getStoreId(),
                decodeHtmlEntities(store.getStoreName()),
                store.getPhoneNumber(),
                decodeHtmlEntities(store.getAddress()),
                store.getLatitude(),
                store.getLongitude(),
                store.getItems().stream()
                    .map(item -> new ItemInfoDTO(item.getItemId(), item.getItemName(),
                        item.getCategory(), item.getPrice()))
                    .collect(Collectors.toList())))
            .collect(Collectors.toList());
    }



    private String decodeHtmlEntities(String input) {
        String correctedInput = correctHtmlEntities(input);
        return StringEscapeUtils.unescapeHtml4(correctedInput);
    }

    private String correctHtmlEntities(String input) {
        return input.replaceAll("& #(\\d+)", "&#$1;");
    }
}
