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
import java.util.List;

import org.dynami.core.config.Config;

public class StageDescriptor {
	private String className;
	private String name;
	private String description;
	private Config settings;
	
	private List<PermissionDescriptor> permissions = new ArrayList<>();

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

	public List<PermissionDescriptor> getPermissions() {
		return permissions;
	}
	public void setPermissions(List<PermissionDescriptor> permissions) {
		this.permissions = permissions;
	}
	
	public Config getSettings() {
		return settings;
	}
	
	public void setSettings(Config settings) {
		this.settings = settings;
	}
	
	@Override
	public int hashCode() {
		String str = name+"."+className;
		int h = 0;
		for (int i = 0; i < str.length(); i++)
			h = (h * 31) + str.charAt(i);
		return h;
	}
}
