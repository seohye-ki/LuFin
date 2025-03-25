package com.lufin.server.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAutoTransfer is a Querydsl query type for AutoTransfer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAutoTransfer extends EntityPathBase<AutoTransfer> {

    private static final long serialVersionUID = -1196426319L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAutoTransfer autoTransfer = new QAutoTransfer("autoTransfer");

    public final BooleanPath active = createBoolean("active");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final EnumPath<java.time.DayOfWeek> dayOfWeek = createEnum("dayOfWeek", java.time.DayOfWeek.class);

    public final DatePath<java.time.LocalDate> endDate = createDate("endDate", java.time.LocalDate.class);

    public final QAccount from;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DatePath<java.time.LocalDate> startDate = createDate("startDate", java.time.LocalDate.class);

    public final StringPath toAccountNumber = createString("toAccountNumber");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QAutoTransfer(String variable) {
        this(AutoTransfer.class, forVariable(variable), INITS);
    }

    public QAutoTransfer(Path<? extends AutoTransfer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAutoTransfer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAutoTransfer(PathMetadata metadata, PathInits inits) {
        this(AutoTransfer.class, metadata, inits);
    }

    public QAutoTransfer(Class<? extends AutoTransfer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.from = inits.isInitialized("from") ? new QAccount(forProperty("from"), inits.get("from")) : null;
    }

}

