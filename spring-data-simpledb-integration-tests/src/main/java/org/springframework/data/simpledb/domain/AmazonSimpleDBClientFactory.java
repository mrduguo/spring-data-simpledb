package org.springframework.data.simpledb.domain;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;

public final class AmazonSimpleDBClientFactory {

    private AmazonSimpleDBClientFactory() { }

	public static final String TEST_AMAZON_ACCESS_KEY = System.getProperty("awsAccessKey");
	public static final String TEST_AMAZON_PRIVATE_KEY = System.getProperty("awsSecretKey");

	public static AmazonSimpleDBClient getTestClient() {
		return new AmazonSimpleDBClient(new AWSCredentials() {

			@Override
			public String getAWSAccessKeyId() {
				return TEST_AMAZON_ACCESS_KEY;
			}

			@Override
			public String getAWSSecretKey() {
				return TEST_AMAZON_PRIVATE_KEY;
			}
		});
	}
}
