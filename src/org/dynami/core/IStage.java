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
package org.dynami.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * IStage implementation is the core part of the trading strategy. 
 * IStage contains the behavior of trading system defined by user.
 * @author Atria
 * @see IStrategy
 */
public interface IStage {
	
	/**
	 * Return the user specified stage name
	 * @return
	 */
	public default String getName(){
		return getClass().getSimpleName();
	}
	
	/**
	 * 
	 * Implement this method to setup params and stage variables.
	 * The method is invoked once per stage.
	 * @param dynami
	 * @throws Exception 
	 */
	public void setup(final IDynami dynami) throws Exception;
	
	/**
	 * This method is the default event handler for all event type and all symbols.
	 * For high frequency trading use custom method as specific handler to avoid different concurrent 
	 * event or check event type as first instruction.
	 * <pre><b>Attention</b> if another method is annotated as handler, process method <b>wont be executed</b></pre>
	 * 
	 * @param dynami, you can get the current event invoking IDynami::getLastEvent() method.
	 * @see Event
	 * @see Event.Handler
	 * @see IDynami
	 */
	public void process(final IDynami dynami, Event event) throws Exception;
	
	@Target({ElementType.METHOD})
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Filter {
		Event.Type[] event() default {}; 
		String[] symbol() default {};
	}
}
