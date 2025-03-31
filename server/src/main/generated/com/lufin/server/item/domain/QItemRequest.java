package com.lufin.server.item.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemRequest is a Querydsl query type for ItemRequest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemRequest extends EntityPathBase<ItemRequest> {

    private static final long serialVersionUID = 245069979L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemRequest itemRequest = new QItemRequest("itemRequest");

    public final com.lufin.server.member.domain.QMember approvedBy;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QItemPurchase purchase;

    public final com.lufin.server.member.domain.QMember requester;

    public final EnumPath<ItemRequestStatus> status = createEnum("status", ItemRequestStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QItemRequest(String variable) {
        this(ItemRequest.class, forVariable(variable), INITS);
    }

    public QItemRequest(Path<? extends ItemRequest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemRequest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemRequest(PathMetadata metadata, PathInits inits) {
        this(ItemRequest.class, metadata, inits);
    }

    public QItemRequest(Class<? extends ItemRequest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.approvedBy = inits.isInitialized("approvedBy") ? new com.lufin.server.member.domain.QMember(forProperty("approvedBy"), inits.get("approvedBy")) : null;
        this.purchase = inits.isInitialized("purchase") ? new QItemPurchase(forProperty("purchase"), inits.get("purchase")) : null;
        this.requester = inits.isInitialized("requester") ? new com.lufin.server.member.domain.QMember(forProperty("requester"), inits.get("requester")) : null;
    }

}

