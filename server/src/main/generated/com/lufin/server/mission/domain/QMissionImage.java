package com.lufin.server.mission.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMissionImage is a Querydsl query type for MissionImage
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMissionImage extends EntityPathBase<MissionImage> {

    private static final long serialVersionUID = 545849861L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMissionImage missionImage = new QMissionImage("missionImage");

    public final StringPath bucketName = createString("bucketName");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QMission mission;

    public final NumberPath<Integer> missionImageId = createNumber("missionImageId", Integer.class);

    public final StringPath objectKey = createString("objectKey");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QMissionImage(String variable) {
        this(MissionImage.class, forVariable(variable), INITS);
    }

    public QMissionImage(Path<? extends MissionImage> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMissionImage(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMissionImage(PathMetadata metadata, PathInits inits) {
        this(MissionImage.class, metadata, inits);
    }

    public QMissionImage(Class<? extends MissionImage> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.mission = inits.isInitialized("mission") ? new QMission(forProperty("mission"), inits.get("mission")) : null;
    }

}

