package io.github.sdwfqin.app_kt.ui.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.LogUtils
import com.sdwfqin.quicklib.mvvm.BaseViewModel
import io.github.sdwfqin.app_kt.retrofit.WeatherRepository
import kotlinx.coroutines.launch
import java.util.*

/**
 * 天气ViewModel
 *
 * @author 张钦
 * @date 2020/4/15
 */
class WeatherViewModel() : BaseViewModel() {

    private val repository: WeatherRepository = WeatherRepository()

    val weatherBean = MutableLiveData<WeatherBean>()

    fun loadWeather() {

        isLoading.postValue(true)

        val map: Map<String, Any> = HashMap<String, Any>()

        launch({
            weatherBean.postValue(repository.getWeather(map))
        }, {
            LogUtils.e(it)
            networkError.postValue(it)
        }, {
            isLoading.postValue(false)
        })
    }

    private fun launch(block: suspend () -> Unit, error: suspend (Throwable) -> Unit, complete: suspend () -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            error(e)
        } finally {
            complete()
        }
    }
}