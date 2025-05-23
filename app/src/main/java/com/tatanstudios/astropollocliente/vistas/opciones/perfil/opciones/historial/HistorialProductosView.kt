package com.tatanstudios.astropollocliente.vistas.opciones.perfil.opciones.historial

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ProductoListadoHistorialItemCard
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.modelos.ModeloProductoHistorialOrdenesArray
import com.tatanstudios.astropollocliente.model.rutas.Routes
import com.tatanstudios.astropollocliente.network.RetrofitBuilder
import com.tatanstudios.astropollocliente.viewmodel.ListadoProductosHistorialOrdenViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ListadoProductosHistorialScreen(navController: NavHostController, _idorden: Int,
                                    viewModelProductosListaHistorial: ListadoProductosHistorialOrdenViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    // listado de productos
    val isLoading by viewModelProductosListaHistorial.isLoading.observeAsState(initial = false)
    val resultado by viewModelProductosListaHistorial.resultado.observeAsState()
    var boolDatosCargados by remember { mutableStateOf(false) }

    var modeloListaProductosHistorialArray by remember { mutableStateOf(listOf<ModeloProductoHistorialOrdenesArray>()) }

    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }

    //val keyboardController = LocalSoftwareKeyboardController.current

    var _idProducto by remember { mutableStateOf(0) }


    // Lanzar la solicitud cuando se carga la pantalla
    LaunchedEffect(Unit) {
        scope.launch {
            idusuario = tokenManager.idUsuario.first()
            viewModelProductosListaHistorial.listaProductosHistorialRetrofit(idorden = _idorden)
        }
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.productos),
                colorResource(R.color.colorRojo)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Listado dinámico
            items(modeloListaProductosHistorialArray) { tipoProducto ->
                // Aquí colocas tu componente personalizado o vista para cada producto

                ProductoListadoHistorialItemCard(
                    cantidad = tipoProducto.cantidad.toString(),
                    hayImagen = tipoProducto.utilizaImagen,
                    imagenUrl = "${RetrofitBuilder.urlImagenes}${tipoProducto.imagen}",
                    titulo = tipoProducto.nombreProducto?: "",
                    descripcion = tipoProducto.nota?: "",
                    precio = tipoProducto.precio?: "",
                    onClick = {

                        navController.navigate(
                            Routes.VistaInfoProductoHistorialOrden.createRoute(
                                tipoProducto.id.toString(),
                            ),
                            navOptions {
                                launchSingleTop = true
                            }
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }



        if (isLoading) {
            LoadingModal(isLoading = true)
        }


        resultado?.getContentIfNotHandled()?.let { result ->
            when (result.success) {
                1 -> {
                    // LISTADO DE PRODUCTOS
                    modeloListaProductosHistorialArray = result.lista
                    boolDatosCargados = true

                }
                else -> {
                    // Error, recargar de nuevo
                    CustomToasty(
                        ctx,
                        stringResource(id = R.string.error_reintentar_de_nuevo),
                        ToastType.ERROR
                    )
                }
            }
        }


    } // end-scalfold


}
