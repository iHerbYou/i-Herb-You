package com.iherbyou.banner.controller;

import com.iherbyou.banner.dto.AddBannerRequest;
import com.iherbyou.banner.dto.AddBannerResponse;
import com.iherbyou.banner.dto.DeleteBannerResponse;
import com.iherbyou.banner.entity.Banner;
import com.iherbyou.banner.repository.BannerRepository;
import com.iherbyou.banner.service.BannerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerRepository bannerRepository;
    private final BannerService bannerService;

    // 배너 목록 조회
    @GetMapping()
    public List<Banner> listAll() {
        return bannerRepository.findAll();
    }

    // 배너 추가
    @PostMapping
    public ResponseEntity<AddBannerResponse> addBanner(@RequestBody @Valid AddBannerRequest request) {
        try {
            Banner saved = bannerService.create(
                    request.getBannerName(),
                    request.getImageUrl(),
                    request.getSortOrder()
            );
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(AddBannerResponse.builder()
                            .message("배너가 등록되었습니다. id=" + saved.getId())
                            .build());
        } catch (IllegalArgumentException e) {
            // sortOrder 중복 등
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(AddBannerResponse.builder()
                            .message(e.getMessage())
                            .build());
        }
    }

    // 배너 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteBannerResponse> deleteBanner(@PathVariable Long id) {
        bannerRepository.deleteById(id);
        return ResponseEntity.ok(new DeleteBannerResponse("배너가 삭제되었습니다."));
    }

}
