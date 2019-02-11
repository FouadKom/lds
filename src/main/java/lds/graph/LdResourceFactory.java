package lds.graph;


import lds.resource.R;
import sc.research.ldq.LdDatasetFactory;

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
}
