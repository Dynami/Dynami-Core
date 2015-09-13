package org.dynami.core.orders.cond;

import org.dynami.core.assets.Book.Orders;
import org.dynami.core.orders.OrderRequest.IOrderCondition;

public class StopLoss implements IOrderCondition {
	public final long stopLossPrice;
	
	public StopLoss(long stopLossPrice) {
		this.stopLossPrice = stopLossPrice;
	}
	
	@Override
	public boolean check(long quantity, Orders bid, Orders ask) {
		return quantity>0? stopLossPrice >= bid.price : stopLossPrice <= ask.price;
	}
	
	@Override
	public String toString() {
		return "Stop @ "+stopLossPrice;
	}
	
}
