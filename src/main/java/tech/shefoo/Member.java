
package tech.shefoo;

import java.util.Map;

public class Member implements Cloneable {
    private int id;
    
    private String name;
    private String national_id;
    private String phone;
    private String email;
    private String date_of_birth;
    private String address;
    private String profileImg;
    
    public String getProfileImg() {
		return profileImg;
	}

	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}

	private boolean isNewRecord = true;
    
	private Map<String, String> props;
    
    public Member(int id, String name, String national_id, String phone, String email, String date_of_birth,
    		String address) {
    	super();
    	this.id = id;
    	this.name = name;
    	this.national_id = national_id;
    	this.phone = phone;
    	this.email = email;
    	this.date_of_birth = date_of_birth;
    	this.address = address;
    }

    // Add other fields as necessary

    public String getNational_id() {
		return national_id;
	}

	public void setNational_id(String national_id) {
		this.national_id = national_id;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDate_of_birth() {
		return date_of_birth;
	}

	public void setDate_of_birth(String date_of_birth) {
		this.date_of_birth = date_of_birth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	// Default constructor
    public Member() {}

    // Parameterized constructor (optional)
    public Member(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter and setter for id
    public int getId() {
        return id;
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
}
