/*
 *
 *  Copyright 2015 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */
package springfox.documentation.swagger.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SecurityConfiguration {
  public static final SecurityConfiguration DEFAULT = new SecurityConfiguration();

  private String clientId;
  private String clientSecret;
  private String realm;
  private String appName;
  private String apiKey;
  private String scopeSeparator;

  private SecurityConfiguration() {
    this(null, null, null, null, null, ",");
  }

  public SecurityConfiguration(String clientId, String clientSecret, String realm, String appName, String apiKey,
                               String scopeSeparator) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.realm = realm;
    this.appName = appName;
    this.apiKey = apiKey;
    this.scopeSeparator = scopeSeparator;
  }

  @JsonProperty("clientId")
  public String getClientId() {
    return clientId;
  }

  @JsonProperty("realm")
  public String getRealm() {
    return realm;
  }

  @JsonProperty("appName")
  public String getAppName() {
    return appName;
  }

  @JsonProperty("apiKey")
  public String getApiKey() {
    return apiKey;
  }

  @JsonProperty("clientSecret")
  public String getClientSecret() {
    return clientSecret;
  }

  @JsonProperty("scopeSeparator")
  public String scopeSeparator() {
    return scopeSeparator;
  }
}
