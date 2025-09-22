package com.iherbyou.ordering.common;

import com.iherbyou.common.Code;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CodeFinder {
    private final CodeRepository repo;

    public Code get(String group, String code) {
        return repo.find(group, code)
                .orElseThrow(() ->
                        new IllegalArgumentException(group + ":" + code + " not found"));
    }
}
