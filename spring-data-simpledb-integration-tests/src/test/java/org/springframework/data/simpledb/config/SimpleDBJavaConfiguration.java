package org.springframework.data.simpledb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.simpledb.config.AWSCredentials;
import org.springframework.data.simpledb.config.AbstractSimpleDBConfiguration;
import org.springframework.data.simpledb.core.SimpleDb;
import org.springframework.data.simpledb.repository.config.EnableSimpleDBRepositories;
import org.springframework.data.simpledb.util.HostNameResolver;

@EnableSimpleDBRepositories(basePackages = "org.springframework.data.simpledb.repository")
@Configuration
public class SimpleDBJavaConfiguration extends AbstractSimpleDBConfiguration {

	@Override
	public AWSCredentials getAWSCredentials() {
		return new AWSCredentials(System.getProperty("awsAccessKey"), System.getProperty("awsSecretKey"));
	}

	@Override
	public void setExtraProperties(SimpleDb simpleDb) {
		simpleDb.setConsistentRead(true);
		simpleDb.setDomainPrefix(HostNameResolver.readHostname() + "testDB");
	}
}
