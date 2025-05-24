package com.tatanstudios.astropollocliente.vistas.principal.opciones.premios

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuOpen
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tatanstudios.astropollocliente.R
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColor
import com.tatanstudios.astropollocliente.componentes.BarraToolbarColorMenuPrincipal
import com.tatanstudios.astropollocliente.componentes.CardHorarioV1
import com.tatanstudios.astropollocliente.componentes.CardHorarioV2
import com.tatanstudios.astropollocliente.componentes.CardMisPuntos
import com.tatanstudios.astropollocliente.componentes.CardSeleccionItem
import com.tatanstudios.astropollocliente.componentes.CustomToasty
import com.tatanstudios.astropollocliente.componentes.LoadingModal
import com.tatanstudios.astropollocliente.componentes.ToastType
import com.tatanstudios.astropollocliente.extras.TokenManager
import com.tatanstudios.astropollocliente.model.modelos.ModeloPremiosArray
import com.tatanstudios.astropollocliente.viewmodel.ListadoPremiosViewModel
import kotlinx.coroutines.flow.first

@Composable
fun PremiosScreen(navController: NavHostController,
                   viewModel: ListadoPremiosViewModel = viewModel()
) {
    val ctx = LocalContext.current
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val resultado by viewModel.resultado.observeAsState()
    val scope = rememberCoroutineScope() // Crea el alcance de coroutine
    val tokenManager = remember { TokenManager(ctx) }
    var idusuario by remember { mutableStateOf("") }

    var textoDescripcion by remember { mutableStateOf("") }
    var modeloPremiosArray by remember { mutableStateOf(listOf<ModeloPremiosArray>()) }
    var boolDatosCargados by remember { mutableStateOf(false) }

    var cantidadPuntos by remember { mutableStateOf(0) }

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
                    CardMisPuntos(nombre = textoDescripcion, puntos = cantidadPuntos)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
                // Listado dinámico
                items(modeloPremiosArray) { tipoDato ->
                    // Aquí colocas tu componente personalizado o vista para cada producto

                    CardSeleccionItem(
                        texto = tipoDato.nombre?: "",
                        seleccionado = tipoDato.seleccionado,
                        puntos = tipoDato.puntos,
                        onClick = {
                            tipoDato.seleccionado = if (tipoDato.seleccionado == 1) 0 else 1
                        }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        if (isLoading) {
            LoadingModal(isLoading = true)
        }

        resultado?.getContentIfNotHandled()?.let { result ->
            when (result.success) {
                1 -> {
                    // DATOS CARGADOS
                    textoDescripcion = result.nota?: ""
                    modeloPremiosArray = result.lista
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
    }
}
