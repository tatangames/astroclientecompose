package com.tatanstudios.astropollocliente.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.tatanstudios.astropollocliente.extras.Event
import com.tatanstudios.astropollocliente.model.modelos.ModeloDatosBasicos
import com.tatanstudios.astropollocliente.model.modelos.ModeloDirecciones
import com.tatanstudios.astropollocliente.model.modelos.ModeloHistorialOrdenes
import com.tatanstudios.astropollocliente.model.modelos.ModeloHorario
import com.tatanstudios.astropollocliente.model.modelos.ModeloInfoProducto
import com.tatanstudios.astropollocliente.model.modelos.ModeloMenuPrincipal
import com.tatanstudios.astropollocliente.model.modelos.ModeloPoligonos
import com.tatanstudios.astropollocliente.model.modelos.ModeloPremios
import com.tatanstudios.astropollocliente.model.modelos.ModeloProductoHistorialOrdenes
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.http.Field


class LoginViewModel : ViewModel() {
    private val _usuario = MutableLiveData<String>()
    val usuario: LiveData<String> get() = _usuario

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun setUsuario(usuario: String) {
        _usuario.value = usuario
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun verificarUsuarioPasssword(idonesignal: String) {

        // Verificar si ya hay una solicitud en progreso
        if (isRequestInProgress) return

        isRequestInProgress = true
        _isLoading.value = true

        // EL DEVICE IDENTIFICA QUE ESTOY MANDANDO SOLICITUD DESDE ANDROID
        disposable = RetrofitBuilder.getApiService().verificarUsuarioPassword(_usuario.value!!, _password.value!!, idonesignal)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class RegistroViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _usuario = MutableLiveData<String>()
    val usuario: LiveData<String> get() = _usuario

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    private val _correo = MutableLiveData<String?>()
    val correo: LiveData<String?> get() = _correo


    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun setUsuario(usuario: String) {
        _usuario.value = usuario
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun setCorreo(correo: String) {
        _correo.value = correo
    }

    fun registroRetrofit(idonesignal: String?, version: String?) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().registrarme(_usuario.value!!, _password.value!!,  _correo.value, idonesignal, version)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}





class EnviarCorreoApiViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _correo = MutableLiveData<String>()
    val correo: LiveData<String> get() = _correo


    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun setCorreo(correo: String) {
        _correo.value = correo
    }

    fun enviarCorreoRetrofit() {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().enviarCorreoApi(_correo.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class CambiarPasswordEmailViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _password = MutableLiveData<String>()
    val passowrd: LiveData<String> get() = _password

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun setPassword(password: String) {
        _password.value = password
    }

    fun resetearContrasenaEmailRetrofit(idusuario: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().resetearPasswordEmailApi(idusuario, _password.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}





class VerificarCodigoCorreoViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun verificarCodigoRetrofit(codigo: String, correo: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().verificarCodigoEmailApi(codigo, correo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}



// PANTALLA PRINCIPAL -> OPCION MENU
class ListadoMenuPrincipal() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloMenuPrincipal>>()
    val resultado: LiveData<Event<ModeloMenuPrincipal>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun listadoMenuPrincipalRetrofit(id: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoMenuPrincipal(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}



class HistorialFechasBuscarViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloHistorialOrdenes>>()
    val resultado: LiveData<Event<ModeloHistorialOrdenes>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun historialListadonRetrofit(id: String, fecha1: String, fecha2: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoHistorialOrdenes(id, fecha1, fecha2)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class ListadoProductosHistorialOrdenViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloProductoHistorialOrdenes>>()
    val resultado: LiveData<Event<ModeloProductoHistorialOrdenes>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun listaProductosHistorialRetrofit(idorden: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoProductosHistorialOrden(idorden)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class InfoProductoViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloInfoProducto>>()
    val resultado: LiveData<Event<ModeloInfoProducto>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun infoProductoRetrofit(idproducto: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().infoProductosHistorialOrden(idproducto)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}






class ActualizarPasswordViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _password = MutableLiveData<String>()
    val passowrd: LiveData<String> get() = _password

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun setPassword(password: String) {
        _password.value = password
    }

    fun actualizarContrasenaRetrofit(idusuario: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().actualizarPassword(idusuario, _password.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class ListadoHorariosViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloHorario>>()
    val resultado: LiveData<Event<ModeloHorario>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun listadoHorarioRetrofit(idusuario: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoHorario(idusuario)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}





class ListadoPremiosViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloPremios>>()
    val resultado: LiveData<Event<ModeloPremios>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun listadoPremiosRetrofit(idusuario: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoPremios(idusuario)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class SeleccionarPremioViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun seleccionarPremiosRetrofit(idusuario: String, idpremio: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().seleccionarPremios(idusuario, idpremio)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class DeseleccionarPremioViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun DeseleccionarPremiosRetrofit(idusuario: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().quitarSeleccionarPremios(idusuario)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class ListadoDireccionesViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDirecciones>>()
    val resultado: LiveData<Event<ModeloDirecciones>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun listadoDireccionesRetrofit(idusuario: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoDirecciones(idusuario)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}

data class PoligonoUI(
    val id: Int,
    val nombre: String?,
    val puntos: List<LatLng>
)

class ListadoPoligonosViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloPoligonos>>()
    val resultado: LiveData<Event<ModeloPoligonos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    private val _poligonosUI = mutableStateListOf<PoligonoUI>()
    val poligonosUI: List<PoligonoUI> get() = _poligonosUI


    fun listadoPoligonosRetrofit() {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().listadoPoligonos()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false

                    if (response.success == 1) {
                        _poligonosUI.clear()
                        response.lista.forEach { poligono ->
                            val puntos = poligono.listado.mapNotNull { coord ->
                                try {
                                    LatLng(coord.latitud.toDouble(), coord.longitud.toDouble())
                                } catch (e: NumberFormatException) {
                                    null
                                }
                            }

                            _poligonosUI.add(
                                PoligonoUI(
                                    id = poligono.id,
                                    nombre = poligono.nombre,
                                    puntos = puntos
                                )
                            )
                        }
                    }
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}






class RegistroNuevaDireccionViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> get() = _nombre

    private val _direccion = MutableLiveData<String>()
    val direccion: LiveData<String> get() = _direccion

    // UNICO OPCIONAL
    private val _puntoReferencia = MutableLiveData<String?>()
    val puntoReferencia: LiveData<String?> get() = _puntoReferencia

    private val _telefono = MutableLiveData<String>()
    val telefono: LiveData<String> get() = _telefono


    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun setNombre(nombre: String) {
        _nombre.value = nombre
    }

    fun setDireccion(direccion: String) {
        _direccion.value = direccion
    }

    fun setPuntoReferencia(puntoReferencia: String) {
        _puntoReferencia.value = puntoReferencia
    }

    fun setTelefono(telefono: String) {
        _telefono.value = telefono
    }

    fun registrarNuevaDireccionRetrofit(idusuario: String, idzona: String, latitud: String, longitud: String, latitudreal: String?,
                                        longitudreal: String?) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().registrarNuevaDireccion(idusuario, _nombre.value!!,
            _direccion.value!!, _puntoReferencia.value!!, idzona, latitud, longitud, latitudreal, longitudreal,
            _telefono.value!!)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}



class SeleccionarDireccionViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun seleccionarDireccionRetrofit(idusuario: String, dirid: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().seleccionarDireccion(idusuario, dirid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class BorrarDireccionViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun borrarDireccionRetrofit(idusuario: String, dirid: Int) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().borrarDireccion(idusuario, dirid)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}




class InformacionUsuarioViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun informacionUsuarioRetrofit(idusuario: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().informacionUsuario(idusuario)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}


class ActualizarCorreoUsuarioViewModel() : ViewModel() {

    private val _resultado = MutableLiveData<Event<ModeloDatosBasicos>>()
    val resultado: LiveData<Event<ModeloDatosBasicos>> get() = _resultado

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var disposable: Disposable? = null
    private var isRequestInProgress = false

    fun actualizarUsuarioRetrofit(idusuario: String, usuario: String, correo: String) {
        if (isRequestInProgress) return

        isRequestInProgress = true

        _isLoading.value = true
        disposable = RetrofitBuilder.getApiService().actualizarCorreoUsuario(idusuario, usuario, correo)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .retry()
            .subscribe(
                { response ->
                    _isLoading.value = false
                    _resultado.value = Event(response)
                    isRequestInProgress = false
                },
                { error ->
                    _isLoading.value = false
                    isRequestInProgress = false
                }
            )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose() // Limpiar la suscripción
    }
}








