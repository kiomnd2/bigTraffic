package kr.kiomn2.bigtraffic.domain.finance.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBankAccount is a Querydsl query type for BankAccount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBankAccount extends EntityPathBase<BankAccount> {

    private static final long serialVersionUID = 61862362L;

    public static final QBankAccount bankAccount = new QBankAccount("bankAccount");

    public final StringPath accountName = createString("accountName");

    public final StringPath accountNumber = createString("accountNumber");

    public final EnumPath<kr.kiomn2.bigtraffic.domain.finance.vo.AccountType> accountType = createEnum("accountType", kr.kiomn2.bigtraffic.domain.finance.vo.AccountType.class);

    public final NumberPath<java.math.BigDecimal> balance = createNumber("balance", java.math.BigDecimal.class);

    public final StringPath bankName = createString("bankName");

    public final StringPath color = createString("color");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final BooleanPath isDefault = createBoolean("isDefault");

    public final StringPath lastFourDigits = createString("lastFourDigits");

    public final StringPath memo = createString("memo");

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QBankAccount(String variable) {
        super(BankAccount.class, forVariable(variable));
    }

    public QBankAccount(Path<? extends BankAccount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBankAccount(PathMetadata metadata) {
        super(BankAccount.class, metadata);
    }

}

