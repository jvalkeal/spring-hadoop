/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.hadoop.security;

/**
 * This class keeps together all hadoop security related settings.
 *
 * @author Janne Valkealahti
 *
 */
public class HadoopSecurity {

	private SecurityAuthMethod securityAuthMethod;
	private String userPrincipal;
	private String userKeytab;
	private String yarnPrincipal;
	private String hdfsPrincipal;

	public HadoopSecurity() {
	}

	public HadoopSecurity(SecurityAuthMethod securityAuthMethod, String userPrincipal, String userKeytab) {
		super();
		this.securityAuthMethod = securityAuthMethod;
		this.userPrincipal = userPrincipal;
		this.userKeytab = userKeytab;
	}

	public SecurityAuthMethod getSecurityAuthMethod() {
		return securityAuthMethod;
	}

	public void setSecurityAuthMethod(SecurityAuthMethod securityAuthMethod) {
		this.securityAuthMethod = securityAuthMethod;
	}

	public String getUserPrincipal() {
		return userPrincipal;
	}

	public void setUserPrincipal(String userPrincipal) {
		this.userPrincipal = userPrincipal;
	}

	public String getUserKeytab() {
		return userKeytab;
	}

	public void setUserKeytab(String userKeytab) {
		this.userKeytab = userKeytab;
	}

	public String getYarnPrincipal() {
		return yarnPrincipal;
	}

	public void setYarnPrincipal(String yarnPrincipal) {
		this.yarnPrincipal = yarnPrincipal;
	}

	public String getHdfsPrincipal() {
		return hdfsPrincipal;
	}

	public void setHdfsPrincipal(String hdfsPrincipal) {
		this.hdfsPrincipal = hdfsPrincipal;
	}

}
