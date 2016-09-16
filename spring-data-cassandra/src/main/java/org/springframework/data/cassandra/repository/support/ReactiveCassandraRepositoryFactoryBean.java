/*
 * Copyright 2016 the original author or authors.
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

package org.springframework.data.cassandra.repository.support;

import java.io.Serializable;

import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.ReactiveCassandraOperations;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.util.Assert;

/**
 * {@link org.springframework.beans.factory.FactoryBean} to create
 * {@link org.springframework.data.cassandra.repository.ReactiveCassandraRepository} instances.
 * 
 * @author Mark Paluch
 * @since 2.0
 * @see org.springframework.data.repository.reactive.ReactivePagingAndSortingRepository
 * @see org.springframework.data.repository.reactive.RxJavaPagingAndSortingRepository
 */
public class ReactiveCassandraRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
		extends RepositoryFactoryBeanSupport<T, S, ID> {

	private ReactiveCassandraOperations operations;
	private boolean mappingContextConfigured = false;

	/**
	 * Configures the {@link ReactiveCassandraOperations} used for Cassandra data access operations.
	 *
	 * @param operations {@link ReactiveCassandraOperations} used to perform CRUD, Query and general data access operations
	 * on Apache Cassandra.
	 */
	public void setReactiveCassandraOperations(ReactiveCassandraOperations operations) {
		this.operations = operations;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport#setMappingContext(org.springframework.data.mapping.context.MappingContext)
	 */
	@Override
	protected void setMappingContext(MappingContext<?, ?> mappingContext) {

		super.setMappingContext(mappingContext);
		this.mappingContextConfigured = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.data.repository.support.RepositoryFactoryBeanSupport
	 * #createRepositoryFactory()
	 */
	@Override
	protected final RepositoryFactorySupport createRepositoryFactory() {
		return getFactoryInstance(operations);
	}

	/**
	 * Creates and initializes a {@link RepositoryFactorySupport} instance.
	 * 
	 * @param operations
	 * @return
	 */
	protected RepositoryFactorySupport getFactoryInstance(ReactiveCassandraOperations operations) {
		return new ReactiveCassandraRepositoryFactory(operations);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.support.RepositoryFactoryBeanSupport#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {

		super.afterPropertiesSet();
		Assert.notNull(operations, "ReactiveCassandraOperations must not be null!");

		if (!mappingContextConfigured) {
			setMappingContext(operations.getConverter().getMappingContext());
		}
	}
}
