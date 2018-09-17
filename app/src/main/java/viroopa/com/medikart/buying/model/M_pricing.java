package viroopa.com.medikart.buying.model;

/**
 * Created by prakash on 18/08/15.
 */
public class M_pricing {

    String PromoCode     ,
           PercentageDiscount    ,
           FixedDiscount    ,
           ExpiryDate     ,
           MinimunPurchase    ,
           MaxDiscount    ,
           FixedPercent     ,
           Amount    ,
           Discription;

    public String getPromoCode() {
        return PromoCode;
    }

    public void setPromoCode(String promoCode) {
        PromoCode = promoCode;
    }

    public String getPercentageDiscount() {
        return PercentageDiscount;
    }

    public void setPercentageDiscount(String percentageDiscount) {
        PercentageDiscount = percentageDiscount;
    }

    public String getFixedDiscount() {
        return FixedDiscount;
    }

    public void setFixedDiscount(String fixedDiscount) {
        FixedDiscount = fixedDiscount;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public String getMinimunPurchase() {
        return MinimunPurchase;
    }

    public void setMinimunPurchase(String minimunPurchase) {
        MinimunPurchase = minimunPurchase;
    }

    public String getMaxDiscount() {
        return MaxDiscount;
    }

    public void setMaxDiscount(String maxDiscount) {
        MaxDiscount = maxDiscount;
    }

    public String getFixedPercent() {
        return FixedPercent;
    }

    public void setFixedPercent(String fixedPercent) {
        FixedPercent = fixedPercent;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }

    public M_pricing() {
    }
}
