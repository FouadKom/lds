/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lds.conf;

import slib.utils.i.Conf;
/**
 *
 * @author Fouad Komeiha
 */
public class Config {
    Conf conf;
    
    public Config(){
        this.conf = new Conf();
    }
    
    public Config(Conf conf){
        this.conf = conf;
    }
    
    public Config addParam(ConfigParam param , Object object){
        conf.addParam(param.toString() , object);
        return this;
    }
    
    public void removeParam(ConfigParam param) {
        conf.removeParam(param.toString());
    }
    
    public Object getParam(ConfigParam param){
        return conf.getParam(param.toString());
    }
    
    public int getSize(){
        return conf.getParams().size();
    }
    
    public boolean containsParam(ConfigParam param) {
        return (conf.getParam(param.toString()) != null);
    }
    
    @Override
    public String toString() {
        return conf.toString();
    }
    
}