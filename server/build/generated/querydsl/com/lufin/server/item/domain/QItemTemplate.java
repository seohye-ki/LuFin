package com.lufin.server.item.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QItemTemplate is a Querydsl query type for ItemTemplate
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QItemTemplate extends EntityPathBase<ItemTemplate> {

    private static final long serialVersionUID = -1921054738L;

    public static final QItemTemplate itemTemplate = new QItemTemplate("itemTemplate");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public QItemTemplate(String variable) {
        super(ItemTemplate.class, forVariable(variable));
    }

    public QItemTemplate(Path<? extends ItemTemplate> path) {
        super(path.getType(), path.getMetadata());
    }

    public QItemTemplate(PathMetadata metadata) {
        super(ItemTemplate.class, metadata);
    }

}

