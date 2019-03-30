package com.business.ASUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.business.Host;
import com.business.THost;
import com.business.TUser;
import com.business.User;

public class ASUserImp implements ASUser {

	@Override
	public boolean createUser(TUser tUser) {

		boolean result = false;

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
			EntityManager em = emf.createEntityManager();
			EntityTransaction tr = em.getTransaction();
			tr.begin();

			String consulta = "SELECT * FROM user WHERE user.nickname = :nickname";
			Query query = em.createQuery(consulta);
			query.setParameter("nickname", tUser.getNickname());

			User user = null;

			try {
				user = (User) query.getSingleResult();
			} catch (NoResultException ex) {
				System.out.println(ex.getMessage());
			}

			if (user == null) {
				user = new User(tUser.getNickname(), tUser.getRating(), tUser.getDescription(), tUser.getHost(),
						tUser.getTraveler());
				em.persist(user);
				result = true;
			}
			tr.commit();
			em.close();
			emf.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return result;
	}

	@Override
	public TUser loginUser(TUser user) {
		TUser logedUser = null;

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
			EntityManager em = emf.createEntityManager();
			EntityTransaction tr = em.getTransaction();
			tr.begin();
			String consulta = "SELECT * FROM USER u WHERE u.email = ?1 AND u.password = ?2";
			Query query = em.createNativeQuery(consulta, User.class);
			query.setParameter(1, user.getEmail());
			query.setParameter(2, user.getPassword());
			User result = null;
			
			try {
				result = (User) query.getSingleResult();
			} catch (NoResultException ex) {
				System.out.println(ex.getMessage());
			}
			
			if(result != null) {
				logedUser = new TUser();
                		logedUser.setDescription(result.getDescription());
                		logedUser.setHost(result.getHost());
						logedUser.setTraveler(result.getTraveler());
                		logedUser.setNickname(result.getNickname());
                		logedUser.setRating(result.getRating());
                		logedUser.setEmail(result.getEmail());
                		logedUser.setPassword(result.getPassword());
			}
	
			tr.commit();
			em.close();
			emf.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return logedUser;
	}
	
	/**
	 This method receives tHost with nickname and list of interests.
	 If a user with that nickname doesn't exist the method returns false;
	 If the user exists, then it is checked if the host exists, in which case we just modify the interests.
	 If host does not exist, a new one is created with the corresponding nickname.
	 The user's atribute host (boolean) is updated only if needed.
	**/
	@Override
	public boolean editHostInformation(THost tHost) { 
		
		boolean updated = false;
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("HostAbroad");
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction t = em.getTransaction();
		t.begin();
		
		User user = em.find(User.class, tHost.getNickname());
		
		if(user != null) {
			em.lock(user, LockModeType.OPTIMISTIC);
			Host host;
			
			try {
				String query = "SELECT * FROM HOST WHERE USER_NICKNAME = ?1";
				host = (Host)em.createNativeQuery(query, Host.class).setParameter(1, tHost.getNickname()).getSingleResult();
				host.setListOfInterests(tHost.getListOfInterests());
			}catch(NoResultException e) {
				host = new Host(user, tHost.getListOfInterests());
			}
			
			em.persist(host);
			
			if(!user.getHost()) {
				user.setHost(true);
				user.setHostEntity(host);
				em.persist(user);
			}
			
			updated = true;
		}
		
		try{
			em.getTransaction().commit();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		em.close();
		emfactory.close();
		
		return updated;
	}
	
}