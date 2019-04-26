package com.business.ASMatches;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;

import com.business.businessObjects.Likes;
import com.business.businessObjects.Matches;
import com.business.businessObjects.UserHA;
import com.business.transfers.TMatches;

public class ASMatchesImp implements ASMatches {

	
	@Override
	public boolean acceptLike(TMatches tMatches) {
		
		boolean result = false;
		
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
			EntityManager em = emf.createEntityManager();
			EntityTransaction tr = em.getTransaction();
			tr.begin();

			UserHA userSender; //Usuario 1 es el que acepta el Like (que es el UserReceiver de Likes)
			UserHA userReceiver; //Usuario 2 es el que envió el like
			try {
				String query = "SELECT * FROM USER WHERE NICKNAME = ?1";
				userSender = (UserHA)em.createNativeQuery(query, UserHA.class)
						.setParameter(1, tMatches.getUserSender()).getSingleResult();
				userReceiver = (UserHA)em.createNativeQuery(query, UserHA.class)
						.setParameter(1, tMatches.getUserReceiver()).getSingleResult();
				
				query = "SELECT * FROM MATCHES WHERE (USERONE_NICKNAME = ?1 AND USERTWO_NICKNAME = ?2) OR (USERONE_NICKNAME = ?2 AND USERTWO_NICKNAME = ?1)  ";
				Matches match;
				try {
					
					match = (Matches)em.createNativeQuery(query, Matches.class).setParameter(1, tMatches.getUserReceiver()).setParameter(2, tMatches.getUserSender()).getSingleResult();
				
				}catch(Exception e) {
					
					match = new Matches(userSender, userReceiver);
					em.persist(match);
					userSender.getMatches().add(match); //Aqui se añade el match a la dos usuarios
					userReceiver.getMatches().add(match);
					
					
					userSender.getLikes().remove(new Likes(userReceiver, userSender)); //Aqui se borra el Like del usuario que lo recibio y que ahora ha aceptado
																			//Están cambiados de orden porque en Likes se guarda en el primer
																			// parametro el que envía el like(que ahora es el UserTwo)
					em.persist(userSender);
					em.persist(userReceiver);
					result = true;
					
				}
			}catch(NoResultException e) {}
			
			tr.commit();
			em.close();
			emf.close();
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return result;
	}

	@Override
	public boolean declineLike(TMatches tMatches) {
		
			boolean result = false;	
		try {
			EntityManagerFactory emf = Persistence.createEntityManagerFactory("HostAbroad");
			EntityManager em = emf.createEntityManager();
			EntityTransaction tr = em.getTransaction();
			tr.begin();

			UserHA userSender; //Usuario 1 es el que declina el Like (que es el UserReceiver de Likes)
			UserHA userReceiver; //Usuario 2 es el que envió el like
			try {
				String query = "SELECT * FROM USER WHERE NICKNAME = ?1";
				userSender = (UserHA)em.createNativeQuery(query, UserHA.class)
						.setParameter(1, tMatches.getUserSender()).getSingleResult();
				userReceiver = (UserHA)em.createNativeQuery(query, UserHA.class)
						.setParameter(1, tMatches.getUserReceiver()).getSingleResult();
				
				userSender.getLikes().remove(new Likes(userReceiver, userSender)); //Aqui se borra el Like del usuario que lo recibio y que ahora ha aceptado
																		//Están cambiados de orden porque en Likes se guarda en el primer
																		//parámetro el que envía el like(que ahora es el UserTwo)
					
				em.persist(userSender);
				result = true;
			}catch(NoResultException e) {}
			
			tr.commit();
			em.close();
			emf.close();
			
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

		return result;
	}
	
}