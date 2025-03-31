package com.lufin.server.item.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemPurchase is a Querydsl query type for ItemPurchase
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemPurchase extends EntityPathBase<ItemPurchase> {

    private static final long serialVersionUID = 1143816309L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemPurchase itemPurchase = new QItemPurchase("itemPurchase");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QItem item;

    public final NumberPath<Integer> itemCount = createNumber("itemCount", Integer.class);

    public final com.lufin.server.member.domain.QMember member;

    public final NumberPath<Integer> purchasePrice = createNumber("purchasePrice", Integer.class);

    public final EnumPath<ItemPurchaseStatus> status = createEnum("status", ItemPurchaseStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QItemPurchase(String variable) {
        this(ItemPurchase.class, forVariable(variable), INITS);
    }

    public QItemPurchase(Path<? extends ItemPurchase> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemPurchase(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemPurchase(PathMetadata metadata, PathInits inits) {
        this(ItemPurchase.class, metadata, inits);
    }

    public QItemPurchase(Class<? extends ItemPurchase> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item"), inits.get("item")) : null;
        this.member = inits.isInitialized("member") ? new com.lufin.server.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

