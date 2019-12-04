package lds.resource;

import org.openrdf.model.URI;
import slib.graph.model.impl.repo.URIFactoryMemory;



public class R {

	private URI uri = null;
        private String nameSpace = null;

	public R(String uri) {
		this.uri = URIFactoryMemory.getSingleton().getURI(uri);
                this.nameSpace = URIFactoryMemory.getSingleton().getNamespace(uri);
	}

	public R(String baseUri, String name) {
		this.uri = URIFactoryMemory.getSingleton().getURI(baseUri + name);
                this.nameSpace = baseUri;
	}

	public String getTurtle() {
		return "<" + this.uri.toString() + ">";
	}


	public URI getUri() {
		return this.uri;
	}
        
        public String getNamespace() {
		return this.nameSpace;
	}
	
	public boolean equals(R b) {
		if(this.getUri().equals(b.getUri()))
			return true;
		return false;
		
	}



}
