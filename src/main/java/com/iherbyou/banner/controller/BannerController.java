package com.iherbyou.banner.controller;

import com.iherbyou.banner.dto.AddBannerRequest;
import com.iherbyou.banner.dto.AddBannerResponse;
import com.iherbyou.banner.entity.Banner;
import com.iherbyou.banner.mapper.BannerMapper;
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

    private final BannerService bannerService;
    private final BannerMapper bannerMapper;

    @GetMapping
    public ResponseEntity<List<AddBannerResponse>> findAll() {
        List<Banner> banners = bannerService.findAll();
        return ResponseEntity.ok(bannerMapper.toResponseList(banners));
    }

    @PostMapping
    public ResponseEntity<AddBannerResponse> create(@RequestBody @Valid AddBannerRequest request) {
        Banner banner = bannerMapper.toEntity(request);
        Banner saved = bannerService.create(banner);
        return ResponseEntity.status(HttpStatus.CREATED).body(bannerMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bannerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}