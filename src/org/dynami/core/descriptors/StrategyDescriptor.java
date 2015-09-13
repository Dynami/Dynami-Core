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
package org.dynami.core.descriptors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StrategyDescriptor implements Comparable<StrategyDescriptor> {
	public static final String FILE_NAME = "strategy.mpe";
	private String className;
	private String name;
	private String description;
	private String owner;
	private String vendor;
	private int version;
	private Date released;
	private final List<StageDescriptor> stages = new ArrayList<>();
	
	public List<StageDescriptor> getStages() {
		return stages;
	}
	public void setStages(List<StageDescriptor> phases) {
		this.stages.addAll(phases);
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public Date getReleased() {
		return released;
	}
	public void setReleased(Date released) {
		this.released = released;
	}
	@Override
	public int compareTo(StrategyDescriptor o) {
		return (name.compareTo(o.name) != 0)?name.compareTo(o.name):Integer.compare(version, o.version);
	}
	
	public String getJarName(){
		return cleanCharacter(name).toLowerCase()+"_V"+version+".jar";
	}
	
	private static String cleanCharacter(final String input){
		StringBuilder builder = new StringBuilder();
		// digits [48-57]
		// letters and symbols [46, 64 - 90, 95, 97-122]
		final char[] c = input.toCharArray();
		for(int i = 0; i < c.length; i++){
			if( c[i] == 46 ||
				(c[i] >= 48 && c[i] <= 57) ||
				(c[i] >= 64 && c[i] <= 90) ||
				c[i] == 95 ||
				(c[i] >= 97 && c[i] <= 122)) {
				builder.append(c[i]);
			} else {
				builder.append('_');
			}
		}
		return builder.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + version;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StrategyDescriptor other = (StrategyDescriptor) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (version != other.version)
			return false;
		return true;
	}
}
