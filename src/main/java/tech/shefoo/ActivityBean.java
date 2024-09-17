package tech.shefoo;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@ManagedBean
@ViewScoped
public class ActivityBean {
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}


	private List<Activity> activities;
	private List<Activity> availableActivities;
	private List<Activity> joinedActivities;
	private ActivityDAO activityDAO = new ActivityDAO();
    private MemberDAO memberDAO = new MemberDAO();
    private Activity newActivity = new Activity();
    private Activity selectedActivity;

    private boolean showErrorPopup;
    private String errorMessage;

    private int memberId;
    private Member member;
    private boolean firstEntrance = true;
    
    
    public void updateAvailableList() {
    	try {
			setAvailableActivities(activityDAO.getAvialableActivites(memberId));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void updateJoinedList() {
    	try {
    		setJoinedActivities(activityDAO.getJoinedActivities(memberId));
    	} catch (SQLException e) {
    		// TODO Auto-generated catch block
    		e.printStackTrace();
    	}
    	
    }
    
    public void searchActivities() {
    	setFirstEntrance(false);
    	updateAvailableList();
    	updateJoinedList();
    	try {
			member = memberDAO.getMemberById(memberId);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public List<Activity> getJoinedActivities() {
		return joinedActivities;
	}


	public void setJoinedActivities(List<Activity> joinedActivities) {
		this.joinedActivities = joinedActivities;
	}


	public void loadActivitys() {
        try {
        	activities = activityDAO.getAllActivites();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public List<Activity> getActivities() {
        if (activities == null) {
            loadActivitys();
        }
        return activities;
    }

    public Activity getNewActivity() {
        return newActivity;
    }

    public void setNewActivity(Activity Activity) {
        this.newActivity = Activity;
    }

    public void addActivity() {
		FacesContext context = validate();
		if (context.getMessageList().size() > 0)
			return;
        try {
        	if (newActivity.getId() == 0) {
        		activityDAO.addActivity(newActivity);
        	} else {
        		activityDAO.updateActivity(selectedActivity, newActivity);
        	}
        	newActivity = new Activity(); // Reset the form        		
            loadActivitys(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    public void editActivity(Integer id) {
        // Find the member by ID and populate `newActivity` for editing
        for (Activity activity : activities) {
            if (activity.getId() == id) {
            	try {
					setNewActivity((Activity) activity.clone());
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	setSelectedActivity(activity);
                break;
            }
        }
    }
   

    public void deleteActivity(int memberId) {
        try {
        	activityDAO.deleteActivity(memberId);
            loadActivitys(); // Refresh the list
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }
    
    
    public void joinActivity(int activityId) {
    	try {
			activityDAO.joinActivity(activityId, memberId);
			updateAvailableList();
			updateJoinedList();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			showErrorPopup();
			setErrorMessage(e.getMessage());
			System.out.println(e);
		}
	}
    
    
    public void leaveActivity(int activityId) {
    	List<Activity> filteredList = new ArrayList<>();
    	try {
    		for (Activity activity : joinedActivities) {
    			if (activity.getId() != activityId)
    				filteredList.add(activity);
    		}
			activityDAO.leaveActivity(activityId, memberId);
			setJoinedActivities(filteredList);
			updateAvailableList();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			showErrorPopup();
			setErrorMessage(e.getMessage());
			System.out.println(e);
		}
	}

    public void resetInputs() {
    	newActivity = new Activity(); // Reset the form   
    }
    
    public Activity getSelectedActivity() {
        return selectedActivity;
    }

    public void setSelectedActivity(Activity selectedActivity) {
        this.selectedActivity = selectedActivity;
    }

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}


	public List<Activity> getAvailableActivities() {
		return availableActivities;
	}


	public void setAvailableActivities(List<Activity> availableActivities) {
		this.availableActivities = availableActivities;
	}
	
	
	public void showErrorPopup() {
		setShowErrorPopup(true);
	}
	
	public void closePopup() {
		System.out.println("close Popup called !");
		closeErrorPopup();
	}
	
	public void closeErrorPopup() {
		setShowErrorPopup(false);
	}


	public boolean isShowErrorPopup() {
		return showErrorPopup;
	}


	public void setShowErrorPopup(boolean showErrorPopup) {
		this.showErrorPopup = showErrorPopup;
	}


	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isFirstEntrance() {
		return firstEntrance;
	}

	public void setFirstEntrance(boolean firstEntrance) {
		this.firstEntrance = firstEntrance;
	}


public FacesContext validate() {
        FacesContext context = FacesContext.getCurrentInstance();
    
        if (newActivity.getName() == null || newActivity.getName().isEmpty()) {
            context.addMessage("activityForm:name", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "Name is required."));
        }
    
        if (newActivity.getDescription() == null || newActivity.getDescription().isEmpty()) {
            context.addMessage("activityForm:description", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "Description is required."));
        }
    
        if (newActivity.getMin_age() == null) {
            context.addMessage("activityForm:min_age", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "min age is required."));
        }
    
        if (newActivity.getMin_age() != null)
        	System.out.println(newActivity.getMin_age().getClass());
        	
        if (newActivity.getMax_age() == null) {
            context.addMessage("activityForm:max_age", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Validation Error", "max age is required."));
        }
		return context;
    }

}
