package com.lufin.server.loan.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLoanProduct is a Querydsl query type for LoanProduct
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoanProduct extends EntityPathBase<LoanProduct> {

    private static final long serialVersionUID = -1105277567L;

    public static final QLoanProduct loanProduct = new QLoanProduct("loanProduct");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> creditRank = createNumber("creditRank", Integer.class);

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<java.math.BigDecimal> interestRate = createNumber("interestRate", java.math.BigDecimal.class);

    public final NumberPath<Integer> maxAmount = createNumber("maxAmount", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> period = createNumber("period", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QLoanProduct(String variable) {
        super(LoanProduct.class, forVariable(variable));
    }

    public QLoanProduct(Path<? extends LoanProduct> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLoanProduct(PathMetadata metadata) {
        super(LoanProduct.class, metadata);
    }

}

