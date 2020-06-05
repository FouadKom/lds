/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.LdManager;

import java.util.List;
import lds.resource.R;
import org.openrdf.model.URI;

/**
 *
 * @author Fouad Komeiha
 */
public interface LdManagerO {
    public List<String> getSubjects(R a);
    public List<String> getObjects(R a);
    public List<String> getSameAsResources(R a);
    public List<String> getCommonObjects(R a , R b);
    public List<String> getCommonObjects(R a);
    public List<String> getCommonSubjects(R a , R b);
    public List<String> getCommonSubjects(R a);
    public List<String> getEdges(R a); 
    public List<URI> getEdges(R a, R b);    
    public int countPropertyOccurrence(URI link);
    public int countShareCommonObjects(URI link , R a);
    public int countShareCommonSubjects(URI link , R a);
    public int countResource();
    public int countSubject(URI link , R a);
    public int countSubject(R a);
    public int countObject(URI link , R a);
    public int countObject(R a);
   
    
}
