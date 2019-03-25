/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.measures.resim;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import lds.LdManager.LdManager;
import lds.graph.GraphManager;
import lds.measures.LdSimilarityMeasureBase;
import lds.resource.LdResourceFactory;
import lds.resource.R;
import org.apache.commons.httpclient.HttpException;
import static org.junit.Assert.fail;
import org.openrdf.model.URI;
import sc.research.ldq.LdDataset;
import slib.graph.model.graph.G;
import slib.graph.model.graph.elements.E;
import slib.graph.model.graph.utils.Direction;
import slib.graph.model.impl.graph.elements.Edge;
import slib.utils.ex.SLIB_Ex_Critic;

/**
 *
 * @author Fouad Komeiha
 */
public class Resim extends LdSimilarityMeasureBase {
	private LdDataset dataset;
	private G graph;
	public Set<URI> edges;

	public LdManager resimLDLoader;

	public Resim() {

	}

////	 TODO, remove, because dataset is inside LdLoader
//	public Resim(LdDataset dataset) {
//		this.dataset = dataset;
//		this.edges = ResimLdManager.getEdges(dataset);
//	}

	public Resim(LdManager resimLDLoader) {
		this.resimLDLoader = resimLDLoader;
//                this.edges = ResimLdManager.getEdges();
	}
        
        
//	public Resim(LdDataset dataset, G graph, Set<URI> edges) throws SLIB_Ex_Critic {// this constructor is added to be used in tests classes
//		this.dataset = dataset;
//		this.graph = graph;
//		this.edges = edges;
//	}

	public double compare(R a, R b) {
		double sim = 0;
		try {
                        
			sim = Resim(a, b);

		} catch (SLIB_Ex_Critic ex) {
			Logger.getLogger(Resim.class.getName()).log(Level.SEVERE, null, ex);
		}
		return sim;
	}

	public double Resim(R a, R b) throws SLIB_Ex_Critic {
		/*
		 * if (graph.getNumberVertices() == 0 && edges.isEmpty()) { try { this.graph =
		 * ResimLDLoader.generateGraph(a, b, dataset); } catch (HttpException ex) {
		 * Logger.getLogger(Resim.class.getName()).log(Level.SEVERE, null, ex); }
		 * 
		 * }
		 */
		int w1 = 1, w2 = 1;
		double x = 0, y = 0;
                
                this.edges = ResimLdManager.getEdges(a , b);
		// TODO: if we're in the same dataset, it's not necessary to check sameAs ?
		if (a.equals(b) || resimLDLoader.isSameAs(a, b))
			return 1;
                        
		x = w1 * PropertySim(a, b);
		y = w2 * LDSDsim(a, b);
		return (x + y) / (w1 + w2);

	}

	public double PropertySim(R a, R b) {
		double x = 0, y = 0, ip = 0, op = 0, ppty = 0;

		for (URI e : edges) {
			x = x + ((double) Csip(e, a, b) / Cd(e));
			y = y + ((double) Csop(e, a, b) / Cd(e));
		}

		ip = x / (Cip(a) + Cip(b));
		op = y / (Cop(a) + Cop(b));

		ppty = ip + op;

		return ppty;
	}

	public double LDSD(R a, R b) {
		double cdA_norm = 0, cdB_norm = 0, cii_norm = 0, cio_norm = 0;

		for (URI l : edges) {
			cdA_norm = cdA_norm + Cd_normalized(l, a, b);
			cdB_norm = cdB_norm + Cd_normalized(l, b, a);
			cii_norm = cii_norm + Cii_normalized(l, a , b);
			cio_norm = cio_norm + Cio_normalized(l, a , b);

		}

		return 1 / (1 + cdA_norm + cdB_norm + cii_norm + cio_norm);
	}

	public double LDSDsim(R a, R b) {
		return 1 - LDSD(a, b);
	}

	public int Cip(R a) {
//		Set<E> ingoing = graph.getE(a.getUri(), Direction.IN); // count of ingoing edges to a
//		return ingoing.size();
                return resimLDLoader.countIngoingEdges(a);
	}

	public int Cop(R a) {
//		Set<E> outgoing = graph.getE(a.getUri(), Direction.OUT); // count of outgoing edges frm a 
//		return outgoing.size();
                return resimLDLoader.countOutgoingEdges(a);
	}

	public int Csip(URI l, R a, R b) {
//		Set<E> edgesA = graph.getE(l, a.getUri(), Direction.IN); //count of ingoing edges of type l to a
//		Set<E> edgesB = graph.getE(l, b.getUri(), Direction.IN); // count of ingoing edges of type l to b
//		if (edgesA.isEmpty() || edgesB.isEmpty())
//			return 0;
//		return 1;
                if (resimLDLoader.countIngoingEdges(l , a) > 0 && resimLDLoader.countIngoingEdges(l , b ) > 0)
                    return 1;
                return 0;
	}

