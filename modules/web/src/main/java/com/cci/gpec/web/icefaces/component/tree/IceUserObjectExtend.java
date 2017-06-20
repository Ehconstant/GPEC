package com.cci.gpec.web.icefaces.component.tree;

import javax.faces.context.FacesContext;
import javax.swing.tree.DefaultMutableTreeNode;

import com.cci.gpec.web.backingBean.navigation.NavigationBB;
import com.icesoft.faces.component.tree.IceUserObject;

public class IceUserObjectExtend extends IceUserObject{

	public IceUserObjectExtend(DefaultMutableTreeNode wrapper) {
		super(wrapper);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String action() {
		NavigationBB navigationBB = 
			(NavigationBB)FacesContext.getCurrentInstance().getCurrentInstance()
							.getExternalContext().getSessionMap().get("navigationBB");
		if(navigationBB.isExitWithoutSaveSalarie()){
			navigationBB.setExitWithoutSaveSalarie(false);
		}
		// TODO Auto-generated method stub
		return super.action();
	}
	
	
	
}
