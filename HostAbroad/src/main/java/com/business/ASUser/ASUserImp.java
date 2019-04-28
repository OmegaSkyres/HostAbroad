package com.business.ASUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import com.business.businessObjects.Host;
import com.business.businessObjects.Interest;
import com.business.businessObjects.Language;
import com.business.businessObjects.Likes;
import com.business.businessObjects.Place;
import com.business.businessObjects.Rating;
import com.business.businessObjects.Traveler;
import com.business.businessObjects.UserHA;
import com.business.enums.InterestsEnum;
import com.business.enums.LanguagesEnum;
import com.business.transfers.THost;
import com.business.transfers.TPlace;
import com.business.transfers.TRating;
import com.business.transfers.TTraveler;
import com.business.transfers.TUser;

public class ASUserImp implements ASUser {

	/**
	 * This method checks if a user with that nickname or password exists. the
	 * password is encrypted with hashCode
	 **/
	@Override
	public boolean createUser(TUser tUser) {

		boolean result = false;

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
			EntityManager em = emf.createEntityManager();
			EntityTransaction tr = em.getTransaction();
			tr.begin();

			UserHA user;
			try {
				String query = "SELECT * FROM USERHA WHERE NICKNAME = ?1 OR EMAIL = ?2";
				user = (UserHA) em.createNativeQuery(query, UserHA.class).setParameter(1, tUser.getNickname())
						.setParameter(2, tUser.getEmail()).getSingleResult();

			} catch (NoResultException e) {
				user = new UserHA(tUser.getNickname(), tUser.getFullName(), tUser.getEmail(),
						tUser.getPassword().hashCode());
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

	/**
	 * this method logs in the user if it exists
	 */
	@Override
	public TUser loginUser(TUser tUser) {
		TUser logedUser = null;

		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
			EntityManager em = emf.createEntityManager();
			EntityTransaction tr = em.getTransaction();
			tr.begin();

			String consulta = "SELECT * FROM USERHA WHERE email = ?1 AND password = ?2";
			UserHA result;
			try {
				result = (UserHA) em.createNativeQuery(consulta, UserHA.class).setParameter(1, tUser.getEmail())
						.setParameter(2, tUser.getPassword().hashCode()).getSingleResult();
				logedUser = new TUser();

				logedUser.setNickname(result.getNickname());
				logedUser.setFullName(result.getFullName());
				logedUser.setPassword(result.getPassword().toString());
				logedUser.setRating(result.getRating());
				logedUser.setDescription(result.getDescription());
				logedUser.setHost(result.getHost());
				logedUser.setTraveler(result.getTraveler());
				logedUser.setEmail(result.getEmail());
			} catch (NoResultException ex) {
				System.out.println(ex.getMessage());
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
	 * This method receives tHost with nickname and list of interests. If a user
	 * with that nickname doesn't exist the method returns false; If the user
	 * exists, then it is checked if the host exists, in which case we just modify
	 * the interests. If host does not exist, a new one is created with the
	 * corresponding nickname. The user's atribute host (boolean) is updated only if
	 * needed.
	 **/
	@Override
	public boolean editHostInformation(THost tHost) {

		boolean updated = false;
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("HostAbroad");
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction t = em.getTransaction();
		t.begin();

		UserHA user = em.find(UserHA.class, tHost.getNickname());

		if (user != null) {
			Host host;

			try {
				String query = "SELECT * FROM HOST WHERE USER_NICKNAME = ?1";
				host = (Host) em.createNativeQuery(query, Host.class).setParameter(1, tHost.getNickname())
						.getSingleResult();
				host.setListOfInterests(tHost.getListOfInterests());
			} catch (NoResultException e) {
				host = new Host(user, tHost.getListOfInterests());
			}

			em.persist(host);

			if (!user.getHost()) {
				user.setHost(true);
				user.setHostEntity(host);
				em.persist(user);
			}

			updated = true;
		}

		try {
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		em.close();
		emfactory.close();

		return updated;
	}

	/**
	 * This method edits a traveler information
	 */
	@Override
	public boolean editTravelerInformation(TTraveler tTraveler) {
		boolean updated = false;

		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("HostAbroad");
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction t = em.getTransaction();
		t.begin();

		UserHA user = em.find(UserHA.class, tTraveler.getNickname());

		if (user != null) {
			Traveler traveler;

			try {
				String query = "SELECT * FROM TRAVELER WHERE USER_NICKNAME = ?1";
				traveler = (Traveler) em.createNativeQuery(query, Traveler.class)
						.setParameter(1, tTraveler.getNickname()).getSingleResult();
				traveler.setDurationOfStay(tTraveler.getDurationOfStay());
				traveler.setListOfCountries(tTraveler.getListOfCountries());
				traveler.setListOfKnowledges(tTraveler.getListOfKnowledges());
			} catch (NoResultException e) {
				traveler = new Traveler(user, tTraveler.getListOfCountries(), tTraveler.getListOfKnowledges(),
						tTraveler.getDurationOfStay());
			}

			em.persist(traveler);

			if (!user.getTraveler()) {
				user.setTraveler(true);
				user.setTravelerEntity(traveler);
				em.persist(user);
			}

			updated = true;
		}

		try {
			em.getTransaction().commit();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		em.close();
		emfactory.close();

		return updated;
	}

	/**
	 * This method adds a place to a existing host
	 */
	@Override
	public boolean addPlace(TPlace tPlace) {
		boolean updated = false;

		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("HostAbroad");
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction t = em.getTransaction();
		t.begin();
		Host host;
		String consulta = "SELECT * FROM HOST WHERE USER_NICKNAME = ?1";
		try {
			host = (Host) em.createNativeQuery(consulta, Host.class).setParameter(1, tPlace.getNickname())
					.getSingleResult();
			consulta = "SELECT * FROM PLACE WHERE ADDRESS = ?1 AND HOST_ID = ?2";
			Place place;
			try {
				place = (Place) em.createNativeQuery(consulta, Place.class).setParameter(1, tPlace.getAddress())
						.setParameter(2, host.getId()).getSingleResult();
			} catch (NoResultException e) {
				place = new Place();
				place.setHost(host);
				place.setAddress(tPlace.getAddress());
				host.getPlaces().add(place);
			}

			place.setDescription(tPlace.getDescription());
			place.setFamilyUnit(tPlace.getFamilyUnit());
			place.setNoAvaliableDates(tPlace.getNoAvaliableDates());
			place.setPhoto(tPlace.getPhoto());
			em.persist(place);
			em.persist(host);
			updated = true;
		} catch (NoResultException e) {
		}

		em.getTransaction().commit();
		em.close();
		emfactory.close();
		return updated;
	}

	@Override
	public THost readHostInformation(TUser user) {
		THost host = null;
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("HostAbroad");
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction t = em.getTransaction();
		t.begin();
		
		String query = "SELECT * FROM HOST WHERE USER_NICKNAME = ?1";
		try {
			Host hostEntity = (Host)em.createNativeQuery(query, Host.class)
					.setParameter(1, user.getNickname()).getSingleResult();
			host = hostEntity.toTransfer();	
		}catch(NoResultException e) {}
		
		em.getTransaction().commit();
        em.close();
 		emfactory.close();
		return host;
	}

	@Override
	public TTraveler readTravelerInformation(TUser user) {

		TTraveler traveler = null;
		EntityManagerFactory emfactory = Persistence.createEntityManagerFactory("HostAbroad");
		EntityManager em = emfactory.createEntityManager();
		EntityTransaction t = em.getTransaction();
		t.begin();
		
		String query = "SELECT * FROM TRAVELER WHERE USER_NICKNAME = ?1";
		try {
			Traveler travelerEntity = (Traveler)em.createNativeQuery(query, Traveler.class)
					.setParameter(1, user.getNickname()).getSingleResult();
			traveler = travelerEntity.toTransfer();	
		}catch(NoResultException e) {}
		
		em.getTransaction().commit();
        em.close();
 		emfactory.close();
		return traveler;
	}

	public ArrayList<TUser> sendersLike(TUser tUser) {

		
		 ArrayList<TUser> sendersUser = new ArrayList<TUser>();
	
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
			EntityManager em = emf.createEntityManager();
			EntityTransaction tr = em.getTransaction();
			tr.begin();
			
			TUser tUserSender;
			for(Integer id : tUser.getLikes()) {
				
				String consulta = "SELECT * FROM LIKES WHERE id = ?1";
				Query query = em.createNativeQuery(consulta, Likes.class);
				query.setParameter(1, id);
	
				Likes like = null;
				
				try {
					like = (Likes) query.getSingleResult();
				}
				catch (NoResultException ex) {
					System.out.println(ex.getMessage());
				}
			
				tUserSender = new TUser(like.getUserSender().getNickname(), like.getUserSender().getRating(),
						like.getUserSender().getDescription());
				
				sendersUser.add(tUserSender);
				
			}
			em.close();
			emf.close();
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}		
		
		
		return  sendersUser;
	}

	/**
	 * This method modifies the basic information of an user
	 * */
	public boolean modifyInformation(TUser tUser) {
		boolean isEditPossible = true;
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tr = em.getTransaction();
		tr.begin();
		
		UserHA user = em.find(UserHA.class, tUser.getNickname());
		em.lock(user, LockModeType.OPTIMISTIC);
		
		String newEmail = tUser.getEmail();
		
		if(!newEmail.equals(user.getEmail())) {
			String query = "SELECT * FROM USER WHERE EMAIL = ?1";
			try {
				UserHA userWithEmailFromTransfer = (UserHA) em.createNativeQuery(query, UserHA.class)
																	.setParameter(1, tUser.getEmail())
																	.getSingleResult();
				isEditPossible = false;
			}catch(NoResultException ex) {
			}
		}
		
		if(isEditPossible) {
			user.setFullName(tUser.getFullName());
			user.setEmail(newEmail);
			user.setDescription(tUser.getDescription());
			user.setPhoto(tUser.getPhoto());
			user.setGender(tUser.getGender());
			user.setBirthday(tUser.getBirthday());
			this.newLanguages(user.getLanguages(), tUser.getLanguages(), em, user);
			em.persist(user);
		}
		
		tr.commit();
		em.close();
		emf.close();
		return isEditPossible;
	}
	
	@SuppressWarnings("unchecked")
	private void newLanguages(List<Language> oldLanguages, 
			TreeSet<LanguagesEnum> newLanguages, EntityManager em, UserHA user){
		int i = 0;
		int j = 0;
		Collections.sort(oldLanguages);
		
		ArrayList<LanguagesEnum> newLanguagesA = new ArrayList<LanguagesEnum>(newLanguages);
		
		while(i < oldLanguages.size() && j < newLanguagesA.size()) {
			if(oldLanguages.get(i).getLanguage().equals(newLanguagesA.get(j).getString())) {
				i++;
				j++;
			}
			else if(oldLanguages.get(i).getLanguage().compareTo(newLanguagesA.get(j).getString()) < 0) {
				em.remove(oldLanguages.get(i));
				i++;
			}
			else {
				em.persist(new Language(user, newLanguagesA.get(j).getString()));
				j++;
			}
		}
		
		while(i < oldLanguages.size()) {
			em.remove(oldLanguages.get(i));
			i++;
		}
		
		while(j < newLanguagesA.size()) {
			em.persist(new Language(user, newLanguagesA.get(j).getString()));
			j++;
		}
	}
	
	@Override
	public boolean rateUser(TRating tRating) {

		boolean result = false; //Devolverá false si ha habido algún error o si ese Sender ya ha valorado a Receiver
		
		try {
			
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
			EntityManager em = emf.createEntityManager();
			EntityTransaction tr = em.getTransaction();
			tr.begin();

			UserHA userSender; // Usuario 1 es el que envia valoracion
			UserHA userReceiver; // Usuario 2 es el que la recibe

			try {
				
				String query = "SELECT * FROM USERHA WHERE NICKNAME = ?1";
				userSender = (UserHA) em.createNativeQuery(query, UserHA.class).setParameter(1, tRating.getUserSender())
						.getSingleResult();
				userReceiver = (UserHA) em.createNativeQuery(query, UserHA.class).setParameter(1, tRating.getUserReceiver())
						.getSingleResult();

				query = "SELECT * FROM RATING WHERE USERRECEIVER_NICKNAME = ?1 AND USERSENDER_NICKNAME = ?2";
				Rating rating;
				
				try {

					rating = (Rating) em.createNativeQuery(query, Rating.class)
							.setParameter(1, tRating.getUserReceiver()).setParameter(2, tRating.getUserSender())
							.getSingleResult();

				} catch (Exception e) {

					rating = new Rating(userSender, userReceiver, tRating.getRate());
					em.persist(rating);

					userReceiver.addRate(rating);
					userReceiver.updateRating(); // Vuelve a calcular la valoracion total para
																	// actualizarla

					em.persist(userReceiver);
					result = true;

				}
			} catch (NoResultException e) {}

			tr.commit();
			em.close();
			emf.close();
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return result;
	}

	@Override
	public TUser readUserNickName(TUser user) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
		EntityManager em = emf.createEntityManager();
		
		String queryStr = "SELECT NICKNAME FROM USERHA WHERE EMAIL = ?1";
		Query query = em.createNativeQuery(queryStr, UserHA.class);
		query.setParameter(1, user.getEmail());
		TUser nickname = null;
		try {
			UserHA userNick = (UserHA)query.getSingleResult();
			nickname = userNick.toTransfer();
		}
		catch (NoResultException ex) {
			System.out.println(ex.getMessage());
		}
		
		em.close();
		emf.close();
		
		return nickname;
	}

	@Override
	public void modifyInterests(TUser tUser) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
		EntityManager em = emf.createEntityManager();
		EntityTransaction tr = em.getTransaction();
		tr.begin();
		
		UserHA user = em.find(UserHA.class, tUser.getNickname());
		em.lock(user, LockModeType.OPTIMISTIC);
		
		List<Interest> oldInterests = user.getInterests();
		ArrayList<InterestsEnum> newInterests = new ArrayList<InterestsEnum>(tUser.getInterests());
		int i = 0;
		int j = 0;
		Collections.sort(oldInterests);
		
		while(i < oldInterests.size() && j < newInterests.size()) {
			if(oldInterests.get(i).getInterest().equals(newInterests.get(j).getString())) {
				i++;
				j++;
			}
			else if(oldInterests.get(i).getInterest().compareTo(newInterests.get(j).getString()) < 0) {
				em.remove(oldInterests.get(i));
				i++;
			}
			else {
				em.persist(new Interest(user, newInterests.get(j).getString()));
				j++;
			}
		}
		
		while(i < oldInterests.size()) {
			em.remove(oldInterests.get(i));
			i++;
		}
		
		while(j < newInterests.size()) {
			em.persist(new Interest(user, newInterests.get(j).getString()));
			j++;
		}
		tr.commit();
		em.close();
		emf.close();
	}
	
}
