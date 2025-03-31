package com.lufin.server.transaction.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QTransactionHistory is a Querydsl query type for TransactionHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTransactionHistory extends EntityPathBase<TransactionHistory> {

    private static final long serialVersionUID = 1216116222L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QTransactionHistory transactionHistory = new QTransactionHistory("transactionHistory");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final NumberPath<Integer> balanceAfter = createNumber("balanceAfter", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final com.lufin.server.member.domain.QMember executor;

    public final com.lufin.server.account.domain.QAccount fromAccount;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final EnumPath<TransactionSourceType> sourceType = createEnum("sourceType", TransactionSourceType.class);

    public final EnumPath<com.lufin.server.common.constants.HistoryStatus> status = createEnum("status", com.lufin.server.common.constants.HistoryStatus.class);

    public final StringPath toAccountNumber = createString("toAccountNumber");

    public final EnumPath<TransactionType> transactionType = createEnum("transactionType", TransactionType.class);

    public QTransactionHistory(String variable) {
        this(TransactionHistory.class, forVariable(variable), INITS);
    }

    public QTransactionHistory(Path<? extends TransactionHistory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QTransactionHistory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QTransactionHistory(PathMetadata metadata, PathInits inits) {
        this(TransactionHistory.class, metadata, inits);
    }

    public QTransactionHistory(Class<? extends TransactionHistory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.executor = inits.isInitialized("executor") ? new com.lufin.server.member.domain.QMember(forProperty("executor"), inits.get("executor")) : null;
        this.fromAccount = inits.isInitialized("fromAccount") ? new com.lufin.server.account.domain.QAccount(forProperty("fromAccount"), inits.get("fromAccount")) : null;
    }

}

