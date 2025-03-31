package com.lufin.server.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QMemberAuth is a Querydsl query type for MemberAuth
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QMemberAuth extends BeanPath<MemberAuth> {

    private static final long serialVersionUID = -1519543766L;

    public static final QMemberAuth memberAuth = new QMemberAuth("memberAuth");

    public final DateTimePath<java.time.LocalDateTime> lastLogin = createDateTime("lastLogin", java.time.LocalDateTime.class);

    public final StringPath password = createString("password");

    public final StringPath salt = createString("salt");

    public final StringPath secondaryPassword = createString("secondaryPassword");

    public QMemberAuth(String variable) {
        super(MemberAuth.class, forVariable(variable));
    }

    public QMemberAuth(Path<? extends MemberAuth> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMemberAuth(PathMetadata metadata) {
        super(MemberAuth.class, metadata);
    }

}

