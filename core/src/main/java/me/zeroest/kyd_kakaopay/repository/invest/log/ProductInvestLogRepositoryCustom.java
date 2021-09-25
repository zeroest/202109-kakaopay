package me.zeroest.kyd_kakaopay.repository.invest.log;

public interface ProductInvestLogRepositoryCustom {

    long lastAccrueUserInvest(String userId, long productId);

}
