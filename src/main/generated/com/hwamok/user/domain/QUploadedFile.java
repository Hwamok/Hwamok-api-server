package com.hwamok.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUploadedFile is a Querydsl query type for UploadedFile
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUploadedFile extends BeanPath<UploadedFile> {

    private static final long serialVersionUID = 1812162413L;

    public static final QUploadedFile uploadedFile = new QUploadedFile("uploadedFile");

    public final StringPath originalFileName = createString("originalFileName");

    public final StringPath savedFileName = createString("savedFileName");

    public QUploadedFile(String variable) {
        super(UploadedFile.class, forVariable(variable));
    }

    public QUploadedFile(Path<? extends UploadedFile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUploadedFile(PathMetadata metadata) {
        super(UploadedFile.class, metadata);
    }

}

