package carinaTrivago.carina.demo.db.mappers;

import carinaTrivago.carina.demo.db.models.UserPreference;

public interface UserPreferenceMapper {

	void create(UserPreference userPreference);

	UserPreference findById(Long id);

}
