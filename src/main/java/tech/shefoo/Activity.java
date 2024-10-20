
package tech.shefoo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



public class Activity implements Cloneable {
    private int id;
    
    private String name;

    private String description;
    private Integer min_age;
    private Integer max_age;
    private List<Skill> skills = new ArrayList<>();

	public String getSkillsAsString() {
        return skills.stream()
        		.map(Skill::getName)
        		.collect(Collectors.joining(", "));
	}


	private boolean isNewRecord = true;

	private Map<String, String> props;
    
    public Activity(int id, String name, String description, Integer min_age, Integer max_age) {
    	super();
    	this.id = id;
    	this.name = name;
    	this.description = description;
    	this.min_age = min_age;
    	this.max_age = max_age;
    }

    // Add other fields as necessary

   
    public Activity() {
		// TODO Auto-generated constructor stub
	}


	// Getter and setter for id
    public int getId() {
        return id;
    }

    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getMin_age() {
		return min_age;
	}

	public void setMin_age(Integer min_age) {
		this.min_age = min_age;
	}

	public Integer getMax_age() {
		return max_age;
	}

	public void setMax_age(Integer max_age) {
		this.max_age = max_age;
	}

	public void setId(int id) {
        this.id = id;
    }

    // Getter and setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Add getters and setters for other fields as necessary

    @Override
    public String toString() {
        return "Member{" +
               "id=" + id +
               ", name='" + name + '\'' +
               '}';
    }
    
	public Map<String, String> getProps() {
		return props;
	}

	public void setProps(Map<String, String> p) {
		this.props = p;
	}

	 @Override
	 public Object clone() throws CloneNotSupportedException {
	 return super.clone();
	 }

	public boolean isNewRecord() {
		return isNewRecord;
	}

	public void setNewRecord(boolean isNewRecord) {
		this.isNewRecord = isNewRecord;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
}
