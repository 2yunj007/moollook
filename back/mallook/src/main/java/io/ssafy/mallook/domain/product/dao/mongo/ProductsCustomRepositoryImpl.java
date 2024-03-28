package io.ssafy.mallook.domain.product.dao.mongo;

import io.ssafy.mallook.domain.product.dto.response.ProductsListDto;
import io.ssafy.mallook.domain.product.entity.Products;
import io.ssafy.mallook.domain.product.entity.ReviewObject;
import io.ssafy.mallook.domain.product.entity.Reviews;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.mongodb.client.model.Projections.slice;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

@Repository
public class ProductsCustomRepositoryImpl implements ProductsCustomRepository {
    @Autowired
    MongoTemplate mongoTemplate;

    private String COLLECTION_NAME = "hiver";

    @Override
    public Slice<ProductsListDto> getProductsListByCategory(ObjectId cursor, Pageable pageable, String mainCategory, String subCategory) {
        Query query = new Query().addCriteria(Criteria.where("id").lt(cursor))
                .with(pageable);

        if (mainCategory != null) {
            query.addCriteria(Criteria.where("mainCategory").is(mainCategory));
        }
        if (subCategory != null) {
            query.addCriteria(Criteria.where("subCategory").is(subCategory));
        }

        List<ProductsListDto> productsList = mongoTemplate.find(query, Products.class)
                .stream().map(ele -> new ProductsListDto(
                        ele.getId().toString(),
                        ele.getMainCategory(),
                        ele.getSubCategory(),
                        ele.getGender(),
                        ele.getName(),
                        ele.getPrice(),
                        ele.getBrandName(),
                        ele.getSize(),
                        ele.getFee(),
                        ele.getTags(),
                        ele.getDetailImages(),
                        ele.getDetailHtml(),
                        ele.getCode(),
                        ele.getUrl())).toList();

        boolean hasNext = mongoTemplate.count(query, Products.class) > ((pageable.getPageNumber() + 1) * pageable.getPageSize());
        return new SliceImpl<>(productsList, pageable, hasNext);
    }

    @Override
    public Products getProductDetailWithLimitedReviews(String id) {
        AggregationOperation matchOperation = Aggregation.match(Criteria.where("_id").is(id));
        AggregationOperation projectOperation = Aggregation.project(
                "main_category", "sub_category", "gender",
                "name", "price", "color", "size", "brand_name", "fee",
                "image", "code", "url", "tags", "detail_images", "detail_html",
                "keywords")
                .and("reviews.count").as("reviews.count")
                .and("reviews.average_point").as("reviews.average_point")
                .and("reviews.reviews").slice(5).as("reviews.reviews");
        TypedAggregation<Products> aggregation = newAggregation(Products.class, matchOperation, projectOperation);
        AggregationResults<Products> result =  mongoTemplate.aggregate(aggregation, COLLECTION_NAME, Products.class);
        return result.getUniqueMappedResult();
    }

    @Override
    public Page<ReviewObject> getReviews(String id, Pageable pageable) {
        AggregationOperation matchOperation = Aggregation.match(Criteria.where("_id").is(id));
        AggregationOperation sliceOperation = Aggregation.project()
                .and("reviews.count").as("reviews.count")
                .and("reviews.average_point").as("reviews.average_point")
                .and("reviews.reviews").slice(pageable.getPageSize(),(int) (pageable.getOffset())).as("reviews.reviews");
        TypedAggregation<Products> aggregation = Aggregation.newAggregation(Products.class, matchOperation, sliceOperation);
        AggregationResults<Products> result = mongoTemplate.aggregate(aggregation, COLLECTION_NAME, Products.class);
        Reviews reviews = Objects.requireNonNull(result.getUniqueMappedResult()).getReviews();
        return new PageImpl<>(reviews.getReviews(), pageable, reviews.getCount());
    }
}
