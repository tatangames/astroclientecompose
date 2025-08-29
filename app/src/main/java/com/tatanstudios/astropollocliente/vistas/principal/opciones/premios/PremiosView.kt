package com.tatanstudios.astropollocliente.vistas.principal.opciones.premios

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.CardMisPuntos
import com.tatanstudios.astropollocliente.componentes.CardSeleccionPremioItem
import com.tatanstudios.astropollocliente.componentes.CustomModal1BotonTitulo
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.modelos.ModeloPremiosArray
import com.tatanstudios.astropollocliente.viewmodel.DeseleccionarPremioViewModel
import com.tatanstudios.astropollocliente.viewmodel.ListadoPremiosViewModel
import com.tatanstudios.astropollocliente.viewmodel.SeleccionarPremioViewModel
import kotlinx.coroutines.flow.first
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun PremiosScreen(navController: NavHostController,
                  viewModel: ListadoPremiosViewModel = viewModel(),
                  viewModelSeleccionar: SeleccionarPremioViewModel = viewModel(),
                  viewModelDeseleccionar: DeseleccionarPremioViewModel = viewModel(),
) {
    val ctx = LocalContext.current
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val resultado by viewModel.resultado.observeAsState()

    val isLoadingSeleccion by viewModelSeleccionar.isLoading.observeAsState(initial = false)
    val resultadoSeleccion by viewModelSeleccionar.resultado.observeAsState()

    val isLoadingDeseleccionar by viewModelDeseleccionar.isLoading.observeAsState(initial = false)
    val resultadoDeseleccionar by viewModelDeseleccionar.resultado.observeAsState()


    val scope = rememberCoroutineScope() // Crea el alcance de coroutine
    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }

    var textoDescripcion by remember { mutableStateOf("") }
    var modeloPremiosArray by remember { mutableStateOf(listOf<ModeloPremiosArray>()) }
    var boolDatosCargados by remember { mutableStateOf(false) }
    var conteoPremiosDisponibles by remember { mutableStateOf(0) }

    var textoMisPuntos by remember { mutableStateOf("") }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var accionSeleccion by remember { mutableStateOf("") } // "seleccionar" o "borrar"
    var itemSeleccionado by remember { mutableStateOf<ModeloPremiosArray?>(null) }

    // titulo y mensaje de respuestas
    var textoTituloApi by remember { mutableStateOf("") }
    var textoMensajeApi by remember { mutableStateOf("") }
    var showDialog1Boton by remember { mutableStateOf(false) }

    val ACCION_SELECCIONAR = stringResource(R.string.seleccionar)
    val ACCION_BORRAR = stringResource(R.string.borrar)


    var idPremioSeleccionado by remember { mutableStateOf(0) }


    LaunchedEffect(Unit) {
        scope.launch {
            idusuario = tokenManager.idUsuario.first()
            viewModel.listadoPremiosRetrofit(idusuario)
        }
    }

    Scaffold(
        topBar = {
            BarraToolbarColor(
                navController,
                stringResource(R.string.menu),
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

            if(boolDatosCargados){
                item {
                    CardMisPuntos(nombre = textoDescripcion, txtPuntos = textoMisPuntos)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (modeloPremiosArray.isEmpty()) {
                    item {
                        // Imagen centrada
                        Column(
                            modifier = Modifier
                                .fillParentMaxSize(), // ocupa el espacio disponible en el LazyColumn
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "No hay Premios Disponibles",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                } else {
                    // Listado dinámico
                    items(modeloPremiosArray) { tipoDato ->
                        // Aquí colocas tu componente personalizado o vista para cada producto

                        CardSeleccionPremioItem(
                            texto = tipoDato.nombre ?: "",
                            seleccionado = tipoDato.seleccionado,
                            puntos = tipoDato.puntos,
                            onClick = {
                                idPremioSeleccionado = tipoDato.id
                                itemSeleccionado = tipoDato
                                accionSeleccion = if (tipoDato.seleccionado == 1) ACCION_BORRAR else ACCION_SELECCIONAR
                                mostrarDialogo = true
                            }
                        )
                    }
                }


                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        if (mostrarDialogo && itemSeleccionado != null) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = {
                    Text(
                        text = if (accionSeleccion == ACCION_SELECCIONAR)
                            stringResource(R.string.decea_seleccionar_este_premio)
                        else
                            stringResource(R.string.decea_borrar_esta_seleccion)
                    )
                },
                text = {
                    Text(
                        text = if (accionSeleccion == ACCION_SELECCIONAR)
                            stringResource(R.string.este_premio_se_agregara_a_tu_seleccion)
                        else
                            stringResource(R.string.este_premio_se_eliminara)
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        mostrarDialogo = false

                        if (accionSeleccion == ACCION_SELECCIONAR) {
                            // Aquí sabes que el usuario tocó "Seleccionar"
                            // Puedes llamar tu lógica o API para seleccionar
                            viewModelSeleccionar.seleccionarPremiosRetrofit(idusuario, idPremioSeleccionado)
                        } else {
                            // Aquí sabes que el usuario tocó "Borrar selección"
                            // Puedes llamar tu lógica o API para desmarcar
                            viewModelDeseleccionar.DeseleccionarPremiosRetrofit(idusuario)
                        }
                    }) {
                        Text(stringResource(R.string.si), color = Color.Black)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) {
                        Text(stringResource(R.string.cancelar), color = Color.Black)
                    }
                }
            )
        }

        if (isLoading) {
            LoadingModal(isLoading = true)
        }

        if (isLoadingSeleccion) {
            LoadingModal(isLoading = true)
        }

        if (isLoadingDeseleccionar) {
            LoadingModal(isLoading = true)
        }


        if(showDialog1Boton){
            CustomModal1BotonTitulo(
                showDialog = showDialog1Boton,
                title = textoTituloApi,
                message = textoMensajeApi,
                onDismiss = {
                    showDialog1Boton = false
                }
            )
        }


        resultado?.getContentIfNotHandled()?.let { result ->
            when (result.success) {
                1 -> {
                    // DATOS CARGADOS
                    textoMisPuntos = result.puntos?: ""
                    textoDescripcion = result.nota?: ""
                    modeloPremiosArray = result.lista
                    boolDatosCargados = true
                    conteoPremiosDisponibles = result.conteo
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

        resultadoSeleccion?.getContentIfNotHandled()?.let { result ->
            when (result.success) {
                1 -> {
                    // PREMIO YA NO ESTA DISPONIBLE
                    textoTituloApi = result.titulo?: ""
                    textoMensajeApi = result.mensaje?: ""
                    showDialog1Boton = true
                }
                2 -> {
                    // PUNTOS NO ALCANZAN
                    textoTituloApi = result.titulo?: ""
                    textoMensajeApi = result.mensaje?: ""
                    showDialog1Boton = true
                }
                3 -> {
                    // SELECCIONADO CORRECTAMENTE
                    CustomToasty(
                        ctx,
                        stringResource(id = R.string.seleccionado),
                        ToastType.SUCCESS
                    )
                    // RECARGAR LISTADO
                    scope.launch {
                        viewModel.listadoPremiosRetrofit(idusuario)
                    }
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

        resultadoDeseleccionar?.getContentIfNotHandled()?.let { result ->
            when (result.success) {
                1 -> {
                    // ACTUALIZADO
                    CustomToasty(
                        ctx,
                        stringResource(id = R.string.seleccionado),
                        ToastType.SUCCESS
                    )
                    // RECARGAR LISTADO
                    scope.launch {
                        viewModel.listadoPremiosRetrofit(idusuario)
                    }
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

    }
}
