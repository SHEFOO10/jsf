package tech.shefoo;

public class Skill {
	
	private int id;
	private String name;

	
	public Skill() {}
	
	public Skill(String newSkillName) {
		// TODO Auto-generated constructor stub
		setName(newSkillName);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
