package com.lufin.server.loan.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QLoanApplication is a Querydsl query type for LoanApplication
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoanApplication extends EntityPathBase<LoanApplication> {

    private static final long serialVersionUID = -2067895070L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QLoanApplication loanApplication = new QLoanApplication("loanApplication");

    public final com.lufin.server.classroom.domain.QClassroom classroom;

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final StringPath description = createString("description");

    public final DateTimePath<java.time.LocalDateTime> dueDate = createDateTime("dueDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> interestAmount = createNumber("interestAmount", Integer.class);

    public final QLoanProduct loanProduct;

    public final com.lufin.server.member.domain.QMember member;

    public final DateTimePath<java.time.LocalDateTime> nextPaymentDate = createDateTime("nextPaymentDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> overdueCount = createNumber("overdueCount", Integer.class);

    public final NumberPath<Integer> requiredAmount = createNumber("requiredAmount", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final EnumPath<LoanApplicationStatus> status = createEnum("status", LoanApplicationStatus.class);

    public QLoanApplication(String variable) {
        this(LoanApplication.class, forVariable(variable), INITS);
    }

    public QLoanApplication(Path<? extends LoanApplication> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QLoanApplication(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QLoanApplication(PathMetadata metadata, PathInits inits) {
        this(LoanApplication.class, metadata, inits);
    }

    public QLoanApplication(Class<? extends LoanApplication> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.classroom = inits.isInitialized("classroom") ? new com.lufin.server.classroom.domain.QClassroom(forProperty("classroom"), inits.get("classroom")) : null;
        this.loanProduct = inits.isInitialized("loanProduct") ? new QLoanProduct(forProperty("loanProduct")) : null;
        this.member = inits.isInitialized("member") ? new com.lufin.server.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

