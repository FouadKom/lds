package lds.resource;

import org.openrdf.model.URI;
import slib.graph.model.impl.repo.URIFactoryMemory;



public class R {

	private URI uri = null;
        private String nameSpace = null;
        private String suffix = null;

	public R(String uri) {
		this.uri = URIFactoryMemory.getSingleton().getURI(uri);
                this.nameSpace = URIFactoryMemory.getSingleton().getNamespace(uri);
	}

	public R(String baseUri, String name) {
		this.uri = URIFactoryMemory.getSingleton().getURI(baseUri + name);
                this.nameSpace = baseUri;
                this.suffix = name;
	}

	public String getTurtle() {
		return "<" + this.uri.toString() + ">";
	}


	public URI getUri() {
		return this.uri;
	}
        
        public String getNamespace() {
            if(nameSpace == null)
                nameSpace = uri.getNamespace();
		
            return this.nameSpace;
	}
        
        public String getLocalName(){
            if(suffix == null){
                
               suffix = uri.getLocalName();
                      
            }
            
            return suffix;
        }
	
	public boolean equals(R b) {
		if(this.getUri().equals(b.getUri()))
			return true;
		return false;
		
	}

        @Override
        public String toString(){
            return uri.stringValue();
        }

}
