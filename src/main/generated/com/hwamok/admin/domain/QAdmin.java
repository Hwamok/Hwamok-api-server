package com.hwamok.admin.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAdmin is a Querydsl query type for Admin
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAdmin extends EntityPathBase<Admin> {

    private static final long serialVersionUID = -811537842L;

    public static final QAdmin admin = new QAdmin("admin");

    public final com.hwamok.support.QBaseEntity _super = new com.hwamok.support.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final StringPath loginId = createString("loginId");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final ListPath<com.hwamok.utils.Role, EnumPath<com.hwamok.utils.Role>> roles = this.<com.hwamok.utils.Role, EnumPath<com.hwamok.utils.Role>>createList("roles", com.hwamok.utils.Role.class, EnumPath.class, PathInits.DIRECT2);

    public final EnumPath<AdminStatus> status = createEnum("status", AdminStatus.class);

    public QAdmin(String variable) {
        super(Admin.class, forVariable(variable));
    }

    public QAdmin(Path<? extends Admin> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAdmin(PathMetadata metadata) {
        super(Admin.class, metadata);
    }

}

