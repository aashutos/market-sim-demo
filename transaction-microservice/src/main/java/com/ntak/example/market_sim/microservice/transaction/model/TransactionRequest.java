package com.ntak.example.market_sim.microservice.transaction.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 *    Transaction request POJO.
 *
 *    Example REST request body beanified by this object:
 *    {
 *      "clientCounterparty": "CPTY_001",
 *      "clientAccount": "ACC_EQ_CPTY_001",
 *      "instrumentType": "equity",
 *      "instrumentId": "EQUCMP",
 *      "bidLotSize": 1000,
 *      "bidPrice": 100,
 *      "bidExpiry": 10,
 *      "bidCurrency":"GBP"
 *    }
 **/
public class TransactionRequest {
    @NotNull(message = "Client Counterparty is compulsory")
    private String clientCounterparty;
    @NotNull(message = "Client Account is compulsory")
    private String clientAccount;

    @NotNull(message = "Instrument type is compulsory")
    private String instrumentType;
    @NotNull(message = "Instrument identifier is compulsory")
    private String instrumentId;

    @Min(value = 0, message = "Lot size must be positive")
    private int bidLotSize;
    @Min(value = 0, message = "Bid price must be positive")
    private int bidPrice;
    @Pattern(regexp = "\\w{3}", message = "ISO 3166-1 alpha-3 currency code must be supplied")
    private String bidCurrency;
    @Pattern(regexp = "(buy|sell|Buy|Sell)", message = "Must be [bB]uy or [sS]ell")
    private String buySell;
    @Min(value = 0, message = "Expiry must be positive")
    private long bidExpiry;

    public String getClientCounterparty() {
        return clientCounterparty;
    }

    public void setClientCounterparty(String clientCounterparty) {
        this.clientCounterparty = clientCounterparty;
    }

    public String getClientAccount() {
        return clientAccount;
    }

    public void setClientAccount(String clientAccount) {
        this.clientAccount = clientAccount;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public int getBidLotSize() {
        return bidLotSize;
    }

    public void setBidLotSize(int bidLotSize) {
        this.bidLotSize = bidLotSize;
    }

    public int getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getBidCurrency() {
        return bidCurrency;
    }

    public void setBidCurrency(String bidCurrency) {
        this.bidCurrency = bidCurrency;
    }

    public String getBuySell() {
        return buySell;
    }

    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    public long getBidExpiry() {
        return bidExpiry;
    }

    public void setBidExpiry(long bidExpiry) {
        this.bidExpiry = bidExpiry;
    }
}
