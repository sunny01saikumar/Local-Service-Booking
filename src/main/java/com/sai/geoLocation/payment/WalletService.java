package com.sai.geoLocation.payment;

import com.sai.geoLocation.entity.Wallet;
import com.sai.geoLocation.entity.WalletTransaction;
import com.sai.geoLocation.entity.Payment;
import com.sai.geoLocation.exception.BusinessRuleException;
import com.sai.geoLocation.exception.ResourceNotFoundException;
import com.sai.geoLocation.repository.WalletRepository;
import com.sai.geoLocation.repository.WalletTransactionRepository;
import com.sai.geoLocation.repository.PaymentRepository;
import com.sai.geoLocation.service.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class WalletService extends CrudService<Wallet> {
    private final WalletRepository wallets;
    private final WalletTransactionRepository transactions;
    private final PaymentRepository payments;

    public WalletService(WalletRepository repository, WalletTransactionRepository transactions, PaymentRepository payments) {
        super(repository);
        this.wallets = repository;
        this.transactions = transactions;
        this.payments = payments;
    }

    @Transactional
    public Wallet createWallet(UUID userId) {
        if (wallets.findByUserId(userId).isPresent()) {
            throw new BusinessRuleException("Wallet already exists for user: " + userId);
        }
        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency("INR");
        return wallets.save(wallet);
    }

    public Wallet getWallet(UUID userId) {
        return wallets.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user: " + userId));
    }

    @Transactional
    public Wallet depositFunds(UUID userId, BigDecimal amount, String method, String gatewayOrderId, String gatewayPaymentId) {
        Wallet wallet = getWallet(userId);
        wallet.setBalance(wallet.getBalance().add(amount));
        wallet = wallets.save(wallet);

        // Record Deposit Transaction
        WalletTransaction tx = new WalletTransaction();
        tx.setWalletId(wallet.getId());
        tx.setUserId(userId);
        tx.setAmount(amount);
        tx.setTransactionType("CREDIT");
        tx.setBalanceAfter(wallet.getBalance());
        tx.setReferenceType("DEPOSIT");
        tx.setReferenceId(UUID.randomUUID());
        tx.setDescription("Deposit via " + method);
        transactions.save(tx);

        // Record Payment Record
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setPaymentMethod(method);
        payment.setGateway("RAZORPAY");
        payment.setGatewayOrderId(gatewayOrderId);
        payment.setGatewayPaymentId(gatewayPaymentId);
        payment.setStatus("PAID");
        payment.setPaidAt(OffsetDateTime.now());
        payments.save(payment);

        return wallet;
    }

    @Transactional
    public void releaseBookingPayment(UUID bookingId, UUID customerId, UUID providerId, BigDecimal totalAmount, double commissionRate) {
        Wallet customerWallet = getWallet(customerId);
        Wallet providerWallet = getWallet(providerId);

        if (customerWallet.getBalance().compareTo(totalAmount) < 0) {
            throw new BusinessRuleException("Insufficient customer wallet balance");
        }

        // Calculate Splits
        BigDecimal commission = totalAmount.multiply(BigDecimal.valueOf(commissionRate));
        BigDecimal providerEarnings = totalAmount.subtract(commission);

        // Debit Customer
        customerWallet.setBalance(customerWallet.getBalance().subtract(totalAmount));
        wallets.save(customerWallet);

        WalletTransaction customerTx = new WalletTransaction();
        customerTx.setWalletId(customerWallet.getId());
        customerTx.setUserId(customerId);
        customerTx.setAmount(totalAmount.negate());
        customerTx.setTransactionType("DEBIT");
        customerTx.setBalanceAfter(customerWallet.getBalance());
        customerTx.setReferenceType("BOOKING");
        customerTx.setReferenceId(bookingId);
        customerTx.setDescription("Payment released for booking: " + bookingId);
        transactions.save(customerTx);

        // Credit Provider
        providerWallet.setBalance(providerWallet.getBalance().add(providerEarnings));
        wallets.save(providerWallet);

        WalletTransaction providerTx = new WalletTransaction();
        providerTx.setWalletId(providerWallet.getId());
        providerTx.setUserId(providerId);
        providerTx.setAmount(providerEarnings);
        providerTx.setTransactionType("CREDIT");
        providerTx.setBalanceAfter(providerWallet.getBalance());
        providerTx.setReferenceType("BOOKING");
        providerTx.setReferenceId(bookingId);
        providerTx.setDescription("Earnings for booking: " + bookingId + " (Net of " + (commissionRate * 100) + "% admin commission)");
        transactions.save(providerTx);
    }

    public List<WalletTransaction> getTransactions(UUID walletId) {
        return transactions.findByWalletIdOrderByCreatedAtDesc(walletId);
    }
}
