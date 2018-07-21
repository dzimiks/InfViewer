package state;

import models.Entity;
import view.DesktopView;

public class StateManager {
	private State currentState;

	private AddState addState;
	private EmptyState emptyState;
	private RelationsState relationsState;
	private SearchState searchState;
	private SortState sortState;
	private UpdateState updateState;
	
	private SortDBState sortDBState;
	private AddDBState addDBState;
	private FilterDBState filterDBState;

	private UpdateDBState updateDBState;


	private DesktopView desktopView;

	public StateManager(Entity entity, DesktopView desktopView) {
		this.desktopView = desktopView;

		addState = new AddState(entity, entity.getChildCount(), this);
		emptyState = new EmptyState();
		relationsState = new RelationsState(entity);
		searchState = new SearchState(entity, entity.getChildCount(), this);
		sortState = new SortState(entity, entity.getChildCount(), this);
		updateState = new UpdateState(entity, entity.getChildCount(), this);
		
		addDBState = new AddDBState(entity, entity.getChildCount(), this);
		updateDBState = new UpdateDBState(entity, entity.getChildCount(), this);
		sortDBState = new SortDBState(entity, entity.getChildCount(), this);
		filterDBState = new FilterDBState(entity,entity.getChildCount(), this);
		
		currentState = emptyState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public void setAddDBState() {
		currentState = addDBState;
	}
	
	public void setAddState() {
		currentState = addState;
	}

	public void setEmptyState() {
		currentState = emptyState;
	}

	public void setRelationsState() {
		currentState = relationsState;
	}

	public void setSearchState() {
		currentState = searchState;
	}

	public void setSortState() {
		currentState = sortState;
	}

	public void setUpdateState() {
		currentState = updateState;
	}
	
	public void setUpdateDBState() {
		currentState = updateDBState;
	}
	
	public void setSortDBState() {
		currentState = sortDBState;
	}
	
	public void setFilterDBState() {
		currentState = filterDBState;
	}

	public State getCurrentState() {
		return currentState;
	}

	public DesktopView getDesktopView() {
		return desktopView;
	}

}
