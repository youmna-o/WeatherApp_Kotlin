package com.example.weatherapp.favorite
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.Response
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.data.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavViewModel(private val repo:Repo):ViewModel() {
    private val mutableFavCity: MutableStateFlow<Response<List<FavCity>>> = MutableStateFlow(Response.Loading())
    val FavCiyt: StateFlow<Response<List<FavCity>>> = mutableFavCity.asStateFlow()

    private val mutableMessage: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = mutableMessage

    fun getFavCities() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.getFavCity()
                .catch { ex ->
                    mutableFavCity.value = Response.Failure(ex)
                }
                .collect { list ->
                    if (list.isEmpty()) {
                    }
                    mutableFavCity.value = Response.Success(list)
                }
        }
    }

    fun addFavCity(city: FavCity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repo.insert(city)
                if (result > 0) {
                    mutableMessage.postValue("added")
                } else {
                    mutableMessage.postValue("faild")
                }

            } catch (ex: Exception) {
                mutableMessage.postValue("An erroe ,${ex.message}")
            }

        }

    }

    fun deleteFavCity(city: FavCity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repo.delete(city)
                if (result > 0) {
                    mutableMessage.postValue("added")
                    // getFavProducts()
                } else {
                    mutableMessage.postValue("faild")
                }

            } catch (ex: Exception) {
                mutableMessage.postValue("An erroe ,${ex.message}")
            }

        }

    }
}
//must have the same param of view model
class FavFactory(private val repo: Repo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        //create object of view model
        return FavViewModel(repo) as T

    }
}