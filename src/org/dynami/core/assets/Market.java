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
package org.dynami.core.assets;

import java.time.LocalTime;
import java.util.Locale;

import org.dynami.core.config.Config;

@Config.Settings
public class Market {
	@Config.Param
	private String name;
	@Config.Param
	private String code;
	@Config.Param
	private Locale country;
	@Config.Param
	private LocalTime openTime;
	@Config.Param
	private LocalTime closeTime;
	
	public Market(){}
	
	public Market(String name, String code, Locale country, LocalTime openTime, LocalTime closeTime){
		this.name = name;
		this.code = code;
		this.country = country;
		this.openTime = openTime;
		this.closeTime = closeTime;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public Locale getCountry() {
		return country;
	}

	public LocalTime getOpenTime() {
		return openTime;
	}

	public LocalTime getCloseTime() {
		return closeTime;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCountry(Locale country) {
		this.country = country;
	}

	public void setOpenTime(LocalTime openTime) {
		this.openTime = openTime;
	}

	public void setCloseTime(LocalTime closeTime) {
		this.closeTime = closeTime;
	}
}
