package lds.measures.ldsd;

import java.util.Set;

import org.openrdf.model.URI;

import lds.measures.LdSimilarityMeasureBase;
import lds.resource.R;
import slib.graph.model.graph.G;
import slib.graph.model.graph.elements.E;
import slib.graph.model.graph.utils.Direction;


public class LDSD extends LdSimilarityMeasureBase {

	public double compare(R a, R b, G g) {

                return LDSD_cw(a.getUri(),b.getUri(), g);	

	}

	/**
	 * Returns number of links between a ---n--> b
	 * 
	 * @param a
	 * @param b
	 * @param g
	 * @return
	 */

	public int cd(URI a, URI b, G g) {

		Set<E> a_edgeOut = g.getE(a, Direction.OUT);

		int c = 0;
		for (E e : a_edgeOut) {
			if (e.getTarget().equals(b))
				c++;
		}

		return c;
	}

	/**
	 * Nbr of nodes from a ---- l ---> count(x)
	 * 
	 * @param a
	 * @param l
	 * @param g
	 * @return
	 */

	public int cd(URI a, E l, G g) {

		Set<E> a_edgeOut = g.getE(a, Direction.OUT);

		int c = 0;
		for (E e : a_edgeOut) {
			if (e.getURI().equals(l.getURI()))
				c++;
		}

		return c;
	}

	/**
	 * returns 1 if a ----- count(l) ----> b
	 * 
	 * @param a
	 * @param b
	 * @param l
	 * @param g
	 * @return
	 */
	public int cd(URI a, URI b, URI l, G g) {

		Set<E> a_edgeOut = g.getE(a, Direction.OUT);

		for (E e : a_edgeOut) {
			if (e.getURI().equals(l) && e.getTarget().equals(b))
				return 1;
		}

		return 0;
	}

	public double LDSD_d(URI a, URI b, G g) {
		double d = 1 + cd(a, b, g) + cd(b, a, g);
		return 1 / d;
	}

	public double LDSD_dw(URI a, URI b, G g) {

		double s_a = cd_sum(a, b, g);

		double s_b = cd_sum(b, a, g);

		return 1 / (1 + s_a + s_b);
	}

	private double cd_sum(URI a, URI b, G g) {
		Set<E> a_edgeOut = g.getE(a, Direction.OUT);
		double s = 0;
		for (E e : a_edgeOut) {
			if (e.getTarget().equals(b)) {
				double x = cd(a, b, e.getURI(), g);
				double y = 1 + Math.log(cd(a, e, g));
				s += x / (y);
			}
		}
		return s;
	}

	public int cio(URI a, URI b, G g) {

		Set<E> a_edgeOut = g.getE(a, Direction.OUT);
		int count = 0;
		for (E e : a_edgeOut) {
			URI x = e.getTarget();
			if (g.containsEdge(b, e.getURI(), x))
				count++;
		}

		return count;
	}


	public int cii(URI a, URI b, G g) {

		Set<E> a_edgeIn = g.getE(a, Direction.IN);
		int count = 0;
		for (E e : a_edgeIn) {
			URI x = e.getSource();
			if (g.containsEdge(x, e.getURI(), b))
				count++;
		}

		return count;
	}

	public double LDSD_i(URI a, URI b, G g) {
		double d = 1 + cio(a, b, g) + cii(a, b, g);
		return 1 / d;
	}

	public double LDSD_iw(URI a, URI b, G g) {

		double s_cii = cii_sum(a, b, g);
		double s_cio = cio_sum(a, b, g);

		return 1 / (1 + s_cii + s_cio);
	}

	private double cio_sum(URI a, URI b, G g) {

		Set<E> a_edgeOut = g.getE(a, Direction.OUT);
		double s = 0;
		for (E e : a_edgeOut) {

			double x = cio(a, b, e.getURI(), g);
			double y = Math.log(cio(a, e, g));
			s += x / (1 + y);
		}

		return s;
	}

	/**
	 * todo
	 * 
	 * @param a
	 * @param b
	 * @param g
	 * @return
	 */
	public int cio(URI a, URI b, URI l, G g) {

		Set<E> a_edgeOut = g.getE(a, Direction.OUT);

		for (E e : a_edgeOut) {
			URI x = e.getTarget();
			if (g.containsEdge(b, l, x))
				return 1;
		}

		return 0;
	}

	/**
	 * Count number of indirect outgoing distinct links to a a --l--> x <----l ----
	 * count(n)
	 * 
	 * @param a
	 * @param l
	 * @param g
	 * @return
	 */

