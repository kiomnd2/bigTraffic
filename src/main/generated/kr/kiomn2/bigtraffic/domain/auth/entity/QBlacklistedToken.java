package kr.kiomn2.bigtraffic.domain.auth.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBlacklistedToken is a Querydsl query type for BlacklistedToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlacklistedToken extends EntityPathBase<BlacklistedToken> {

    private static final long serialVersionUID = 1994998274L;

    public static final QBlacklistedToken blacklistedToken = new QBlacklistedToken("blacklistedToken");

    public final DateTimePath<java.time.LocalDateTime> blacklistedAt = createDateTime("blacklistedAt", java.time.LocalDateTime.class);

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> expirationDate = createDateTime("expirationDate", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath reason = createString("reason");

    public final StringPath token = createString("token");

    public QBlacklistedToken(String variable) {
        super(BlacklistedToken.class, forVariable(variable));
    }

    public QBlacklistedToken(Path<? extends BlacklistedToken> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBlacklistedToken(PathMetadata metadata) {
        super(BlacklistedToken.class, metadata);
    }

}