	public int Csop(URI l, R a, R b) {
//		Set<E> edgesA = graph.getE(l, a.getUri(), Direction.OUT); 
//		Set<E> edgesB = graph.getE(l, b.getUri(), Direction.OUT); 
//		if (edgesA.isEmpty() || edgesB.isEmpty())
//			return 0;
//		return 1;
                 if (resimLDLoader.countOutgoingEdges(l , a) > 0 && resimLDLoader.countOutgoingEdges(l , b ) > 0)
                    return 1;
                return 0;
	}

	public int Cd(URI l, R a, R b) {
//		Set<E> a_edgeOut = graph.getE(l, a.getUri(), Direction.OUT); 
//
//		for (E e : a_edgeOut) {
//			if (e.getTarget().equals(b))
//				return 1;
//		}
//
//		return 0;
                
                if(resimLDLoader.isDirectlyConnected(l, a, b))
                    return 1;
                else
                    return 0;
	}

	public int Cd(URI l, R a) {
//		Set<E> a_edgeOut = graph.getE(l, a, Direction.OUT);
//
//		return a_edgeOut.size();
                return resimLDLoader.countOutgoingEdges(l , a);
	}

	public int Cd(URI l) {
		return ResimLdManager.countPropertyOccurrence(l);
	}

	public double Cd_normalized(URI l, R a, R b) {
		int cd = Cd(l, a, b);
		double cd_norm;

		double x = 1 + Math.log(Cd(l, a));

		cd_norm = (double) cd / x;

		return cd_norm;

	}

	public int Cii(URI l, R a, R b) {

//		Set<E> a_edgeIn = graph.getE(l, a.getUri(), Direction.IN);
//		for (E e : a_edgeIn) {
//			URI x = e.getSource();
//			if (graph.containsEdge(x, e.getURI(), b.getUri()))
//				return 1;
//		}
//		return 0;
                
                if (resimLDLoader.shareCommonSubject(l, a , b))
                    return 1;
                return 0;
	}

	public int Cii(URI l, R a) {

//		Set<E> a_edgeIn = graph.getE(l, a.getUri(), Direction.IN);
//		int count = 0;
//		for (E e : a_edgeIn) {
//			URI x = e.getSource();
//			Set<E> x_edgeOut = graph.getE(l, x, Direction.OUT);
//			E edge = new Edge(x, l, a.getUri());
//			count = count + x_edgeOut.size();
//			if (x_edgeOut.contains(edge))
//				count--;
//
//		}
//
//		return count;

                return resimLDLoader.countShareCommonSubjects(l, a);
	}

	public double Cii_normalized(URI l, R a, R b) {

		int ciiA, ciiB, cii;
		double x, cii_norm;

		cii = Cii(l, a, b);
		ciiA = Cii(l, a);
		ciiB = Cii(l, b);
		x = 1 + Math.log((ciiA + ciiB) / 2);

		cii_norm = ((double) cii / x);

		return cii_norm;

	}

	public int Cio(URI l, R a, R b) {

//		Set<E> a_edgeOut = graph.getE(l, a.getUri(), Direction.OUT);
//		for (E e : a_edgeOut) {
//			URI x = e.getTarget();
//			if (graph.containsEdge(b.getUri(), e.getURI(), x))
//				return 1;
//		}
//
//		return 0;

                if(resimLDLoader.shareCommonObject(l, a , b))
                    return 1;
                return 0;
	}

	public int Cio(URI l, R a) {

//		Set<E> a_edgeOut = graph.getE(l, a.getUri(), Direction.OUT);
//		int count = 0;
//		for (E e : a_edgeOut) {
//			URI x = e.getTarget();
//			Set<E> x_edgeIn = graph.getE(l, x, Direction.IN);
//			E edge = new Edge(a.getUri() , l, x);
//			count = count + x_edgeIn.size();
//			if (x_edgeIn.contains(edge))
//				count--;
//
//		}
//
//		return count;

                return resimLDLoader.countShareCommonObjects(l, a);
	}

	public double Cio_normalized(URI l, R a, R b) {

		int cioA, cioB, cio;
		double cio_norm, x;

		cio = Cio(l, a, b);
		cioA = Cio(l, a);
		cioB = Cio(l, b);
		x = 1 + Math.log((cioA + cioB) / 2);

		cio_norm = ((double) cio / x);

		return cio_norm;

	}

}
