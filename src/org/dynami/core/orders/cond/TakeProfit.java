package org.dynami.core.orders.cond;

import org.dynami.core.assets.Book.Orders;
import org.dynami.core.orders.OrderRequest.IOrderCondition;

public class TakeProfit implements IOrderCondition {
	public final double takeProfit;
	
	public TakeProfit(double takeProfit){
		this.takeProfit = takeProfit;
	}
	
	@Override
	public boolean check(long quantity, Orders bid, Orders ask) {
		return quantity>0? takeProfit <= bid.price : takeProfit >= ask.price;
	}
	
	@Override
	public String toString() {
		return "TakeProfit @ "+takeProfit;
	}
}
