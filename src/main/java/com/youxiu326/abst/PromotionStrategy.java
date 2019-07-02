package com.youxiu326.abst;

import com.youxiu326.entity.Product;
import com.youxiu326.entity.Promotion;
import com.youxiu326.entity.PromotionResult;

import java.math.BigDecimal;
import java.util.List;

/**
 * 促销抽象类
 * 定义公共方法，让子类继承
 * 定义抽象方法，让子类实现
 */
public abstract class PromotionStrategy {

    public abstract Promotion.Type getType();

    /**
     * 定义执行促销方法
     * @param promotion 促销
     * @param products  参加促销的商品集合
     * @return
     */
    public abstract List<PromotionResult> execute(Promotion promotion, List<Product> products);


    /*
    //加法
    BigDecimal result1 = num1.add(num2);
    //减法
    BigDecimal result2 = num1.subtract(num2);
    //乘法
    BigDecimal result3 = num1.multiply(num2);
    //除法
    BigDecimal result5 = num2.divide(num1,20,BigDecimal.ROUND_HALF_UP);
    //绝对值
    BigDecimal result4 = num3.abs();

    比较大小
    结果是:   -1：小于；   0 ：等于；   1 ：大于；
    BigDecimal b1 = new BigDecimal("-121454125453.145");
    if(b1.compareTo(BigDecimal.ZERO)==-1) {
    	System.out.println("金额为负数！");
    }

     */


    /**
     * <span color="red">平摊优惠金额</span>
     * @param products
     * @param disAmount
     */
    protected void sharedAmount(List<Product> products,BigDecimal disAmount){

        //计算总金额
        double totalAmountTemp = products.stream().mapToDouble(it->(
                it.getAmount().multiply(new BigDecimal(it.getQuantity().toString()))).doubleValue()
        ).sum();

        //总金额
        BigDecimal totalAmount = new BigDecimal(totalAmountTemp+"");
        //已分摊金额
        BigDecimal sharedAmount = new BigDecimal("0");;

        //平摊金额到明细
        for(int i=0;i<products.size();i++) {
            Product product = products.get(i);
            if(i == products.size() - 1) {
                //如果是最后一件商品 ，将剩余优惠金额计算到这个商品内
                //product.setDisAmount(formatDouble2Decimal(product.getDisAmount()+disAmount-sharedAmount));
                product.setDiscountAmount(product.getAmount().add(disAmount).subtract(sharedAmount));
            }else {
                //double itemDisAmount = disAmount*(product.getAmount()*product.getQuantity())/totalAmount;
                //该商品总数量
                BigDecimal quantity = new BigDecimal(product.getQuantity().toString());

                BigDecimal itemDisAmount = ( disAmount.multiply(product.getAmount().multiply(quantity)) )
                        .divide(totalAmount,20,BigDecimal.ROUND_HALF_UP);

                product.setDiscountAmount(product.getDiscountAmount().add(itemDisAmount));

                sharedAmount =sharedAmount.add(itemDisAmount);
            }
        }
        //平摊价格
        //products.stream().forEach(it->it.setDisPrice(formatDouble2Decimal(it.getDisPrice()-(disAmount*(it.getDisPrice()*it.getQuantity()/totalAmount)/it.getQuantity()))));
    }
} 