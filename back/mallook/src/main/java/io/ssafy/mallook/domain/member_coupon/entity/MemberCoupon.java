package io.ssafy.mallook.domain.member_coupon.entity;

import io.ssafy.mallook.domain.BaseEntity;
import io.ssafy.mallook.domain.coupon.entity.Coupon;
import io.ssafy.mallook.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "member_coupon")
@SQLRestriction("status=TRUE")
public class MemberCoupon extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    Member member;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    Coupon coupon;
}
