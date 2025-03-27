package com.lufin.server.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberStatus is a Querydsl query type for MemberStatus
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMemberStatus extends BeanPath<MemberStatus> {

    private static final long serialVersionUID = 521171956L;

    public static final QMemberStatus memberStatus = new QMemberStatus("memberStatus");

    public final NumberPath<Integer> creditRating = createNumber("creditRating", Integer.class);

    public final NumberPath<Byte> status = createNumber("status", Byte.class);

    public final StringPath statusDescription = createString("statusDescription");

    public QMemberStatus(String variable) {
        super(MemberStatus.class, forVariable(variable));
    }

    public QMemberStatus(Path<? extends MemberStatus> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberStatus(PathMetadata metadata) {
        super(MemberStatus.class, metadata);
    }

}

