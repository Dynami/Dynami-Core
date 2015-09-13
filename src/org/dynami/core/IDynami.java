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

import org.dynami.core.services.IDataService;
import org.dynami.core.services.IAssetService;
import org.dynami.core.services.IOrderService;
import org.dynami.core.services.IPortfolioService;
import org.dynami.core.services.ITraceService;

/**
 * IDaynami is the End-user interface, which allows strategy developer to interact programmatically with the platform, accessing to user services such as:
 * <ul>
 * 	<li>{@link IOrderService}</li>
 *  <li>{@link IDataService}</li>
 * 	<li>{@link ITraceService}</li>
 *  <li>{@link IPortfolioService}</li>
 *  <li>{@link IAssetService}</li>
 *  <li>...</li>
 * <ul/>
 * IDynami is used as parameter to {@link IStrategy} and {@link IStage}.
 * @author Atria
 */
public interface IDynami {
	
	public void gotoNextStage(final IStage nextStage) throws Exception;
	
	public void gotoNextStageNow(final IStage nextStage) throws Exception;
	
	/**
	 * After processing iteration strategy execution is stopped, even if there are other scheduled stages.
	 */
	public void gotoEnd();
	
	
	public IOrderService orders();
	
	public IDataService data();
	
	public IPortfolioService portfolio();
	
	public ITraceService trace();
	
	public IAssetService assets();
}
