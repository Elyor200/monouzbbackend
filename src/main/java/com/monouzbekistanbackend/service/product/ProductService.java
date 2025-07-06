package com.monouzbekistanbackend.service.product;

import com.monouzbekistanbackend.dto.*;
import com.monouzbekistanbackend.entity.Category;
import com.monouzbekistanbackend.entity.Product;
import com.monouzbekistanbackend.entity.Season;
import com.monouzbekistanbackend.enums.CategoryEnum;
import com.monouzbekistanbackend.enums.SeasonEnum;
import com.monouzbekistanbackend.repository.CategoryRepository;
import com.monouzbekistanbackend.repository.FavoriteProductRepository;
import com.monouzbekistanbackend.repository.ProductRepository;
import com.monouzbekistanbackend.repository.SeasonRepository;
import com.monouzbekistanbackend.service.IdGeneratorService;
import com.monouzbekistanbackend.service.SeasonService;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final IdGeneratorService idGeneratorService;
    private final CategoryRepository categoryRepository;
    private final SeasonRepository seasonRepository;
    private final SeasonService seasonService;
    private final FavoriteProductRepository favoriteProductRepository;

    public ProductService(ProductRepository productRepository,
                          IdGeneratorService idGeneratorService,
                          CategoryRepository categoryRepository,
                          SeasonRepository seasonRepository, SeasonService seasonService, FavoriteProductRepository favoriteProductRepository) {
        this.productRepository = productRepository;
        this.idGeneratorService = idGeneratorService;
        this.categoryRepository = categoryRepository;
        this.seasonRepository = seasonRepository;
        this.seasonService = seasonService;
        this.favoriteProductRepository = favoriteProductRepository;
    }

    public ResponseProductDto createProduct(RequestProductDto dto) {
        Category category = categoryRepository.findByCategoryId(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Season season = null;
        if (dto.getSeasonId() != null) {
             season = seasonRepository.findBySeasonId(dto.getSeasonId())
                    .orElseThrow(() -> new RuntimeException("Season not found"));
        }

        String productId = idGeneratorService.generateProductId();
        Product product = new Product();
        product.setProductId(productId);
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setAvailable(dto.isAvailable());
        product.setCreatedAt(LocalDateTime.now());
        product.setUpdatedAt(LocalDateTime.now());
        product.setCategory(category);
        product.setSeason(season);
        product.setColor(dto.getColor());
        product.setAvailableColors(dto.getAvailableColors());

        Product savedProduct = productRepository.save(product);
        return mapToResponseDto(savedProduct);
    }

    public List<ResponseProductDto> getAllProducts(Long telegramUserId) {
        List<Product> productsList = productRepository.findAll();
        List<String> favoriteIds = favoriteProductRepository.findFavoriteProductIdsByTelegramUserId(telegramUserId);

        return productsList.stream()
                .map(product -> {
                    boolean isFavorite = favoriteIds.contains(product.getProductId());
                    return mapToResponseFavoriteProducts(product, isFavorite);
                })
                .toList();
    }

    public List<ResponseProductDto> getAllAvailableProducts() {
        List<Product> productsList = productRepository.findByAvailableTrue();
        return productsList.stream().map(this::mapToResponseDto)
                .toList();
    }

    public List<ResponseProductDto> getProductBySeason(SeasonEnum season) {
        List<Product> productList = productRepository.findBySeasonName(String.valueOf(season));
        return productList.stream().map(this::mapToResponseDto).toList();
    }


    public List<ResponseProductDto> getProductByCategory(CategoryEnum categoryEnum) {
        Optional<Category> categoryOptional = categoryRepository.findByCategory(categoryEnum);

        if (!categoryOptional.isPresent()) {
            return Collections.emptyList();
        }

        Category category = categoryOptional.get();
        List<Product> productList = productRepository.findByCategory(category);
        return productList.stream().map(this::mapToResponseDto).toList();
    }

    public ResponseProductDto getProductById(String productId) {
        Optional<Product> optionalProduct = productRepository.findByProductId(productId);
        if (!optionalProduct.isPresent()) {
            throw new RuntimeException("Product not found");
        }

        Product product = optionalProduct.get();
        return mapToResponseDto(product);
    }

    public List<FavoriteProductResponse> getAllProductsWithFavoriteStatus(Long telegramUserId) {
        List<Product> productList = productRepository.findByAvailableTrue();
        List<String> favoriteProductIds = favoriteProductRepository.findFavoriteProductsByTelegramUserId(telegramUserId);

        Set<String> favoriteProductsIdSet = new HashSet<>(favoriteProductIds);
        return productList.stream().map(product -> new FavoriteProductResponse(
                product.getProductId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isAvailable(),
                product.getCategory(),
                product.getSeason(),
                product.getPhotos().stream()
                        .map(photo ->  new ProductPhotoDto(photo.getProductPhotoId(), photo.getUrl()))
                        .toList(),
                favoriteProductsIdSet.contains(product.getProductId())
        ))
        .toList();
    }

    public ResponseProductDto mapToResponseDto(Product product) {
        ResponseProductDto dto = new ResponseProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setAvailable(product.isAvailable());
        dto.setCategory(product.getCategory().getCategory().name());
        dto.setSeason(product.getSeason().getName());

        List<String> imageUrl = product.getPhotos().stream()
                .map(photos -> {
                    String url = photos.getUrl();
                    if (url.startsWith("/") && url.length() > 2 && url.charAt(2) == ':') {
                        url = url.substring(1);
                    }
                    String filename = Paths.get(url).getFileName().toString();
                    return "/images/" + filename;
                })
                .collect(Collectors.toList());

        dto.setImageUrl(imageUrl);
        dto.setColor(product.getColor());
        dto.setAvailableColors(product.getAvailableColors());
        return dto;
    }

    public ResponseProductDto mapToResponseFavoriteProducts(Product product, boolean isFavorite) {
        ResponseProductDto dto = new ResponseProductDto();
        dto.setProductId(product.getProductId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setAvailable(product.isAvailable());
        dto.setCategory(product.getCategory().getCategory().name());
        dto.setSeason(product.getSeason().getName());

        List<String> imageUrl = product.getPhotos().stream()
                .map(photos -> {
                    String url = photos.getUrl();
                    if (url.startsWith("/") && url.length() > 2 && url.charAt(2) == ':') {
                        url = url.substring(1);
                    }
                    String filename = Paths.get(url).getFileName().toString();
                    return "/images/" + filename;
                })
                .collect(Collectors.toList());

        dto.setImageUrl(imageUrl);
        dto.setFavorite(isFavorite);
        return dto;
    }

}
