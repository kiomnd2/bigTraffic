package kr.kiomn2.bigtraffic.domain.finance.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCard is a Querydsl query type for Card
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCard extends EntityPathBase<Card> {

    private static final long serialVersionUID = 953885095L;

    public static final QCard card = new QCard("card");

    public final NumberPath<java.math.BigDecimal> balance = createNumber("balance", java.math.BigDecimal.class);

    public final NumberPath<Integer> billingDay = createNumber("billingDay", Integer.class);

    public final StringPath cardCompany = createString("cardCompany");

    public final StringPath cardName = createString("cardName");

    public final StringPath cardNumber = createString("cardNumber");

    public final EnumPath<kr.kiomn2.bigtraffic.domain.finance.vo.CardType> cardType = createEnum("cardType", kr.kiomn2.bigtraffic.domain.finance.vo.CardType.class);

    public final StringPath color = createString("color");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<java.math.BigDecimal> creditLimit = createNumber("creditLimit", java.math.BigDecimal.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final BooleanPath isDefault = createBoolean("isDefault");

    public final StringPath lastFourDigits = createString("lastFourDigits");

    public final StringPath memo = createString("memo");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<java.math.BigDecimal> usedAmount = createNumber("usedAmount", java.math.BigDecimal.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QCard(String variable) {
        super(Card.class, forVariable(variable));
    }

    public QCard(Path<? extends Card> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCard(PathMetadata metadata) {
        super(Card.class, metadata);
    }

}

