
package no.nmdc.module.validate.process.pojo;

import java.util.Objects;

/**
 *
 * @author kjetilf
 */
public class ScienceKeyword {
    private String category = null;
    private String topic = null;
    private String term = null;
    private String varLevel1 = null;
    private String varLevel2 = null;
    private String varLevel3 = null;
    private String detailed = null;

    public ScienceKeyword(String category) {
        this.category = category;
    }

    public ScienceKeyword(String category, String topic) {
        this.category = category;
        this.topic = topic;
    }

    public ScienceKeyword(String category, String topic, String term) {
        this.category = category;
        this.topic = topic;
        this.term = term;
    }

    public ScienceKeyword(String category, String topic, String term, String varLevel1) {
        this.category = category;
        this.topic = topic;
        this.term = term;
        this.varLevel1 = varLevel1;
    }

    public ScienceKeyword(String category, String topic, String term, String varLevel1, String varLevel2) {
        this.category = category;
        this.topic = topic;
        this.term = term;
        this.varLevel1 = varLevel1;
        this.varLevel2 = varLevel2;
    }

    public ScienceKeyword(String category, String topic, String term, String varLevel1, String varLevel2, String varLevel3) {
        this.category = category;
        this.topic = topic;
        this.term = term;
        this.varLevel1 = varLevel1;
        this.varLevel2 = varLevel2;
        this.varLevel3 = varLevel3;
    }

    public ScienceKeyword(String category, String topic, String term, String varLevel1, String varLevel2, String varLevel3, String detailed) {
        this.category = category;
        this.topic = topic;
        this.term = term;
        this.varLevel1 = varLevel1;
        this.varLevel2 = varLevel2;
        this.varLevel3 = varLevel3;
        this.detailed = detailed;
    }

    public String getCategory() {
        return category;
    }

    public String getTopic() {
        return topic;
    }

    public String getTerm() {
        return term;
    }

    public String getVarLevel1() {
        return varLevel1;
    }

    public String getVarLevel2() {
        return varLevel2;
    }

    public String getVarLevel3() {
        return varLevel3;
    }

    public String getDetailed() {
        return detailed;
    }       

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.category);
        hash = 17 * hash + Objects.hashCode(this.topic);
        hash = 17 * hash + Objects.hashCode(this.term);
        hash = 17 * hash + Objects.hashCode(this.varLevel1);
        hash = 17 * hash + Objects.hashCode(this.varLevel2);
        hash = 17 * hash + Objects.hashCode(this.varLevel3);
        hash = 17 * hash + Objects.hashCode(this.detailed);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ScienceKeyword other = (ScienceKeyword) obj;
        if (!Objects.equals(this.category, other.category)) {
            return false;
        }
        if (!Objects.equals(this.topic, other.topic)) {
            return false;
        }
        if (!Objects.equals(this.term, other.term)) {
            return false;
        }
        if (!Objects.equals(this.varLevel1, other.varLevel1)) {
            return false;
        }
        if (!Objects.equals(this.varLevel2, other.varLevel2)) {
            return false;
        }
        if (!Objects.equals(this.varLevel3, other.varLevel3)) {
            return false;
        }
        if (!Objects.equals(this.detailed, other.detailed)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ScienceKeyword{" + "category=" + category + ", topic=" + topic + ", term=" + term + ", varLevel1=" + varLevel1 + ", varLevel2=" + varLevel2 + ", varLevel3=" + varLevel3 + ", detailed=" + detailed + '}';
    }       
    
}
