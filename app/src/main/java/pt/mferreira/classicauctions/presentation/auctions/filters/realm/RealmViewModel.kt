package pt.mferreira.classicauctions.presentation.auctions.filters.realm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch
import pt.mferreira.classicauctions.data.entities.Realm
import pt.mferreira.classicauctions.data.repositories.LocalPreferencesRepositoryImpl
import pt.mferreira.classicauctions.data.repositories.RealmDatabaseRepositoryImpl
import pt.mferreira.classicauctions.domain.usecases.LocalPreferencesUseCase
import pt.mferreira.classicauctions.domain.usecases.RealmDatabaseUseCase
import pt.mferreira.classicauctions.utils.extensions.empty

class RealmViewModel(application: Application) : AndroidViewModel(application) {
	private val _currentRealm: MutableLiveData<String> = MutableLiveData()
	val currentRealm: LiveData<String>
		get() = _currentRealm

	private val realmsDatabaseUseCase =
		RealmDatabaseUseCase(RealmDatabaseRepositoryImpl.getInstance(application))

	private val localPreferencesUseCase = LocalPreferencesUseCase(
		LocalPreferencesRepositoryImpl.getInstance()
	)

	private val _realms: MutableLiveData<List<Realm>> = MutableLiveData()
	val realms: LiveData<List<Realm>>
		get() = _realms

	private val _applyClick: MutableLiveData<String> = MutableLiveData()
	val applyClick: LiveData<String>
		get() = _applyClick

	private val _cancelClick: MutableLiveData<Unit> = MutableLiveData()
	val cancelClick: LiveData<Unit>
		get() = _cancelClick

	private val gson = Gson()

	init {
		viewModelScope.launch {
			_realms.value = realmsDatabaseUseCase.getAllDistinct()
			recallRealm()
		}
	}

	fun onApplyClick(realm: Realm) {
		_applyClick.value = Gson().toJson(realm)
		onClickClose()
	}

	fun onClickClose() {
		_cancelClick.value = Unit
	}

	fun recallRealm() {
		val realm = gson.fromJson(localPreferencesUseCase.getSelectedRealmJson(), Realm::class.java)
		_currentRealm.value = realm?.name ?: String.empty()
	}
}