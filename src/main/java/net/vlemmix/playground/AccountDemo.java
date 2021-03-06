package net.vlemmix.playground;

import net.vlemmix.neomantis.utils.BitfinexAccount;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.bitfinex.v1.dto.account.BitfinexMarginInfosResponse;
import org.knowm.xchange.bitfinex.v1.service.BitfinexAccountServiceRaw;
import org.knowm.xchange.currency.Currency;
import org.knowm.xchange.dto.account.FundingRecord;
import org.knowm.xchange.service.account.AccountService;
import org.knowm.xchange.service.trade.params.TradeHistoryParamCurrency;
import org.knowm.xchange.service.trade.params.TradeHistoryParams;
import org.knowm.xchange.service.trade.params.TradeHistoryParamsTimeSpan;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class AccountDemo {
    public static void main(String[] args) throws IOException {

        Exchange bfx = BitfinexAccount.createExchange();
        AccountService accountService = bfx.getAccountService();

        marginInfo(accountService);
        fundingHistory(accountService);
    }

    private static void marginInfo(AccountService accountService) throws IOException {
        // Get the margin information
        BitfinexAccountServiceRaw accountServiceRaw = (BitfinexAccountServiceRaw) accountService;
        BitfinexMarginInfosResponse[] marginInfos = accountServiceRaw.getBitfinexMarginInfos();
        System.out.println("Margin infos response: " + marginInfos[0]);
    }

    private static void fundingHistory(AccountService accountService) throws IOException {
        // Get the funds information
        TradeHistoryParams params = accountService.createFundingHistoryParams();
        if (params instanceof TradeHistoryParamsTimeSpan) {
            final TradeHistoryParamsTimeSpan timeSpanParam = (TradeHistoryParamsTimeSpan) params;
            timeSpanParam.setStartTime(new Date(System.currentTimeMillis() - (1 * 12 * 30 * 24 * 60 * 60 * 1000L)));
        }
        if (params instanceof TradeHistoryParamCurrency) {
            ((TradeHistoryParamCurrency) params).setCurrency(Currency.BTC);
        }

        List<FundingRecord> fundingRecords = accountService.getFundingHistory(params);
        AccountServiceTestUtil.printFundingHistory(fundingRecords);
    }

}
