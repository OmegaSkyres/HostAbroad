package com.business.ASUser;

import com.business.THost;
import com.business.TUser;

public interface ASUser {
	
	public abstract boolean createUser(TUser user);

	public abstract TUser loginUser(TUser user);
	
	public abstract boolean editHostInformation(THost tHost);
	
}