	public int cio(URI a, E l, G g) {

		Set<E> a_edgeOut = g.getE(a, Direction.OUT);
		int count = 0;
		for (E e : a_edgeOut) {
			if (l.getURI().equals(e.getURI())) {
				URI target = e.getTarget();
				count = g.getE(l.getURI(), target, Direction.IN).size();
			}
		}

		if (count > 0)
			count--; // to exclude a edge

		return count;
	}

	private double cii_sum(URI a, URI b, G g) {

		Set<E> a_edgeIn = g.getE(a, Direction.IN);

		double s = 0;

		for (E e : a_edgeIn) {

			double x = cii(a, b, e.getURI(), g);
			double y = Math.log(cii(a, e, g));
			s += x / (1 + y);

		}
		return s;
	}

	public int cii(URI a, URI b, URI l, G g) {

		Set<E> a_edgeIn = g.getE(a, Direction.IN);

		for (E e : a_edgeIn) {
			URI source = e.getSource();
			if (g.containsEdge(source, l, b))
				return 1;
		}

		return 0;
	}

	public int cii(URI a, E l, G g) {

		Set<E> a_edgeIn = g.getE(a, Direction.IN);
		int count = 0;
		for (E e : a_edgeIn) { // to fix, optimize this, we have only 1 in edge l to a ?
			if (e.getURI().equals(l.getURI())) {
				URI source = e.getSource();
				count = g.getE(source, l.getURI(), Direction.OUT).size();
			}
		}
		if (count > 0)
			count--; // -1 to exclude a edge ?
		return count;
	}

	public double LDSD_cw(URI a, URI b, G g) {

		double s_cd_a = cd_sum(a, b, g);

		double s_cd_b = cd_sum(b, a, g);

		double s_cii = cii_sum(a, b, g);
		double s_cio = cio_sum(a, b, g);

		return 1 / (1 + s_cd_a + s_cd_b + s_cii + s_cio);
	}
        
        
        
        
        
        public int cioT(URI a, URI b, G g) {

            Set<E> a_edgeOut = g.getE(a, Direction.OUT);
            int count = 0;
            for (E e : a_edgeOut) {
                URI x = e.getTarget();
                Set<E> x_edgeOut = g.getE(x, Direction.OUT);
                for (E edge : x_edgeOut) {
                    if(edge.getSource().equals(b))
                        count++;
                }
                            
            }

            return count;
	}
         
        public int ciiT(URI a, URI b, G g) {

            Set<E> a_edgeOut = g.getE(a, Direction.IN);
            int count = 0;
            for (E e : a_edgeOut) {
                URI x = e.getTarget();
                Set<E> x_edgeIn = g.getE(x, Direction.IN);
                for (E edge : x_edgeIn) {
                    if(edge.getTarget().equals(b))
                        count++;
                }
            }

            return count;
	}
          
       
        
        public double ciiT_sum(URI a, URI b, G g){
            Set<E> a_edgeIn = g.getE(a, Direction.IN);
            Set<URI> verticies = g.getV();

            double s = 0 , y = 0;

            for (E e : a_edgeIn) {

                    double x = ciiT(a, b, g);
                    for (URI vertex : verticies){
                        y = y + Math.log(ciiT(a, vertex, g));
                    }
                    s += x / (1 + y);

            }
            return s;
            
        }
        
         public double cioT_sum(URI a, URI b, G g){
            Set<E> a_edgeIn = g.getE(a, Direction.OUT);
            Set<URI> verticies = g.getV();

            double s = 0 , y = 0;

            for (E e : a_edgeIn) {

                    double x = cioT(a, b, g);
                    for (URI vertex : verticies){
                        y = y + Math.log(ciiT(a, vertex, g));
                    }
                    s += x / (1 + y);

            }
            return s;
            
        }
         
         //typless LDSD
         
          public double LDSD_t(URI a, URI b, G g){
            double d = 1 + cioT(a, b, g) + ciiT(a, b, g);
	    return 1 / d;
            
        }
         
         public double lDSD_ty(URI a, URI b, G g){
            double s_cd_a = cd_sum(a, b, g);

            double s_cd_b = cd_sum(b, a, g);

            double s_ciiT = ciiT_sum(a, b, g);
            double s_cioT = cioT_sum(a, b, g);

            return 1 / (1 + s_cd_a + s_cd_b + s_ciiT + s_cioT);
         }

}
