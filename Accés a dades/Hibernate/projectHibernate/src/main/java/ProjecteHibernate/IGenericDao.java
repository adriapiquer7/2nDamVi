package ProjecteHibernate;

import java.io.Serializable;
import java.util.List;

public interface IGenericDao<T, ID extends Serializable> {

	/** INSERT d'una entitat nova. L'objecte retornat té l'id generat. */
	T save(T entity);

	/** SELECT per id. Retorna null si no existeix. */
	T get(ID id);

	/** DELETE per id */
	void delete(ID id);

	/** SELECT ALL */
	List<T> list();
}
