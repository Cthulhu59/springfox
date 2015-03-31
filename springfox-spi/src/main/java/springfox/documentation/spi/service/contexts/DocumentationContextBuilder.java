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

package springfox.documentation.spi.service.contexts;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.base.Function;
import com.google.common.collect.Ordering;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.PathProvider;
import springfox.documentation.builders.BuilderDefaults;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.Authorization;
import springfox.documentation.service.AuthorizationType;
import springfox.documentation.service.Operation;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.GenericTypeNamingStrategy;
import springfox.documentation.spi.service.ResourceGroupingStrategy;
import springfox.documentation.builders.PathSelectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Optional.*;
import static com.google.common.collect.FluentIterable.*;
import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Maps.*;
import static com.google.common.collect.Sets.*;

public class DocumentationContextBuilder {

  private TypeResolver typeResolver;
  private List<RequestMappingHandlerMapping> handlerMappings;
  private ApiInfo apiInfo;
  private String groupName;
  private ResourceGroupingStrategy resourceGroupingStrategy;
  private PathProvider pathProvider;
  private AuthorizationContext authorizationContext;
  private List<AuthorizationType> authorizationTypes;
  private Ordering<ApiListingReference> listingReferenceOrdering;
  private Ordering<ApiDescription> apiDescriptionOrdering;
  private DocumentationType documentationType;
  private Ordering<Operation> operationOrdering;

  private boolean applyDefaultResponseMessages;
  private ApiSelector apiSelector = ApiSelector.DEFAULT;
  private Set<Class> ignorableParameterTypes = newHashSet();
  private Map<RequestMethod, List<ResponseMessage>> responseMessageOverrides = newTreeMap();
  private List<AlternateTypeRule> rules = newArrayList();
  private Map<RequestMethod, List<ResponseMessage>> defaultResponseMessages = newHashMap();
  private Set<String> protocols = newHashSet();
  private Set<String> produces = newHashSet();
  private Set<String> consumes = newHashSet();
  private GenericTypeNamingStrategy genericsNamingStrategy;

  public DocumentationContextBuilder(DocumentationType documentationType) {
    this.documentationType = documentationType;
  }

  public DocumentationContextBuilder handlerMappings(List<RequestMappingHandlerMapping> handlerMappings) {
    this.handlerMappings = handlerMappings;
    return this;
  }

  public DocumentationContextBuilder apiInfo(ApiInfo apiInfo) {
    this.apiInfo = BuilderDefaults.defaultIfAbsent(apiInfo, this.apiInfo);
    return this;
  }

  public DocumentationContextBuilder groupName(String groupName) {
    this.groupName = BuilderDefaults.defaultIfAbsent(groupName, this.groupName);
    return this;
  }

  public DocumentationContextBuilder additionalIgnorableTypes(Set<Class> ignorableParameterTypes) {
    this.ignorableParameterTypes.addAll(ignorableParameterTypes);
    return this;
  }

  public DocumentationContextBuilder additionalResponseMessages(
          Map<RequestMethod, List<ResponseMessage>> additionalResponseMessages) {
    this.responseMessageOverrides.putAll(additionalResponseMessages);
    return this;
  }

  public DocumentationContextBuilder withResourceGroupingStrategy(ResourceGroupingStrategy resourceGroupingStrategy) {
    this.resourceGroupingStrategy = resourceGroupingStrategy;
    return this;
  }

  public DocumentationContextBuilder pathProvider(PathProvider pathProvider) {
    this.pathProvider = BuilderDefaults.defaultIfAbsent(pathProvider, this.pathProvider);
    return this;
  }

  public DocumentationContextBuilder authorizationContext(AuthorizationContext authorizationContext) {
    this.authorizationContext = BuilderDefaults.defaultIfAbsent(authorizationContext, this.authorizationContext);
    return this;
  }

  public DocumentationContextBuilder authorizationTypes(List<AuthorizationType> authorizationTypes) {
    this.authorizationTypes = authorizationTypes;
    return this;
  }

