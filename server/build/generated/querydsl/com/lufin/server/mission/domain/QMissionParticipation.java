package com.lufin.server.mission.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMissionParticipation is a Querydsl query type for MissionParticipation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissionParticipation extends EntityPathBase<MissionParticipation> {

    private static final long serialVersionUID = 1579785195L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMissionParticipation missionParticipation = new QMissionParticipation("missionParticipation");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> memberId = createNumber("memberId", Integer.class);

    public final QMission mission;

    public final NumberPath<Integer> participationId = createNumber("participationId", Integer.class);

    public final NumberPath<Integer> rejectCount = createNumber("rejectCount", Integer.class);

    public final EnumPath<MissionParticipationStatus> status = createEnum("status", MissionParticipationStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final BooleanPath wageStatus = createBoolean("wageStatus");

    public QMissionParticipation(String variable) {
        this(MissionParticipation.class, forVariable(variable), INITS);
    }

    public QMissionParticipation(Path<? extends MissionParticipation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMissionParticipation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMissionParticipation(PathMetadata metadata, PathInits inits) {
        this(MissionParticipation.class, metadata, inits);
    }

    public QMissionParticipation(Class<? extends MissionParticipation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mission = inits.isInitialized("mission") ? new QMission(forProperty("mission")) : null;
    }

}

