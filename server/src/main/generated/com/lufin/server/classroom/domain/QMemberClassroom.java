package com.lufin.server.classroom.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMemberClassroom is a Querydsl query type for MemberClassroom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMemberClassroom extends EntityPathBase<MemberClassroom> {

    private static final long serialVersionUID = 850234652L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMemberClassroom memberClassroom = new QMemberClassroom("memberClassroom");

    public final QClassroom classroom;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isCurrent = createBoolean("isCurrent");

    public final DateTimePath<java.time.LocalDateTime> joinDate = createDateTime("joinDate", java.time.LocalDateTime.class);

    public final com.lufin.server.member.domain.QMember member;

    public QMemberClassroom(String variable) {
        this(MemberClassroom.class, forVariable(variable), INITS);
    }

    public QMemberClassroom(Path<? extends MemberClassroom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMemberClassroom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMemberClassroom(PathMetadata metadata, PathInits inits) {
        this(MemberClassroom.class, metadata, inits);
    }

    public QMemberClassroom(Class<? extends MemberClassroom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.classroom = inits.isInitialized("classroom") ? new QClassroom(forProperty("classroom"), inits.get("classroom")) : null;
        this.member = inits.isInitialized("member") ? new com.lufin.server.member.domain.QMember(forProperty("member"), inits.get("member")) : null;
    }

}

