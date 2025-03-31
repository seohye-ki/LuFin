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

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMission mission = new QMission("mission");

    public final NumberPath<Integer> classId = createNumber("classId", Integer.class);

    public final com.lufin.server.classroom.domain.QClassroom classroom;

    public final StringPath content = createString("content");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> currentParticipants = createNumber("currentParticipants", Integer.class);

    public final NumberPath<Integer> difficulty = createNumber("difficulty", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final ListPath<MissionImage, QMissionImage> images = this.<MissionImage, QMissionImage>createList("images", MissionImage.class, QMissionImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> maxParticipants = createNumber("maxParticipants", Integer.class);

    public final DateTimePath<java.time.LocalDateTime> missionDate = createDateTime("missionDate", java.time.LocalDateTime.class);

    public final ListPath<MissionParticipation, QMissionParticipation> participations = this.<MissionParticipation, QMissionParticipation>createList("participations", MissionParticipation.class, QMissionParticipation.class, PathInits.DIRECT2);

    public final EnumPath<MissionStatus> status = createEnum("status", MissionStatus.class);

    public final StringPath title = createString("title");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> wage = createNumber("wage", Integer.class);

    public QMission(String variable) {
        this(Mission.class, forVariable(variable), INITS);
    }

    public QMission(Path<? extends Mission> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMission(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMission(PathMetadata metadata, PathInits inits) {
        this(Mission.class, metadata, inits);
    }

    public QMission(Class<? extends Mission> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.classroom = inits.isInitialized("classroom") ? new com.lufin.server.classroom.domain.QClassroom(forProperty("classroom"), inits.get("classroom")) : null;
    }

}

