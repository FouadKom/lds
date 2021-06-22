package lds.resource;

import org.apache.jena.query.QueryFactory;

import ldq.LdDatasetFactory;

public class LdResourceFactory {
	private static LdResourceFactory factory;

	
	private String name;
	private String baseUri;
	
	public static LdResourceFactory getInstance() {
		return (factory == null) ? (factory = new LdResourceFactory()) : factory;
	}
	
	public LdResourceFactory name(String name) {
		this.name = name;
		return factory;
	}
	
	public LdResourceFactory baseUri(String uri) {
		this.baseUri = uri;
		return factory;
	}

	public R create() {
		return new R(baseUri + name);
	}

	public LdResourceFactory uri(String uri) {
		this.baseUri = uri;
		this.name = "";
		return factory;

	}
}
