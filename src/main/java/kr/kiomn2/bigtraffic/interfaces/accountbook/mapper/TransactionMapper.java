package kr.kiomn2.bigtraffic.interfaces.accountbook.mapper;

import kr.kiomn2.bigtraffic.domain.accountbook.entity.Transaction;
import kr.kiomn2.bigtraffic.domain.accountbook.vo.TransactionType;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.DailyTransactionSummary;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.MonthlyCalendarResponse;
import kr.kiomn2.bigtraffic.interfaces.accountbook.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.from(transaction);
    }

    public List<TransactionResponse> toResponseList(List<Transaction> transactions) {
        return transactions.stream()
                .map(TransactionResponse::from)
                .toList();
    }

    public Page<TransactionResponse> toResponsePage(Page<Transaction> transactionPage) {
        return transactionPage.map(TransactionResponse::from);
    }

    public MonthlyCalendarResponse toMonthlyCalendarResponse(
            int year, int month, Map<LocalDate, List<Transaction>> groupedByDate) {

        List<DailyTransactionSummary> dailySummaries = groupedByDate.entrySet().stream()
                .map(entry -> {
                    LocalDate date = entry.getKey();
                    List<Transaction> dayTransactions = entry.getValue();

                    BigDecimal income = dayTransactions.stream()
                            .filter(t -> t.getType() == TransactionType.INCOME)
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal expense = dayTransactions.stream()
                            .filter(t -> t.getType() == TransactionType.EXPENSE)
                            .map(Transaction::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return DailyTransactionSummary.builder()
                            .date(date)
                            .transactionCount(dayTransactions.size())
                            .dailyIncome(income)
                            .dailyExpense(expense)
                            .dailyNet(income.subtract(expense))
                            .build();
                })
                .sorted(Comparator.comparing(DailyTransactionSummary::getDate))
                .toList();

        BigDecimal monthlyIncome = dailySummaries.stream()
                .map(DailyTransactionSummary::getDailyIncome)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal monthlyExpense = dailySummaries.stream()
                .map(DailyTransactionSummary::getDailyExpense)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return MonthlyCalendarResponse.builder()
                .year(year)
                .month(month)
                .dailySummaries(dailySummaries)
                .monthlyIncome(monthlyIncome)
                .monthlyExpense(monthlyExpense)
                .monthlyNet(monthlyIncome.subtract(monthlyExpense))
                .build();
    }
}
