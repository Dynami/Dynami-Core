/*
 * Copyright 2015 Alessandro Atria - a.atria@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dynami.core.services;

import java.util.List;

import org.dynami.core.IDynami;
import org.dynami.core.orders.OrderRequest;

public interface IOrderService {
	public static final String ID = "IOrderService";

	public static enum Status {
		Pending, Executed, Rejected, PartiallyExecuted, Cancelled;
	};

	/**
	 * Sends an order request to the market and sets specific behavior during order request life cycle.
	 * @param order order request
	 * @param handler order request handler
	 * @return order request unique identifier
	 * @see IOrderHandler
	 */
	//long send(OrderRequest order, IOrderHandler handler);

	/**
	 * Sends order request
	 * @param order
	 * @return order request unique identifier
	 */
	//long send(OrderRequest order);

	/**
	 * Sends a limit order price
	 * @param symbol
	 * @param price
	 * @param quantity
	 * @param note
	 * @param handler
	 * @return order request id
	 */
	public long limitOrder(String symbol, double price, long quantity, String note, IOrderHandler handler);

	/**
	 * Sends a limit order price
	 * @param symbol
	 * @param price
	 * @param quantity
	 * @param note
	 * @return order request id
	 */
	public long limitOrder(String symbol, double price, long quantity, String note);

	/**
	 * Sends a limit order price
	 * @param symbol
	 * @param price
	 * @param quantity
	 * @return order request id
	 */
	public long limitOrder(String symbol, double price, long quantity);

	/**
	 * Sends a market order
	 * @param symbol
	 * @param quantity
	 * @return order request unique identifier
	 */
	public long marketOrder(String symbol, long quantity);

	/**
	 * Sends a market order
	 * @param symbol
	 * @param quantity
	 * @param note
	 * @return order request unique identifier
	 */
	public long marketOrder(String symbol, long quantity, String note);

	/**
	 * Sends a market order
	 * @param symbol
	 * @param quantity
	 * @param note
	 * @param handler
	 * @return order request unique identifier
	 */
	public long marketOrder(String symbol, long quantity, String note, IOrderHandler handler);

	/**
	 * Removes every pending orders and pending conditions such as stop loss or take profit
	 */
	public void removePendings();

	/**
	 * Retrieves a specified order request, or null if not founded
	 * @param id
	 * @return requested order request
	 */
	public OrderRequest getOrderById(int id);

	/**
	 * Retrieves the order request status
	 * @param id
	 * @return
	 */
	public Status getOrderStatus(int id);

	/**
	 * Sends a cancel signal for a previous order request
	 * @param id order request unique identifier
	 * @return true if the order request can be removed, false otherwise
	 */
	public boolean cancelOrder(int id);

	/**
	 * Retrieve the list of pending order requests, whether there are not pending order request, the method retrieves an empty list.
	 * @return
	 */
	public List<OrderRequest> getPendingOrders();

	/**
	 * Retrieves true if there are pending order requests, false otherwise
	 * @return
	 */
	public boolean thereArePendingOrders();

	/**
	 * Retrieves true if there are pending order requests for a specific instrument, false otherwise
	 * @param symbol
	 * @return
	 */
	public boolean thereArePendingOrders(String symbol);

	/**
	 * L'interfaccia definisce i metodi che la callback di un ordine eseguito in modalità asincrona
	 * deve implementare.
	 * <pre>
	 * Se si vuole definire un comportamento particolare all'esecuzione dell'ordine (ad esempio in caso di esecuzione dell'ordine
	 * invia una mail, piuttosto che attiva l'esecuzione di un secondo ordine, ecc.
	 * </pre>
	 * @author Atria
	 * @since 1.0.0.beta
	 */
	public static interface IOrderHandler {

		/**
		 * It's invoked when the order request is successfully executed
		 * @param dynami
		 * @param order
		 */
		public default void onOrderExecuted(final IDynami dynami, final OrderRequest order){
			System.out.println(order);
		};

		/**
		 * It's invoked when the order request is only partially successfully executed
		 * @param dynami
		 * @param order
		 */
		public default void onOrderPartiallyExecuted(final IDynami dynami, final OrderRequest order){};


		/**
		 * It's invoked when the order request is cancelled by the user
		 * @param dynami
		 * @param order
		 */
		public default void onOrderCancelled(final IDynami dynami, final OrderRequest order){};

		/**
		 * It's invoked when the order request is rejected
		 * @param dynami
		 * @param order
		 */
		public default void onOrderRejected(final IDynami dynami, final OrderRequest order){};

		/**
		 * It's invoked when the order request encounter some kind of errors
		 * @param dynami
		 * @param order
		 * @param error
		 */
		public default void onError(final IDynami dynami, final OrderRequest order, final Throwable error){};
	}
}