  public DocumentationContextBuilder apiListingReferenceOrdering(
          Ordering<ApiListingReference> listingReferenceOrdering) {

    this.listingReferenceOrdering = BuilderDefaults.defaultIfAbsent(listingReferenceOrdering, this
            .listingReferenceOrdering);
    return this;
  }

  public DocumentationContextBuilder apiDescriptionOrdering(Ordering<ApiDescription> apiDescriptionOrdering) {
    this.apiDescriptionOrdering = BuilderDefaults.defaultIfAbsent(apiDescriptionOrdering, this.apiDescriptionOrdering);
    return this;
  }

  private Map<RequestMethod, List<ResponseMessage>> aggregateResponseMessages() {
    Map<RequestMethod, List<ResponseMessage>> responseMessages = newHashMap();
    if (applyDefaultResponseMessages) {
      responseMessages.putAll(defaultResponseMessages);
    }
    responseMessages.putAll(responseMessageOverrides);
    return responseMessages;
  }

  public DocumentationContextBuilder applyDefaultResponseMessages(boolean applyDefaultResponseMessages) {
    this.applyDefaultResponseMessages = applyDefaultResponseMessages;
    return this;
  }

  public DocumentationContextBuilder ruleBuilders(List<Function<TypeResolver, AlternateTypeRule>> ruleBuilders) {
    rules.addAll(from(ruleBuilders)
            .transform(evaluator(typeResolver))
            .toList());
    return this;
  }

  public DocumentationContextBuilder typeResolver(TypeResolver typeResolver) {
    this.typeResolver = typeResolver;
    return this;
  }

  public DocumentationContextBuilder operationOrdering(Ordering<Operation> operationOrdering) {
    this.operationOrdering = BuilderDefaults.defaultIfAbsent(operationOrdering, this.operationOrdering);
    return this;
  }

  public DocumentationContextBuilder rules(List<AlternateTypeRule> rules) {
    this.rules.addAll(rules);
    return this;
  }

  public DocumentationContextBuilder defaultResponseMessages(
          Map<RequestMethod, List<ResponseMessage>> defaultResponseMessages) {
    this.defaultResponseMessages.putAll(defaultResponseMessages);
    return this;
  }

  public DocumentationContextBuilder produces(Set<String> produces) {
    this.produces.addAll(produces);
    return this;
  }

  public DocumentationContextBuilder consumes(Set<String> consumes) {
    this.consumes.addAll(consumes);
    return this;
  }
  public DocumentationContextBuilder genericsNaming(GenericTypeNamingStrategy genericsNamingStrategy) {
    this.genericsNamingStrategy = genericsNamingStrategy;
    return this;
  }

  public DocumentationContextBuilder protocols(Set<String> protocols) {
    this.protocols.addAll(protocols);
    return this;
  }

  public DocumentationContextBuilder selector(ApiSelector apiSelector) {
    this.apiSelector = apiSelector;
    return this;
  }

  public DocumentationContext build() {
    Map<RequestMethod, List<ResponseMessage>> responseMessages = aggregateResponseMessages();
    AuthorizationContext authorizationContext = fromNullable(this.authorizationContext)
            .or(new AuthorizationContext.AuthorizationContextBuilder()
                .withAuthorizations(new ArrayList<Authorization>())
                .forPaths(PathSelectors.any())
                .build());
    return new DocumentationContext(documentationType, handlerMappings, apiInfo, groupName,
            apiSelector, ignorableParameterTypes, responseMessages,
            resourceGroupingStrategy, pathProvider,
            authorizationContext, authorizationTypes, rules,
            listingReferenceOrdering, apiDescriptionOrdering,
            operationOrdering, produces, consumes, protocols, genericsNamingStrategy);
  }

  private Function<Function<TypeResolver, AlternateTypeRule>, AlternateTypeRule>
  evaluator(final TypeResolver typeResolver) {

    return new Function<Function<TypeResolver, AlternateTypeRule>, AlternateTypeRule>() {
      @Override
      public AlternateTypeRule apply(Function<TypeResolver, AlternateTypeRule> input) {
        return input.apply(typeResolver);
      }
    };
  }
}