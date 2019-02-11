package lds.resource;

import org.openrdf.model.URI;
import slib.graph.model.impl.repo.URIFactoryMemory;



public class R {

	private URI uri = null;

	public R(String uri) {
		this.uri = URIFactoryMemory.getSingleton().getURI(uri);
	}

	public R(String baseUri, String name) {
		this.uri = URIFactoryMemory.getSingleton().getURI(baseUri + name);
	}

	public String getTurtle() {
		return "<" + this.uri.toString() + ">";
	}


	public URI getUri() {
		return this.uri;
	}
	
	public boolean equals(R b) {
		if(this.getUri().equals(b.getUri()))
			return true;
		return false;
		
	}



}
