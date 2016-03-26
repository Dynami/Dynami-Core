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
package org.dynami.core.bus;

import java.util.Set;

public interface IMsg {
	public static final String ID = "Msg.Broker";

	public static final String FORCE_SYNC = "FORCE_SYNC";

	/**
	 * Subscribe topic
	 * @param topic
	 * @param handler
	 */
	public abstract void subscribe(String topic, Handler handler);

	/**
	 * Removes listener passed as parameter from topic
	 * @param topic
	 * @param handler
	 */
	public abstract void unsubscribe(String topic, Handler handler);

	/**
	 * Removes all listeners for a specific topic
	 * @param topic
	 */
	public abstract void unsubscribeAllFor(String topic);

	/**
	 * Remove all listeners for the specified topics
	 * @param topics
	 */
	public default void unsubscribeAllFor(String...topics){
		for(String topic:topics)
			unsubscribeAllFor(topic);
	};

	public default void unsubscribeAllTopicsStartingWith(String topic){
		final Set<String> topics = getTopics();
		for(String t:topics){
			if(t.startsWith(topic)){
				unsubscribeAllFor(t);
			}
		}
	}

	/**
	 * Removes topic from event broker, no more messages will be delivered to topic listeners
	 * @param topic
	 */
	public abstract void removeTopic(String topic);

	/**
	 * Returns topics' name
	 * @return
	 */
	public abstract Set<String> getTopics();

	/**
	 * Sends asynchronously a message to subscribers
	 * @param topic
	 * @param msg object passed with message
	 * @return true if topic exists, false otherwise
	 */
	public abstract boolean async(String topic, Object msg);

	//public abstract boolean async(String topic, Object msg, Runnable callback);

	/**
	 * Sends synchronous messages to topic listeners
	 * @param topic
	 * @param msg
	 * @return true if topic exists, false otherwise
	 */
	public abstract boolean sync(String topic, Object msg);

	/**
	 * Forces sync messages even if async methods are used
	 * @param forceSync
	 */
	public abstract void forceSync(boolean forceSync);

	public boolean dispose();

	public default void reset(){};

	@FunctionalInterface
	public static interface Handler {
		/**
		 * Handles messages published on topic.
		 * Refers to {@link MsgTopics} for defined topics and class type message
		 * @param last indicates if the message is the last arrived for the topic
		 * @param msg message object
		 */
		public void update(boolean last, Object msg);
	}

}