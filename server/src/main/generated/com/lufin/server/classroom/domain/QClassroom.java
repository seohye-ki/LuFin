package com.lufin.server.classroom.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QClassroom is a Querydsl query type for Classroom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QClassroom extends EntityPathBase<Classroom> {

    private static final long serialVersionUID = 1401673942L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QClassroom classroom = new QClassroom("classroom");

    public final NumberPath<Integer> classGroup = createNumber("classGroup", Integer.class);

    public final StringPath code = createString("code");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> grade = createNumber("grade", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<MemberClassroom, QMemberClassroom> memberClassrooms = this.<MemberClassroom, QMemberClassroom>createList("memberClassrooms", MemberClassroom.class, QMemberClassroom.class, PathInits.DIRECT2);

    public final NumberPath<Integer> memberCount = createNumber("memberCount", Integer.class);

    public final ListPath<com.lufin.server.mission.domain.Mission, com.lufin.server.mission.domain.QMission> missions = this.<com.lufin.server.mission.domain.Mission, com.lufin.server.mission.domain.QMission>createList("missions", com.lufin.server.mission.domain.Mission.class, com.lufin.server.mission.domain.QMission.class, PathInits.DIRECT2);

    public final StringPath name = createString("name");

    public final StringPath school = createString("school");

    public final com.lufin.server.member.domain.QMember teacher;

    public final StringPath thumbnailKey = createString("thumbnailKey");

    public QClassroom(String variable) {
        this(Classroom.class, forVariable(variable), INITS);
    }

    public QClassroom(Path<? extends Classroom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QClassroom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QClassroom(PathMetadata metadata, PathInits inits) {
        this(Classroom.class, metadata, inits);
    }

    public QClassroom(Class<? extends Classroom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.teacher = inits.isInitialized("teacher") ? new com.lufin.server.member.domain.QMember(forProperty("teacher"), inits.get("teacher")) : null;
    }

}

