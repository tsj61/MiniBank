package org.example.account;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AccountProperties {

    private final int defaultAmount;
    private final double transferCommission;

    public AccountProperties(
            @Value("${account.default-amount}") int defaultAmount,
            @Value("${account.transfer-commission}") double transferCommission
    ) {
        checkProperties(defaultAmount, transferCommission);
        this.defaultAmount = defaultAmount;
        this.transferCommission = transferCommission;
    }

    public int getDefaultAmount() {
        return defaultAmount;
    }

    public double getTransferCommission() {
        return transferCommission;
    }

    @Override
    public String toString() {
        return "AccountProperties{" +
                "defaultAmount=" + defaultAmount +
                ", transferCommission=" + transferCommission +
                '}';
    }

    private void checkProperties(int defaultAmount, double transferCommission) {
        if (defaultAmount < 0)
            throw new IllegalArgumentException("account.default-amount must be >= 0");
        if (!Double.isFinite(
                transferCommission) || transferCommission < 0 || transferCommission > 1)
            throw new IllegalArgumentException("account.transfer-commission must be in [0, 1]");
    }
}
