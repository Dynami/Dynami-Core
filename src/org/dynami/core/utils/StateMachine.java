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
package org.dynami.core.utils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;

public class StateMachine {
	private final List<ChangeStateListener> listeners = new CopyOnWriteArrayList<>();
	private IState current = null;
	
	public StateMachine(Supplier<IState> config){
		current = config.get();
	}
	
	public boolean changeState(final IState newState){
		if(current.canMoveTo(newState)){
			final IState oldState = current;
			current = newState;
			for(ChangeStateListener l:listeners){
				l.changed(oldState, newState);
			}
			//System.out.println("StateMachine.changeState("+newState.toString()+" : "+newState.ordinal()+")");
			return true;
		}
		return false;
	}
	
	public void addListener(ChangeStateListener listener){
		listeners.add(listener);
	}
	
	public IState getCurrentState(){
		return current;
	}
	
	
	public boolean canChangeState(final IState newState){
		return current.canMoveTo(newState);
	}
	
	@FunctionalInterface
	public static interface ChangeStateListener {
		public void changed(IState oldState, IState newState);
	}
	
	public static interface IState {
		//public final List<IState> children = new ArrayList<>();
		
		public default void addChildren(IState... nodes){
			for(IState n:nodes){
				children().add(n);
			}
		};

		public default boolean canMoveTo(IState newState){
			for(IState i : children()){
				if(i.equals( newState ))
					return true;
			}
			return false;
		};

		public default boolean equals(IState node){
			return ordinal() == node.ordinal();
		};
		

		public default boolean in(IState... nodes){
			for(IState n:nodes){
				if(equals(n))
					return true;
			}
			return false;
		};
		
		public abstract List<IState> children();
		
		public abstract int ordinal();
	}
}
