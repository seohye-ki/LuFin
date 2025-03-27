package com.lufin.server.mission.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMission is a Querydsl query type for Mission
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMission extends EntityPathBase<Mission> {

    private static final long serialVersionUID = 1581441270L;

    public static final QMission mission = new QMission("mission");

    public final NumberPath<Integer> classId = createNumber("classId", Integer.class);

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> currentParticipants = createNumber("currentParticipants", Integer.class);

    public final NumberPath<Integer> difficulty = createNumber("difficulty", Integer.class);

    public final StringPath image = createString("image");

    public final NumberPath<Integer> maxParticipants = createNumber("maxParticipants", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> missionDate = createDateTime("missionDate", java.time.LocalDateTime.class);

    public final NumberPath<Integer> missionId = createNumber("missionId", Integer.class);

    public final ListPath<MissionParticipation, QMissionParticipation> participations = this.<MissionParticipation, QMissionParticipation>createList("participations", MissionParticipation.class, QMissionParticipation.class, PathInits.DIRECT2);

    public final EnumPath<MissionStatus> status = createEnum("status", MissionStatus.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> wage = createNumber("wage", Integer.class);

    public QMission(String variable) {
        super(Mission.class, forVariable(variable));
    }

    public QMission(Path<? extends Mission> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMission(PathMetadata metadata) {
        super(Mission.class, metadata);
    }

}

