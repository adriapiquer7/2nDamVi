package ProjecteHibernate;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Function;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public abstract class GenericDao<T, ID extends Serializable> implements IGenericDao<T, ID> {

	protected final SessionFactory sessionFactory;

	protected GenericDao() {
		sessionFactory = SessionManager.getSessionFactory();
	}

	/**
	 * Executa una operació dins d'una transacció. Si l'operació llança una
	 * excepció (JocException o de validació), es fa rollback i es propaga:
	 * així no queda cap canvi a mitges.
	 */
	protected <R> R executar(Function<Session, R> accio) {
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		try {
			R resultat = accio.apply(session);
			tx.commit();
			return resultat;
		} catch (RuntimeException e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		}
	}

	@Override
	public T save(T entity) {
		return executar(s -> {
			s.persist(entity);
			return entity;
		});
	}

	@Override
	public T get(ID id) {
		return executar(s -> s.get(getEntityClass(), id));
	}

	@Override
	public void delete(ID id) {
		executar(s -> {
			T entity = s.get(getEntityClass(), id);
			if (entity != null) {
				s.remove(entity);
			}
			return null;
		});
	}

	@Override
	public List<T> list() {
		return executar(s -> s.createQuery("FROM " + getEntityClass().getSimpleName(), getEntityClass())
				.getResultList());
	}

	@SuppressWarnings("unchecked")
	protected Class<T> getEntityClass() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
