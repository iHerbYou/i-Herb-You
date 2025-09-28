package com.iherbyou.banner.service;

import com.iherbyou.banner.entity.Banner;
import com.iherbyou.banner.repository.BannerRepository;
import jakarta.persistence.EntityNotFoundException;
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

    /**
     * 배너 생성
     */
    @Transactional
    public Banner create(String bannerName, String imageUrl) {
        Banner banner = Banner.builder()
                .bannerName(bannerName)
                .imageUrl(imageUrl)
                .build();
        return bannerRepository.save(banner);
    }

    /** 배너 생성 (sortOrder 중복 검사) */
    @Transactional
    public Banner create(String bannerName, String imageUrl, Integer sortOrder) {
        if (sortOrder == null) {
            throw new IllegalArgumentException("sortOrder는 필수입니다.");
        }
        if (bannerRepository.existsBySortOrder(sortOrder)) {
            throw new IllegalArgumentException("이미 사용 중인 정렬 순서입니다: " + sortOrder);
        }
        Banner banner = Banner.builder()
                .bannerName(bannerName)
                .imageUrl(imageUrl)
                .sortOrder(sortOrder)
                .build();
        return bannerRepository.save(banner);
    }

    /**
     * 목록 조회 (페이지네이션 없음)
     */
    public List<Banner> listAll() {
        return bannerRepository.findAll(Sort.by(Sort.Direction.ASC, "sortOrder"));
    }

    /**
     * 배너 삭제
     */
    public void delete(Long id) {
        if (!bannerRepository.existsById(id)) {
            throw new EntityNotFoundException("Banner not found");
        }
        bannerRepository.deleteById(id);
    }
}
