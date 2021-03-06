package com.business.ASUser;

import java.util.ArrayList;

import com.business.transfers.THost;
import com.business.transfers.TPlace;
import com.business.transfers.TRating;
import com.business.transfers.TTraveler;
import com.business.transfers.TUser;

public interface ASUser {
	
	public abstract boolean createUser(TUser user);

	public abstract TUser loginUser(TUser user);
	
	public abstract boolean editHostInformation(THost tHost);

	public abstract boolean editTravelerInformation(TTraveler tTraveler);
	
	public abstract boolean addPlace(TPlace place);
	
	public abstract THost readHostInformation(TUser user);
	
	public abstract TTraveler readTravelerInformation(TUser user);
  
	public abstract boolean modifyInformation(TUser tUser);

	public abstract TUser readUserNickName(TUser user);
	
	public abstract ArrayList<TUser> getMyLikes(TUser tUser);

	public abstract boolean rateUser(TRating tRating);

	public abstract ArrayList<TUser> readMyMatches(TUser transfer);

	public TUser readUser(TUser user);
	
	public abstract void modifyInterests(TUser tUser);
}
