package io.ssafy.mallook.domain.style.application;

import io.ssafy.mallook.domain.member.entity.Member;
import io.ssafy.mallook.domain.product.entity.Product;
import io.ssafy.mallook.domain.style.dao.StyleRepository;
import io.ssafy.mallook.domain.style.dto.request.StyleInsertReq;
import io.ssafy.mallook.domain.style.dto.response.*;
import io.ssafy.mallook.domain.style.entity.Style;
import io.ssafy.mallook.domain.style_product.dao.StyleProductRepository;
import io.ssafy.mallook.domain.style_product.entity.StyleProduct;
import io.ssafy.mallook.global.common.code.ErrorCode;
import io.ssafy.mallook.global.exception.BaseExceptionHandler;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class StyleServiceImpl implements StyleService{
    private final StyleRepository styleRepository;
    private final StyleProductRepository styleProductRepository;

    @Transactional(readOnly = true)
    @Override
    public Slice<StyleRes> findStyleListFirst(Pageable pageable) {
        Long maxId = styleRepository.findMaxId();
        return styleRepository.findStylesByIdLessThan(pageable, maxId+1);
    }

    @Override
    @Transactional(readOnly = true)
    public Slice<StyleRes> findStyleList(Pageable pageable, Long cursor) {
        System.out.println(cursor);
        return styleRepository.findStylesByIdLessThan(pageable, cursor+1);
    }

    @Override
    @Transactional(readOnly = true)
    public StyleDetailRes findStyleDetail(Long id) {
        var style = styleRepository.findById(id).orElseThrow(()-> new BaseExceptionHandler(ErrorCode.NOT_FOUND_ERROR));
        return new StyleDetailRes(
                style.getMember().getNickname(),
                style.getName(),
                style.getHeartCount(),
                style.getStyleProductList().stream().map(ele -> new StyleProductRes(
                        ele.getProduct().getName(),
                        ele.getProduct().getPrice(),
                        ele.getProduct().getBrandName(),
                        ele.getProduct().getImage()
                )).toList()
        );
    }

    @Override
    @Transactional
    public void saveStyle(UUID memberId, StyleInsertReq styleInsertRes) {
        Style style = Style.builder()
                .name(styleInsertRes.name())
                .heartCount(0L)
                .member(new Member(memberId))
                .build();
        var st = styleRepository.save(style);
        styleInsertRes.productIdList().forEach(ele ->
                styleProductRepository.save(
                        StyleProduct.builder()
                                .style(st)
                                .product(Product.builder().id(ele).build())
                                .build()));
    }
    @Transactional
    @Override
    public void DeleteStyle(UUID memberId, List<Long> styleIdList) {
        styleRepository.deleteMyStyle(memberId, styleIdList);
    }
}
