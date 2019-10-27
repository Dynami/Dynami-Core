package org.dynami.core.helpers;

import org.dynami.core.IDynami;

public class OrdersHelper {
	
	public static void closeAll(IDynami dynami, String note){
		dynami.portfolio().getOpenPositions().forEach(op->{
			dynami.orders().marketOrder(op.asset.symbol, -op.quantity, note);
		});
		dynami.orders().removePendings();
	}
}
