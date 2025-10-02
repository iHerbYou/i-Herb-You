package com.iherbyou.banner.service;

import com.iherbyou.banner.entity.Banner;
import com.iherbyou.exception.banner.BannerNotFoundException;
import com.iherbyou.exception.banner.DuplicateImageUrlException;
import com.iherbyou.exception.banner.DuplicateSortOrderException;
import com.iherbyou.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository bannerRepository;

    @Transactional
    public Banner create(Banner banner) {
        validateSortOrderNotExists(banner.getSortOrder());
        validateImageUrlNotExists(banner.getImageUrl());
        return bannerRepository.save(banner);
    }

    public List<Banner> findAll() {
        return bannerRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"));
    }

    @Transactional
    public void delete(Long id) {
        validateBannerExists(id);
        bannerRepository.deleteById(id);
    }

    private void validateSortOrderNotExists(Integer sortOrder) {
        if (bannerRepository.existsBySortOrder(sortOrder)) {
            throw new DuplicateSortOrderException(sortOrder);
        }
    }

    private void validateImageUrlNotExists(String imageUrl) {
        if (bannerRepository.existsByImageUrl(imageUrl)) {
            throw new DuplicateImageUrlException(imageUrl);
        }
    }

    private void validateBannerExists(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new BannerNotFoundException(id);
        }
    }
}